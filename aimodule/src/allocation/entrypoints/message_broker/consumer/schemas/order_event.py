from pydantic import BaseModel, Field

class OrderStatusUpdateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    order_id: int = Field(..., alias="orderId")
    order_status: str = Field(..., alias="orderStatus")

    class Config:
        allow_population_by_field_name = True
