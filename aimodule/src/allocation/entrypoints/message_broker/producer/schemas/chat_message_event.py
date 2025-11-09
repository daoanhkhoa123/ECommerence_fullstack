from datetime import datetime
from typing import Literal, Optional

from pydantic import BaseModel
from src.allocation.domain.entities.chat_message import CHAT_ROLE


class ChatMessageEvent(BaseModel):
    account_id: int
    message: str
    role: CHAT_ROLE
    timestamp: Optional[datetime] = None
