from sqlalchemy import Column, Integer, String, Text
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.base import Base
from src.allocation.adapters.persistence.models.products_categories_model import products_categories 

class CategoryModel(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)

    products = relationship(
        "ProductModel", 
        secondary=products_categories,
        back_populates="categories"
    )