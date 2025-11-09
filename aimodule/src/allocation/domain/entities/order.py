import datetime
from dataclasses import dataclass, field
from decimal import Decimal
from typing import List, Optional

from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.order_item import OrderItem
from src.allocation.domain.entities.product import Product


@dataclass
class Order:
    id: Optional[int] = None
    customer: Customer = field(default_factory=Customer)
    order_status: str = ""
    total_amount: Decimal = Decimal("0.00")
    shipping_address: str = ""
    order_time: datetime.datetime = field(default_factory=datetime.datetime.now)
    order_items: List[OrderItem] = field(default_factory=list)
    products: List[Product] = field(default_factory=list)

    def calculate_total(self) -> None:
        if self.order_items:
            self.total_amount = sum((item.sub_total for item in self.order_items), Decimal("0.00"))
        else:
            self.total_amount = Decimal("0.00")
