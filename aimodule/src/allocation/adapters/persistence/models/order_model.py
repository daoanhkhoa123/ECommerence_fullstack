from sqlalchemy import Column, Integer, String, Numeric, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.base import Base

class OrderModel(Base):
    __tablename__ = "orders"

    id = Column(Integer, primary_key=True, autoincrement=True)
    customer_id = Column(Integer, ForeignKey("customers.id"), nullable=False)

    order_status = Column(String(50), nullable=False)
    total_amount = Column(Numeric(10, 2), default=0.00)
    shipping_address = Column(String(255), nullable=True)
    order_time = Column(DateTime, nullable=False)

    customer = relationship("CustomerModel", back_populates="order")
    orders_items = relationship("OrderItemModel", back_populates="order")
