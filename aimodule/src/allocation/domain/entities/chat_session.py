from dataclasses import dataclass
from typing import Optional

from src.allocation.domain.entities.account import Account  # reuse your Account entity

@dataclass
class ChatSession:
    id: Optional[int]
    account: Account
    session_name: str
