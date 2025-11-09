from dataclasses import dataclass
from typing import Optional


@dataclass
class Account:
    id: Optional[int] = None
    role: str = ""


