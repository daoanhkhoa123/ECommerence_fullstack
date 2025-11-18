from datetime import date
from typing import List, Optional, Union

from pydantic import BaseModel, Field, field_validator


class CustomerCreateUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    customer_id: int = Field(..., alias="customerId")
    email: str
    full_name: str = Field(..., alias="fullName")
    phone: Optional[str] = None
    address: Optional[str] = None
    birth_date: Optional[date] = Field(None, alias="birDate")

    @field_validator("birth_date", mode="before")
    def parse_birth_date(cls, v: Union[List[int], str, None]) -> Optional[date]:
        if isinstance(v, list) and len(v) == 3:
            return date(v[0], v[1], v[2])
        return v # type: ignore


    class Config:
        allow_population_by_field_name = True


class CustomerReadDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    customer_id: int = Field(..., alias="customerId")

    class Config:
        allow_population_by_field_name = True
