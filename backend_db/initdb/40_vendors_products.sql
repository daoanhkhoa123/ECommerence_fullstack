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

CREATE TRIGGER trg_vendor_products_baseent
BEFORE INSERT OR UPDATE ON vendors_products
FOR EACH ROW EXECUTE FUNCTION append_baseent();
