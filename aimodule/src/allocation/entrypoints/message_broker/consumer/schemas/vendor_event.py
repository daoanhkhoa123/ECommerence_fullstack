from pydantic import BaseModel, Field


class VendorCreateUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    vendor_id: int = Field(..., alias="vendorId")
    email: str
    shop_name: str = Field(..., alias="shopName")
    description: str
    phone: str

    class Config:
        allow_population_by_field_name = True


class VendorReadDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    vendor_id: int = Field(..., alias="vendorId")

    class Config:
        allow_population_by_field_name = True
