from typing import List

from pydantic import BaseModel, Field


class ProductCategoryCreateDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    product_id: int = Field(..., alias="productId")
    category_ids: List[int] = Field(..., alias="categoryIds")

    class Config:
        allow_population_by_field_name = True

