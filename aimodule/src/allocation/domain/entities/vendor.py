from dataclasses import dataclass
from typing import Optional

from src.allocation.domain.entities.account import Account


@dataclass
class Vendor:
    id: Optional[int] = None
    account: Account = Account()

    shop_name: str = ""
    description: Optional[str] = None
    phone: Optional[str] = None
