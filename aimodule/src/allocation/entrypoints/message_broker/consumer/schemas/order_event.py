from pydantic import BaseModel

class OrderStatusUpdateEvent(BaseModel):
    actor_id: int
    order_id: int
    order_status: str
