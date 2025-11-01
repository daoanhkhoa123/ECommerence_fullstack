CREATE TABLE orders_items(
    order_item_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    vendor_product_id INT NOT NULL REFERENCES vendors_products(vendor_product_id),
    quantity INT NOT NULL CHECK (quantity > 0),
    subtotal NUMERIC(12,2) GENERATED ALWAYS AS (quantity * price) STORED
    
)
INHERITS (base_entity);


CREATE OR REPLACE FUNCTION enforce_order_item_exclusivity()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS(
        SELECT 1 FROM vendors_products vp
        WHERE vp.vendor_product_id = NEW.vendor_product_id AND vp.is_active=TRUE
    ) THEN 
        RAISE EXCEPTION 'Vendor product % is not active or does not exist', NEW.vendor_product_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_enforce_order_item_exclusivity
BEFORE INSERT OR UPDATE ON orders_items
FOR EACH ROW EXECUTE FUNCTION enforce_order_item_exclusivity();

CREATE TRIGGER trg_orders_items_baseent
BEFORE INSERT OR UPDATE ON orders_items
FOR EACH ROW EXECUTE FUNCTION append_baseent();