from sqlalchemy import CheckConstraint, ForeignKey, Integer, Numeric
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base


class OrderItemModel(Base):
    __tablename__ = "order_items"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    order_id: Mapped[int] = mapped_column(ForeignKey("orders.id"), nullable=False)
    product_id: Mapped[int] = mapped_column(ForeignKey("products.product_id"), nullable=False)

    quantity: Mapped[int] = mapped_column(Integer, nullable=False)
    subtotal: Mapped[float] = mapped_column(Numeric(12, 2), nullable=False, default=0.00)

    __table_args__ = (
        CheckConstraint("quantity > 0", name="check_quantity_positive"),
    )

    order: Mapped["OrderModel"] = relationship(back_populates="orders_items") # type: ignore
    product: Mapped["ProductModel"] = relationship(back_populates="orders_items") # type: ignore
