CREATE TABLE accounts (
    account_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL
) INHERITS (base_entity);

CREATE TRIGGER trg_accounts_baseent
BEFORE INSERT OR UPDATE ON accounts
FOR EACH ROW
EXECUTE FUNCTION append_baseent();
