from dataclasses import dataclass, field
from typing import Optional

from src.allocation.domain.entities.account import Account


@dataclass
class Vendor:
    id: Optional[int] = None
    account: Account = field(default_factory=Account)

    shop_name: str = ""
    description: Optional[str] = None
    phone: Optional[str] = None
