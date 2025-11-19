from datetime import datetime
from typing import List, Literal, Optional, Union

from pydantic import BaseModel, Field, field_validator

from src.langgraph_module.domain.entities.chat_message import CHAT_ROLE


class ChatMessageEvent(BaseModel):
    account_id: int = Field(..., alias="accountId")
    message: str
    role: CHAT_ROLE
    timestamp: Optional[datetime] = Field(None, alias="timestamp")

    @field_validator("timestamp", mode="before")
    def parse_timestamp(cls, v: Union[List[int], str, None]) -> Optional[datetime]:
        if isinstance(v, list) and len(v) >= 6:
            microsec = int(v[6] / 1000) if len(v) > 6 else 0
            return datetime(v[0], v[1], v[2], v[3], v[4], v[5], microsec)
        return v # type: ignore

    model_config = {
        "populate_by_name": True,
        "extra": "ignore"
    }
