from typing import Any, Dict, Sequence, Tuple
from db.backend_db import Repository
from db.backend_db.entities import OrdersColumns  # assume you define these columns as an Enum or TypedDict

class OrderRepository(Repository):
    def __init__(self):
        super().__init__(table_name="orders")

    def find_all(self) -> Sequence[Tuple]:
        return super().find_all()

    def find_fields(self, *fields: OrdersColumns) -> Sequence[Tuple]:
        return super().find_fields(*fields)

    def find_by_customer(self, customer_id: int) -> Sequence[Tuple]:
        return super().find_by_filters()

    def find_by_status(self, order_status: str) -> Sequence[Tuple]:
        return super().find_by_filters({"order_status": order_status})

    def find_fields_by_filters(
        self, 
        fields: Sequence[OrdersColumns], 
        filters: Dict[OrdersColumns, Any]
    ) -> Sequence[Tuple]:
        return super().find_fields_by_filters(fields, filters)
