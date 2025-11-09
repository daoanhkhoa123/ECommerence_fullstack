from datetime import date
from typing import Optional

from pydantic import BaseModel, Field

class CustomerCreateUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    customer_id: int = Field(..., alias="customerId")
    email: str
    full_name: str = Field(..., alias="fullName")
    phone: Optional[str] = None
    address: Optional[str] = None
    birth_date: Optional[date] = Field(None, alias="birDate")

    class Config:
        allow_population_by_field_name = True


class CustomerReadDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    customer_id: int = Field(..., alias="customerId")

    class Config:
        allow_population_by_field_name = True
