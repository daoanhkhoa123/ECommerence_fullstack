from config.settings import DatabaseSettings
from db.utils import init_connection_pool

backend_db_url = DatabaseSettings().backend_db_url

init_connection_pool(backend_db_url) # type: ignore