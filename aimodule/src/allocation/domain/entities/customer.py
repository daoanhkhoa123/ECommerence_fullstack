from dataclasses import dataclass, field
from typing import Optional
import datetime
from src.allocation.domain.entities.account import Account

@dataclass
class Customer:
    id: Optional[int] = None
    account: Account = field(default_factory=Account)

    full_name: str = ''
    phone: Optional[str] = ""
    address: Optional[str] = ""
    birth_date: Optional[datetime.date] = None
