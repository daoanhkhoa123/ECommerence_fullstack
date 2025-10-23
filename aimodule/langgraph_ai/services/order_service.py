from typing import Sequence, Dict
from db.backend_db.entities import OrdersColumns, OrdersItemsColumns
from db.backend_db.repositories.order_repository import OrderRepository
from db.backend_db.repositories.order_item_repository import OrderItemRepository
from db.backend_db.repositories.product_repository import ProductRepository
from db.backend_db.repositories.vendor_repository import VendorRepository
from db.backend_db.repositories.product_vendor_repository import VendorProductRepository

class OrderService:
    def __init__(self) -> None:
        self.order_repository = OrderRepository()
        self.order_item_repository = OrderItemRepository()
        self.product_repository = ProductRepository()
        self.vendor_repository = VendorRepository()
        self.vendor_product_repository = VendorProductRepository()

    def find_orders_by_customer(self, customer_id: int) -> Sequence[Dict]:
        fields = "order_id", "customer_id", "order_status", "total_amount", "shipping_address", "placed_at"
        filter = {"customer_id": customer_id}
        rows = self.order_repository.find_fields_by_filters(fields, filter) # type: ignore
        return self.order_repository.to_dict(rows, fields)

    def find_order_items_by_order(self, order_id: int) -> Sequence[Dict]:
        fields = "order_item_id", "order_id", "vendor_product_id", "quantity", "price", "subtotal"
        rows = self.order_item_repository.find_by_order(order_id)
        return self.order_item_repository.to_dict(rows, fields)

    def find_order_items_with_product_vendor(self, order_id: int) -> Sequence[Dict]:
        find_fields = (
            "oi.order_item_id", "oi.quantity", "oi.price", "oi.subtotal",
            "p.product_id", "p.name", "p.description", "p.brand", "p.category",
            "vp.price as vendor_price", "v.vendor_id", "v.shop_name", "v.description"
        )
        query = f"""
        SELECT {', '.join(find_fields)}
        FROM {self.order_item_repository.table_name} AS oi
        JOIN {self.vendor_product_repository.table_name} AS vp
          ON oi.vendor_product_id = vp.vendor_product_id
        JOIN {self.product_repository.table_name} AS p
          ON vp.product_id = p.product_id
        JOIN {self.vendor_repository.table_name} AS v
          ON vp.vendor_id = v.vendor_id
        WHERE oi.order_id = %s
        """
        rows = self.order_repository._execute_query(query, order_id) # type: ignore
        return self.order_repository.to_dict(rows, find_fields)

