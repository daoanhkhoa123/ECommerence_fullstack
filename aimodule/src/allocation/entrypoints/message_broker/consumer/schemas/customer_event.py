from pydantic import BaseModel, EmailStr
from datetime import date
from typing import Optional

class CustomerCreateUpdateEvent(BaseModel):
    actor_id: int
    customer_id: int
    email: EmailStr
    full_name: str
    phone: Optional[str] = None
    address: Optional[str] = None
    birth_date: Optional[date] = None


class CustomerReadDeleteEvent(BaseModel):
    actor_id: int
    customer_id: int
