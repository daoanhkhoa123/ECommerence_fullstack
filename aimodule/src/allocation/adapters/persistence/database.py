from sqlalchemy import create_engine
from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from sqlalchemy.orm import DeclarativeBase, scoped_session, sessionmaker

from src.configs.settings import DatabaseSettings

# Load environment variables
settings = DatabaseSettings() # type: ignore

# Create engine
engine = create_engine(
    settings.database_url,
    echo=True,               # Set True for SQL logs
    pool_pre_ping=True,       # Ensures dead connections are recycled
    future=True
)

# async_engine = create_async_engine(
#     settings.database_url,
#     echo=True,
#     future=True
# )

# Scoped session for thread safety (good for web apps, workers, etc.)
SessionLocal = scoped_session(
    sessionmaker(
        autocommit=False,
        autoflush=False,
        bind=engine,
        expire_on_commit=False
    )
)


class Base(DeclarativeBase):
    pass