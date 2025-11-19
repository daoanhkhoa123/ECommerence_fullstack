from datetime import datetime

from sqlalchemy import DateTime, ForeignKey, Integer, Numeric, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base


class OrderModel(Base):
    __tablename__ = "orders"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    customer_id: Mapped[int] = mapped_column(ForeignKey("customers.id"), nullable=False)

    order_status: Mapped[str] = mapped_column(String(50), nullable=False)
    total_amount: Mapped[float] = mapped_column(Numeric(10, 2), default=0.00)
    shipping_address: Mapped[str | None] = mapped_column(String(255), nullable=True)
    order_time: Mapped[datetime] = mapped_column(DateTime, nullable=False)

    customer: Mapped["CustomerModel"] = relationship(back_populates="orders") # type: ignore
    orders_items: Mapped[list["OrderItemModel"]] = relationship(back_populates="order") # type: ignore
