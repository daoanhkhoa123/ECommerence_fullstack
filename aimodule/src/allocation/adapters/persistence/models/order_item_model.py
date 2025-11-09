from sqlalchemy import CheckConstraint, Column, ForeignKey, Integer, Numeric
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.database import Base

class OrderItemModel(Base):
    __tablename__ = "order_items"

    id = Column(Integer, primary_key=True, autoincrement=True)
    order_id = Column(Integer, ForeignKey("orders.id"), nullable=False)
    product_id = Column(Integer, ForeignKey("products.product_id"), nullable=False)

    quantity = Column(Integer, nullable=False)
    subtotal = Column(Numeric(12, 2), nullable=False, default=0.00)

    __table_args__ = (
        CheckConstraint("quantity > 0", name="check_quantity_positive"),
    )

    # relationships
    order = relationship(
        "OrderModel",
        back_populates="orders_items"
    )
    product = relationship(
        "ProductModel",
        back_populates="orders_items"
    )
