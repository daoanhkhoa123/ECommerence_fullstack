from sqlalchemy import Column, ForeignKey, Integer, Table

from src.allocation.adapters.persistence.database import Base

products_categories = Table(
    "product_categories",
    Base.metadata,
    Column("product_id", Integer, ForeignKey("products.product_id"), primary_key=True),
    Column("category_id", Integer, ForeignKey("categories.id"), primary_key=True),
)
