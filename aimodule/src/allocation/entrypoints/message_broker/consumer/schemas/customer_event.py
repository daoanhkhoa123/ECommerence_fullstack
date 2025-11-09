from datetime import date
from typing import Optional

from pydantic import BaseModel, EmailStr


class CustomerCreateUpdateEvent(BaseModel):
    actor_id: int
    customer_id: int
    email: str
    full_name: str
    phone: Optional[str] = None
    address: Optional[str] = None
    birth_date: Optional[date] = None


class CustomerReadDeleteEvent(BaseModel):
    actor_id: int
    customer_id: int
