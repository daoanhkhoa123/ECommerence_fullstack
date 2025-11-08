from src.allocation.domain.entities.chat_message import CHAT_ROLE

from pydantic import BaseModel
from typing import Literal, Optional
from datetime import datetime

class ChatMessageEvent(BaseModel):
    account_id: int
    message: str
    role: CHAT_ROLE
    timestamp: Optional[datetime] = None
