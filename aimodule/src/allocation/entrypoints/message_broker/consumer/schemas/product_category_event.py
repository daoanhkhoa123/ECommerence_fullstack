from pydantic import BaseModel
from typing import List

class ProductCategoryCreateDeleteEvent(BaseModel):
    actor_id: int
    product_id: int
    category_ids: List[int]
