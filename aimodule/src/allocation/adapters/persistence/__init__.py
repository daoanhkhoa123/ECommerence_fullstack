import logging
import importlib
from src.allocation.adapters.persistence.database import Base, engine

import os

logger = logging.getLogger(__name__)


def auto_register_models_and_create_tables():
    if hasattr(Base, "_tables_created"):
        logger.info("Skipped initializing tables")
        return  # already done

    caller_dir = os.path.dirname(__file__)  # ← folder of the file that imported us!

    logger.info("Auto-registering models from: %s", caller_dir)

    for filename in os.listdir(caller_dir):
        if filename.endswith(".py") and filename != "__init__.py":
            module_name = f"{__package__}.{filename[:-3]}"
            try:
                importlib.import_module(module_name)
                logger.debug("Loaded model: %s", module_name)
            except Exception as e:
                logger.warning("Failed to load %s: %s", module_name, e)

    logger.info("Creating all tables (first time only)...")
    Base.metadata.create_all(bind=engine)
    Base._tables_created = True
    logger.info("All tables created! Ready to go!")

auto_register_models_and_create_tables()