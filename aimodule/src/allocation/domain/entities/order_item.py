from dataclasses import dataclass
from decimal import Decimal
from typing import Optional

@dataclass
class OrderItem:
    id: Optional[int]
    order_id: int
    product_id: int
    quantity: int
    price: Decimal

    @property
    def sub_total(self) -> Decimal:
        return self.price * self.quantity
