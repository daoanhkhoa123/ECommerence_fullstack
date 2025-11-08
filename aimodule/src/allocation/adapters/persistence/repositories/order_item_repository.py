from typing import Optional, List
from sqlalchemy.orm import Session
from src.allocation.adapters.persistence.models.order_item_model import OrderItemModel


class OrderItemRepository:
    def __init__(self, session: Session):
        self.session = session

    def get(self, order_item_id: int) -> Optional[OrderItemModel]:
        return self.session.query(OrderItemModel).filter_by(id=order_item_id).first()

    def add(self, order_item: OrderItemModel):
        self.session.add(order_item)

    def delete(self, order_item: OrderItemModel):
        self.session.delete(order_item)

    def list_by_order_id(self, order_id: int) -> List[OrderItemModel]:
        return self.session.query(OrderItemModel).filter_by(order_id=order_id).all()
