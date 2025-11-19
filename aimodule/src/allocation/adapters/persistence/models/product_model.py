from sqlalchemy import Boolean, ForeignKey, Integer, Numeric, String, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base
from src.allocation.adapters.persistence.models.products_categories_model import \
    products_categories


class ProductModel(Base):
    __tablename__ = "products"

    product_id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    vendor_product_id: Mapped[int] = mapped_column(Integer, nullable=False, unique=True)
    vendor_id: Mapped[int] = mapped_column(ForeignKey("vendors.id"), nullable=False)

    name: Mapped[str] = mapped_column(String(255), nullable=False)
    description: Mapped[str | None] = mapped_column(Text, nullable=True)
    brand: Mapped[str | None] = mapped_column(String(100), nullable=True)
    image_url: Mapped[str | None] = mapped_column(String(255), nullable=True)

    price: Mapped[float] = mapped_column(Numeric(10, 2), default=0.00)
    stock: Mapped[int | None] = mapped_column(Integer, nullable=True)
    sku: Mapped[str | None] = mapped_column(String(100), nullable=True)
    is_featured: Mapped[bool] = mapped_column(Boolean, default=False)

    vendor: Mapped["VendorModel"] = relationship(back_populates="products") # type: ignore
    orders_items: Mapped[list["OrderItemModel"]] = relationship(back_populates="product") # type: ignore
    categories: Mapped[list["CategoryModel"]] = relationship( # type: ignore
        secondary=products_categories,
        back_populates="products"
    )
