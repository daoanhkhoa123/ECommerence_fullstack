CREATE TABLE chat_messages (
    chat_message_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    content TEXT NOT NULL
)
INHERITS (base_entity);

CREATE TRIGGER trg_chat_messages_baseent
BEFORE INSERT OR UPDATE ON chat_messages
FOR EACH ROW
EXECUTE FUNCTION append_baseent();
