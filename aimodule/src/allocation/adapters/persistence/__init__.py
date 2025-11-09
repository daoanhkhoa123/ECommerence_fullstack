from src.allocation.adapters.persistence.database import Base, engine
from src.allocation.adapters.persistence import models

_has_created = getattr(Base, "_tables_created", False)
if not _has_created:
    print("[SQLAlchemy] Creating all tables (if not exist)...")
    Base.metadata.create_all(bind=engine)
    Base._tables_created = True
