CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,  
    category_id INT NOT NULL REFERENCES categories(category_id),
    brand VARCHAR(100),
    image_url TEXT
)
INHERITS (base_entity);

CREATE TRIGGER trg_products_baseent
BEFORE INSERT OR UPDATE ON products
FOR EACH ROW EXECUTE FUNCTION append_baseent();
