CREATE TABLE chat_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id INT NOT NULL,
    context_summary TEXT,
    intent VARCHAR(100)
);

CREATE OR REPLACE FUNCTION validate_accountid()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT  EXISTS(
        SELECT  1  FROM accounts a WHERE a.account_id = NEW.account_id
    )  THEN 
        RAISE EXCEPTION 'Account %s does not exist in backend databse', NEW.account_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_accountid
BEFORE INSERT OR UPDATE ON chat_sessions
FOR EACH ROW
EXECUTE FUNCTION validate_accountid();