from sqlalchemy import (Boolean, Column, ForeignKey, Integer, Numeric, String,
                        Text)
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.database import Base
from src.allocation.adapters.persistence.models.products_categories_model import \
    products_categories


class ProductModel(Base):
    __tablename__ = "products"

    product_id = Column(Integer, primary_key=True, autoincrement=True)
    vendor_product_id = Column(Integer, nullable=False, unique=True)
    vendor_id = Column(Integer, ForeignKey("vendors.id"), nullable=False)

    name = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)
    brand = Column(String(100), nullable=True)
    image_url = Column(String(255), nullable=True)

    price = Column(Numeric(10, 2), default=0.00)
    stock = Column(Integer, nullable=True)
    sku = Column(String(100), nullable=True)
    is_featured = Column(Boolean, default=False)

    # relationships
    vendor = relationship("VendorModel", back_populates="products")
    orders_items = relationship("OrderItemModel", back_populates="product")

    categories = relationship(
        "CategoryModel",
        secondary=products_categories,
        back_populates="products"
    )
