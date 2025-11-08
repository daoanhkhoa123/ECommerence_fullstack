from pydantic import BaseModel

class VendorCreateUpdateEvent(BaseModel):
    actor_id: int
    vendor_id: int
    email: str
    shop_name: str
    description: str
    phone: str

class VendorReadDeleteEvent(BaseModel):
    actor_id: int
    vendor_id: int
