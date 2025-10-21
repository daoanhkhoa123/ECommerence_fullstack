from contextlib import contextmanager
from config.settings import DatabaseSettings
from typing import Generator, Dict
from psycopg2 import pool, extensions

_connection_pool:Dict[str, pool.SimpleConnectionPool] = dict() # type: ignore
dbsetting = DatabaseSettings()

def init_connection_pool(db_url:str):
    global _connection_pool
    if _connection_pool.get(db_url) is None:
        _connection_pool[db_url] = pool.SimpleConnectionPool(dbsetting.minconn, dbsetting.maxconn, db_url)
    else:
        raise RuntimeError(f"Connection pool for {db_url} is already initialized")
    
@contextmanager
def get_connection(db_url:str) -> Generator[extensions.connection, None,None]:
    """ Get a connection from the pool. """
    global _connection_pool
    if _connection_pool.get(db_url) is None:
        raise RuntimeError("Connection pool is not initialized. Call init_connection_pool() first.")

    conn = _connection_pool[db_url].getconn()
    try:
        yield conn
    finally:
        _connection_pool[db_url].putconn(conn)