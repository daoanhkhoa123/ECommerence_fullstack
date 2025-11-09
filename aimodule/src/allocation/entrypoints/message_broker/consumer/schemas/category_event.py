from typing import Optional

from pydantic import BaseModel


class CategoryCreateUpdateEvent(BaseModel):
    actor_id: int
    category_id: int
    name: str
    description: Optional[str] = None


class CategoryReadDeleteEvent(BaseModel):
    actor_id: int
    category_id: int
