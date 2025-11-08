from pydantic import BaseModel
from typing import Optional

class CategoryCreateUpdateEvent(BaseModel):
    actor_id: int
    category_id: int
    name: str
    description: Optional[str] = None


class CategoryReadDeleteEvent(BaseModel):
    actor_id: int
    category_id: int
