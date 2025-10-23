CREATE OR REPLACE FUNCTION append_baseent()
RETURNS TRIGGER AS $$
BEGIN
    -- Always update updated_at
    NEW.updated_at = now();

    -- On insert, initialize created_at and is_active if not provided
    IF TG_OP = 'INSERT' THEN
        NEW.created_at = now();

        IF NEW.is_active IS NULL THEN
            NEW.is_active = TRUE;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
