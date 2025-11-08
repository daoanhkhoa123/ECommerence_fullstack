from sqlalchemy import Column, Integer, Numeric, ForeignKey, CheckConstraint
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.base import Base

class OrderItemModel(Base):
    __tablename__ = "orders_items"

    id = Column(Integer, primary_key=True, autoincrement=True)
    order_id = Column(Integer, ForeignKey("orders.id"), nullable=False)
    product_id = Column(Integer, ForeignKey("products.vendor_product_id"), nullable=False)

    quantity = Column(Integer, nullable=False)
    subtotal = Column(Numeric(12, 2), nullable=False, default=0.00)

    __table_args__ = (
        CheckConstraint("quantity > 0", name="check_quantity_positive"),
    )