import uvicorn

from src.configs.logging import setup_logging 
from src.configs.settings import AppSettings
from src.allocation.adapters.persistence import models
from src.allocation.adapters.persistence.database import Base, engine
from src.langgraph_module.adapters.persistence import models
import importlib, pathlib
import logging

setting = AppSettings() # type: ignore
setup_logging(debug=setting.debug)

logger = logging.getLogger(__name__)
def setup_database():
    logger.info("Scanning and importing all model files...")
    imported = 0

    for py_file in pathlib.Path("src").rglob("models/*.py"):
        if py_file.name == "__init__.py":
            continue

        module_path = str(py_file.with_suffix("")).replace("/", ".").replace("\\", ".")
        try:
            importlib.import_module(module_path)
            imported += 1
            logger.debug("Imported model: %s", module_path)
        except Exception as e:
            logger.error("Failed to import %s: %s", module_path, e)

    logger.info("Imported %d model files — creating tables...", imported)
    Base.metadata.create_all(bind=engine)
    logger.info("Database ready — all tables exist!")

setup_database()
if __name__ == "__main__":
    uvicorn.run("src.startup:app",
            host="127.0.0.1",
            port=8000,
            reload=setting.debug
            )