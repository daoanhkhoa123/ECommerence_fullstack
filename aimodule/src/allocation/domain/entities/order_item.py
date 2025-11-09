from dataclasses import dataclass
from decimal import Decimal
from typing import Optional


@dataclass
class OrderItem:
    id: Optional[int] = None
    order_id: int = -1
    product_id: int = -1
    quantity: int = -1
    price: Decimal = Decimal("-1")   

    @property
    def sub_total(self) -> Decimal:
        return self.price * self.quantity
