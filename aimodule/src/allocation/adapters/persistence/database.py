from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from src.allocation.configs.settings import DatabaseSettings

# Load environment variables
settings = DatabaseSettings() # type: ignore

# Create engine
engine = create_engine(
    settings.database_url,
    echo=False,               # Set True for SQL logs
    pool_pre_ping=True,       # Ensures dead connections are recycled
    future=True
)

# Scoped session for thread safety (good for web apps, workers, etc.)
SessionLocal = scoped_session(
    sessionmaker(
        autocommit=False,
        autoflush=False,
        bind=engine,
        expire_on_commit=False
    )
)

# Dependency factory
def get_session():
    """Factory function to get a new SQLAlchemy session."""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
