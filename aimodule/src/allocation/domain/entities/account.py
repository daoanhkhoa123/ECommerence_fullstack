from dataclasses import dataclass
from typing import Literal, Optional

ACCOUNT_ROLE = Literal["CUSTOMER", "VENDOR", ""]

@dataclass
class Account:
    id: Optional[int] = None
    role: ACCOUNT_ROLE = ""


