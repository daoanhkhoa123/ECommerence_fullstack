from decimal import Decimal
from typing import Optional

from pydantic import BaseModel


class OrderItemEvent(BaseModel):
    account_id: int
    customer_id: int
    order_id: int


class OrderItemCreateEvent(BaseModel):
    actor_id: int

    order_id: int
    order_item_id: int
    vendor_product_id: int

    # Order Item
    quantity: int
    sub_total: Decimal

    # Vendor
    shop_name: str
    shop_phone: str

    # Product
    product_name: str
    product_brand: str


class OrderItemDeleteEvent(BaseModel):
    actor_id: int
    order_item_id: int
