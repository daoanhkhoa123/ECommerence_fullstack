from datetime import datetime
from decimal import Decimal

from pydantic import BaseModel


class PaymentCreateEvent(BaseModel):
    payment_id: int
    order_id: int
    actor_id: int

    # Payment info
    payment_method: str
    payment_status: str
    paid_amount: Decimal
    transaction_ref: str
    paid_at: datetime
