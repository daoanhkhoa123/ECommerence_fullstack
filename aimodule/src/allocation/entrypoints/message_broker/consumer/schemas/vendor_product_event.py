from decimal import Decimal
from typing import Optional

from pydantic import BaseModel, Field


class VendorProductDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    vendor_product_id: int = Field(..., alias="vendorProductId")

    class Config:
        allow_population_by_field_name = True


class VendorProductReadEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    vendor_id: int = Field(..., alias="vendorId")

    class Config:
        allow_population_by_field_name = True


class VendorProductCreateUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    name: str
    description: Optional[str] = None
    brand: Optional[str] = None

    price: Decimal
    stock: int
    sku: Optional[str] = None
    is_featured: bool = Field(..., alias="isFeatured")

    class Config:
        allow_population_by_field_name = True
