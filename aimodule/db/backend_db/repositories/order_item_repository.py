from typing import Any, Dict, Sequence, Tuple
from db.backend_db import Repository
from db.backend_db.entities import OrdersItemsColumns  # assume you define these columns

class OrderItemRepository(Repository):
    def __init__(self):
        super().__init__(table_name="orders_items")

    def find_all(self) -> Sequence[Tuple]:
        return super().find_all()

    def find_fields(self, *fields: OrdersItemsColumns) -> Sequence[Tuple]:
        return super().find_fields(*fields)

    def find_by_order(self, order_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"order_id": order_id})

    def find_by_vendor_product(self, vendor_product_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"vendor_product_id": vendor_product_id})

    def find_active_items(self) -> Sequence[Tuple]:
        return super().find_by_filters({"is_active": True})  # if you track active status

    def find_fields_by_filters(
        self, 
        fields: Sequence[OrdersItemsColumns], 
        filters: Dict[OrdersItemsColumns, Any]
    ) -> Sequence[Tuple]:
        return super().find_fields_by_filters(fields, filters)
