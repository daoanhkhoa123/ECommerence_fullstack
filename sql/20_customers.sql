CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    account_id INT UNIQUE NOT NULL REFERENCES accounts(account_id),
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    birth_date DATE
) INHERITS (base_entity);

CREATE TRIGGER trg_customers_baseent
BEFORE INSERT OR UPDATE ON customers
FOR EACH ROW EXECUTE FUNCTION append_baseent();
