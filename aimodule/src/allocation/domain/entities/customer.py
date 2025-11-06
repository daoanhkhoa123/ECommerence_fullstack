import datetime
from dataclasses import dataclass
from typing import Optional

from src.allocation.domain.entities.account import Account


@dataclass
class Customer:
    id: int
    account: Account

    full_name: str
    phone: Optional[str]
    address: Optional[str]
    birth_date : Optional[datetime.date]



