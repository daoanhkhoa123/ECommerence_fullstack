from config.settings import DatabaseSettings
from db.ultils.connection_util import init_connection_pool
from db.ultils.repository_ultil  import RepositoryBase

aimodule_db_url = DatabaseSettings().aimodule_db_url

__all__ = ["aimodule_db_url", "get_connection", "RepositoryBase"]

init_connection_pool(aimodule_db_url) # type: ignore
get_connection =  lambda:get_connection(aimodule_db_url) # type: ignore

class Repository(RepositoryBase):
    def __init__(self, table_name) -> None:
        super().__init__(aimodule_db_url, table_name)