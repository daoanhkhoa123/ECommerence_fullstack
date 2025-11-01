SELECT
    o.order_id,
    o.placed_at AS order_date,
    o.order_status,
    o.total_amount,
    o.shipping_address,
    c.customer_id,
    c.full_name AS customer_name,
    c.phone AS customer_phone,
    oi.order_item_id,
    p.product_id,
    p.name AS product_name,
    v.vendor_id,
    v.shop_name AS vendor_name,
    oi.quantity,
    oi.price AS item_price,
    oi.subtotal AS total_item_price
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN orders_items oi ON o.order_id = oi.order_id
JOIN vendors_products vp ON oi.vendor_product_id = vp.vendor_product_id
JOIN products p ON vp.product_id = p.product_id
JOIN vendors v ON vp.vendor_id = v.vendor_id
ORDER BY o.placed_at DESC, o.order_id, oi.order_item_id;
