from typing import List
from config.settings import DatabaseSettings
from psycopg2.extras import RealDictCursor, RealDictRow
from db.utils import get_connection

backend_db_url = DatabaseSettings().backend_db_url

def get_orders_by_customerid(customer_id:int) -> List[RealDictRow]:
    with get_connection(backend_db_url) as conn: # type: ignore
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute(f"""
            SELECT 
                p.name AS product_name,
                v.shop_name AS shop_name,
                oi.quantity,
                oi.price AS price,
                oi.subtotal AS total_price
            FROM orders o 
            JOIN customers c ON o.customer_id = c.customer_id
            JOIN orders_items oi ON o.order_id = oi.order_id
            JOIN vendors_products vp ON oi.vendor_product_id = vp.vendor_product_id
            JOIN products p ON vp.product_id = p.product_id
            JOIN vendors v ON vp.vendor_id = v.vendor_id
            WHERE c.customer_id = %s
            ORDER BY o.placed_at DESC, o.order_id, oi.order_item_id;
            """, (customer_id, ))
            rows = cur.fetchall()
    
    return rows




# if __name__ == "__main__":
#     print(get_orders_by_customerid(2))