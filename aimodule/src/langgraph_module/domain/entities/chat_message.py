from dataclasses import dataclass
from datetime import datetime
from typing import Literal, Optional

from src.allocation.domain.entities.account import Account

CHAT_ROLE = Literal["USER", "SYSTEM"]

@dataclass
class ChatMessage:
    content: str
    role: CHAT_ROLE
    created_at: Optional[datetime] = None
