-- ============================================
-- 99_sample_datas.sql
-- Seed data for testing and development
-- ============================================

-- === Accounts ===
INSERT INTO accounts (email, password_hash, role)
VALUES 
    ('alice@example.com', 'hashed_pwd_1', 'customer'),
    ('bob@example.com', 'hashed_pwd_2', 'customer'),
    ('shop1@example.com', 'hashed_pwd_3', 'vendor'),
    ('shop2@example.com', 'hashed_pwd_4', 'vendor');

-- === Customers ===
INSERT INTO customers (account_id, full_name, phone, address)
VALUES 
    (1, 'Alice Nguyen', '0901123456', '123 Main Street, Hanoi'),
    (2, 'Bob Tran', '0902987654', '456 Second Ave, HCMC');

-- === Vendors ===
INSERT INTO vendors (account_id, shop_name, description, phone)
VALUES
    (3, 'TechStore', 'Electronics and gadgets', '0912123123'),
    (4, 'FashionHub', 'Trendy clothing and accessories', '0987766554');

-- === Categories ===
INSERT INTO categories (name, description)
VALUES
    ('Electronics', 'Devices and gadgets'),
    ('Clothing', 'Apparel and fashion'),
    ('Footwear', 'Shoes and sneakers');

-- === Products ===
INSERT INTO products (name, description, category_id, brand, image_url)
VALUES
    ('iPhone 15', 'Latest Apple smartphone', 1, 'Apple', 'https://example.com/iphone15.jpg'),
    ('MacBook Air M3', 'Lightweight and powerful laptop', 1, 'Apple', 'https://example.com/macbookair.jpg'),
    ('Leather Jacket', 'Premium leather jacket', 2, 'UrbanStyle', 'https://example.com/jacket.jpg'),
    ('Sneakers', 'Comfortable casual sneakers', 3, 'RunFast', 'https://example.com/sneakers.jpg');

-- === Products Categories ===
INSERT INTO products_categories (product_id, category_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 3);

-- === Vendor Products (listings per vendor) ===
INSERT INTO vendors_products (vendor_id, product_id, price, stock, sku, is_featured)
VALUES
    (1, 1, 25000000, 10, 'TS-IPH15', TRUE),
    (1, 2, 32000000, 5,  'TS-MBA-M3', FALSE),
    (2, 3, 1200000, 20, 'FH-LJACK', TRUE),
    (2, 4, 900000, 50,  'FH-SNK01', FALSE);

-- === Orders ===
INSERT INTO orders (customer_id, order_status, total_amount, shipping_address)
VALUES
    (1, 'pending', 25900000, '123 Main Street, Hanoi'),
    (2, 'paid', 41000000, '456 Second Ave, HCMC');

-- === Order Items ===
INSERT INTO orders_items (order_id, vendor_product_id, quantity)
VALUES
    (1, 1, 1),
    (1, 4, 1),
    (2, 2, 1),
    (2, 3, 1);

-- === Payments ===
INSERT INTO payments (order_id, payment_method, payment_status, transaction_ref, paid_amount, paid_at)
VALUES
    (1, 'cod', NULL, NULL, NULL),
    (2, 'credit_card', 'TXN123456', 41000000, now());
