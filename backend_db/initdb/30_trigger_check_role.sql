CREATE OR REPLACE FUNCTION enforce_account_role_exclusivity()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_TABLE_NAME = 'customers' THEN
        IF (SELECT role FROM accounts WHERE account_id = NEW.account_id) <> 'CUSTOMER' THEN
            RAISE EXCEPTION 'Account role mismatch: must be "customer" for customers table';
        END IF;
    ELSIF TG_TABLE_NAME = 'vendors' THEN
        IF (SELECT role FROM accounts WHERE account_id = NEW.account_id) <> 'VENDOR' THEN
            RAISE EXCEPTION 'Account role mismatch: must be "vendor" for vendors table';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_customer_role
BEFORE INSERT ON customers
FOR EACH ROW EXECUTE FUNCTION enforce_account_role_exclusivity();

CREATE TRIGGER trg_check_vendor_role
BEFORE INSERT ON vendors
FOR EACH ROW EXECUTE FUNCTION enforce_account_role_exclusivity();
