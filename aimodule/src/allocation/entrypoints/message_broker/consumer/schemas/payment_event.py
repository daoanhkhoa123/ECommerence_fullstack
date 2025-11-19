from datetime import datetime
from decimal import Decimal

from pydantic import BaseModel, Field


class PaymentCreateEvent(BaseModel):
    payment_id: int = Field(..., alias="paymentId")
    order_id: int = Field(..., alias="orderId")
    actor_id: int = Field(..., alias="actorId")

    payment_method: str = Field(..., alias="paymentMethod")
    payment_status: str = Field(..., alias="paymentStatus")
    paid_amount: Decimal = Field(..., alias="paidAmount")
    transaction_ref: str = Field(..., alias="transactionRef")
    paid_at: datetime = Field(..., alias="paidAt")

    class Config:
        allow_population_by_field_name = True