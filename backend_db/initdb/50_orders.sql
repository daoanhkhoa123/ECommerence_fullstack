CREATE TABLE orders(
    order_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(customer_id),
    order_status VARCHAR(20) NOT NULL DEFAULT 'pending',
    total_amount NUMERIC(12,2) DEFAULT 0 CHECK (total_amount >= 0),
    shipping_address TEXT NOT NULL,
    placed_at TIMESTAMP DEFAULT now()
)
INHERITS (base_entity);

CREATE TRIGGER trg_orders_baseent
BEFORE INSERT OR UPDATE ON orders
FOR EACH ROW EXECUTE FUNCTION append_baseent();