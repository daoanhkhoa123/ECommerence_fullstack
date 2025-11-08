import datetime
from dataclasses import dataclass
from typing import Optional

from src.allocation.domain.entities.account import Account


@dataclass
class Customer:
    id: Optional[int] = None
    account: Account = Account()

    full_name: str = ''
    phone: Optional[str] = ""
    address: Optional[str] = ""
    birth_date : Optional[datetime.date] = None



