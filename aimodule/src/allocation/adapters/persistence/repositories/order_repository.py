from typing import Optional

from sqlalchemy.orm import Session

from src.allocation.adapters.persistence.models.order_model import OrderModel
from src.allocation.domain.entities.order import Order


class SqlAlchemyOrderRepository:
    def __init__(self, session: Session):
        self.session = session

    def get(self, order_id: int) -> Optional[OrderModel]:
        return self.session.query(OrderModel).filter_by(id=order_id).first()

    def find_first_by_customer_and_status(self, customer_id: int, status: str) -> Optional[OrderModel]:
        return (
            self.session.query(OrderModel)
            .filter_by(customer_id=customer_id, order_status=status)
            .first()
        )

    def add(self, order: OrderModel):
        self.session.add(order)

    def delete(self, order: OrderModel):
        self.session.delete(order)
