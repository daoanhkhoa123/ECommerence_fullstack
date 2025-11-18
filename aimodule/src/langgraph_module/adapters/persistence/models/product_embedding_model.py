from typing import Optional

from pgvector.sqlalchemy import Vector
from sqlalchemy import ForeignKey, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base
from src.configs.settings import DatabaseSettings

db_setting = DatabaseSettings() # type: ignore

class ProductEmbeddingModel(Base):
    __tablename__= "product_embeddings"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)

    product_id: Mapped[int] = mapped_column(Integer, 
                                            ForeignKey("products.product_id", ondelete="CASCADE"),
                                            nullable=False)
    
    embedding: Mapped[list] = mapped_column(Vector(db_setting.database_embedding_dim))
    embedding_model: Mapped[str] = mapped_column(String(200), nullable=True)
    embedding_dim: Mapped[Optional[int]] = mapped_column(Integer, nullable=True)

    product = relationship("ProductModel")