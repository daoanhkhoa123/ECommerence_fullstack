import logging
from src.allocation.adapters.persistence.database import Base, engine
from src.allocation.adapters.persistence.models import load_all_models  # noqa: F401 (ensures models are imported)

logger = logging.getLogger(__name__)

load_all_models()
_has_created = getattr(Base, "_tables_created", False)
if not _has_created:
    logger.info("Creating all tables (if not exist)...")
    Base.metadata.create_all(bind=engine)
    Base._tables_created = True
