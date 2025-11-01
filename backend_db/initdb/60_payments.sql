CREATE TABLE payments(
    payment_id SERIAL PRIMARY KEY,
    order_id INT UNIQUE NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    payment_method VARCHAR(30) NOT NULL,
    transaction_ref VARCHAR(100),
    paid_amount NUMERIC(12,2) CHECK (paid_amount >= 0),
    paid_at TIMESTAMP
    
)
INHERITS (base_entity);

CREATE OR REPLACE FUNCTION enforce_payment_exclusivity()
RETURNS TRIGGER AS $$
BEGIN 
    IF NEW.payment_status = 'completed' AND NEW.paid_at IS NULL THEN 
        NEW.paid_at = now();   
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_payments_baseent
BEFORE INSERT OR UPDATE ON payments
FOR EACH ROW EXECUTE FUNCTION append_baseent();