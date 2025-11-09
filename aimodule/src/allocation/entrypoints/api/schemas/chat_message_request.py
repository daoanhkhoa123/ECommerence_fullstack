from pydantic import BaseModel


class ChatMessageRequest(BaseModel):
    account_id: int
    message: str
