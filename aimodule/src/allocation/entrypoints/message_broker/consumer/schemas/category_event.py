from typing import Optional
from pydantic import BaseModel, Field

class CategoryCreateUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    category_id: int = Field(..., alias="categoryId")
    name: str
    description: Optional[str] = None

    class Config:
        allow_population_by_field_name = True


class CategoryReadDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    category_id: int = Field(..., alias="categoryId")

    class Config:
        allow_population_by_field_name = True
