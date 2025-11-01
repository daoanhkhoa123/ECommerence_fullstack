-- Join table for many-to-many relationship between products and categories
CREATE TABLE products_categories (
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    category_id INT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
)
INHERITS (base_entity);

CREATE TRIGGER trg_products_categories_baseent
BEFORE INSERT OR UPDATE ON products_categories
FOR EACH ROW EXECUTE FUNCTION append_baseent();
