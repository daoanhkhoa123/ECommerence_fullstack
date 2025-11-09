from typing import List

from pydantic import BaseModel


class ProductCategoryCreateDeleteEvent(BaseModel):
    actor_id: int
    product_id: int
    category_ids: List[int]
