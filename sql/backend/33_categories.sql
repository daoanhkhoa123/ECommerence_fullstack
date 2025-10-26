CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT
)
INHERITS (base_entity);

CREATE TRIGGER trg_products_baseent
BEFORE INSERT OR UPDATE ON categories
FOR EACH ROW EXECUTE FUNCTION append_baseent();
