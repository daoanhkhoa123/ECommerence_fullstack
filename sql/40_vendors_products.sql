CREATE TABLE vendors_products (
    vendor_product_id SERIAL  PRIMARY KEY,
    vendor_id INT NOT NULL REFERENCES vendors(vendor_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    sku VARCHAR(50) UNIQUE,
    is_featured BOOLEAN DEFAULT FALSE,
    
    UNIQUE (vendor_id, product_id)
)
INHERITS (base_entity);

CREATE OR REPLACE FUNCTION enforce_vendor_product_existence()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM vendors v
        WHERE v.vendor_id = NEW.vendor_id
          AND v.is_active = TRUE
    ) THEN
        RAISE EXCEPTION 'Cannot insert/update vendor_product: vendor % is inactive or does not exist', NEW.vendor_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_vendor_products_baseent
BEFORE INSERT OR UPDATE ON vendors_products
FOR EACH ROW EXECUTE FUNCTION append_baseent();

CREATE TRIGGER trg_check_vendor_product_existence
BEFORE INSERT OR UPDATE ON vendors_products
FOR EACH ROW EXECUTE FUNCTION enforce_vendor_product_existence();
