from pydantic import BaseModel
from decimal import Decimal
from typing import Optional

class VendorProductDeleteEvent(BaseModel):
    actor_id: int
    vendor_product_id: int

class VendorProductReadEvent(BaseModel):
    actor_id: int
    vendor_id: int

class VendorProductCreateUpdateEvent(BaseModel):
    actor_id: int
    name: str
    description: Optional[str]
    brand: Optional[str]

    price: Decimal
    stock: int
    sku: Optional[str]
    is_featured: bool
