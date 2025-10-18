CREATE OR REPLACE FUNCTION enforce_vendor_product_existence()
RETURNS TRIGGER AS $$
BEGIN
    -- ensure vendor is active and exists
    IF NOT EXISTS(
        SELECT 1 FROM vendors v 
        WHERE v.vendor_id = NEW.vendor_id AND v.is_active = TRUE
    ) THEN
        RAISE EXCEPTION format('Vendor % is not active or does not exist', NEW.vendor_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM products p
        WHERE p.product_id = NEW.product_id AND p.is_active = TRUE
    ) THEN
        RAISE EXCEPTION format('Product % is not active or does not exist', NEW.product_id);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_vendor_product_existence
BEFORE INSERT OR UPDATE ON vendors_products
FOR EACH ROW EXECUTE FUNCTION enforce_vendor_product_existence();

