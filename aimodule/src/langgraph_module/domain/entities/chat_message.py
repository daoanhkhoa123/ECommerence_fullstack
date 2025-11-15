from dataclasses import dataclass
from datetime import datetime
from typing import Literal, Optional

CHAT_ROLE = Literal["USER", "SYSTEM"]

@dataclass
class ChatMessage:
    content: str
    role: CHAT_ROLE
    created_at: Optional[datetime] = None
