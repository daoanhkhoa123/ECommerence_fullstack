from dataclasses import dataclass
from typing import Optional, Literal

CHAT_ROLE = Literal["USER", "SYSTEM"]

@dataclass
class ChatHistory:
    id: Optional[int]
    session_id: int
    message: str
    role: CHAT_ROLE
