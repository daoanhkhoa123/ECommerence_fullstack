from psycopg2.extras import RealDictCursor
from . import get_connection         


def func(a, *b):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute("""
                        SELECT * FROM products
                    """, b)
            
            rows = cur.fetchall()
            # print(cur.description)
            print(rows[:2])
            # print(cur.description)
            # print([d[0] for d in cur.description])                


func(69)