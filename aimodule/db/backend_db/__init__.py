from config.settings import DatabaseSettings
from db.ultils.connection_util import init_connection_pool, get_connection
from db.ultils.repository_ultil import RepositoryBase

backend_db_url = DatabaseSettings().backend_db_url

__all__ = ["backend_db_url", "get_connection", "RepositoryBase"]

init_connection_pool(backend_db_url) # type: ignore
get_connection = lambda : get_connection(backend_db_url) # type: ignore

class Repository(RepositoryBase):
    def __init__(self, table_name) -> None:
        super().__init__(backend_db_url, table_name)