CREATE TABLE vendors (
    vendor_id SERIAL PRIMARY KEY,
    account_id INT UNIQUE NOT NULL REFERENCES accounts(account_id),
    shop_name VARCHAR(100) NOT NULL,
    description TEXT,
    phone VARCHAR(20)
) INHERITS (base_entity);

CREATE TRIGGER trg_vendors_baseent
BEFORE INSERT OR UPDATE ON vendors
FOR EACH ROW EXECUTE FUNCTION append_baseent();
