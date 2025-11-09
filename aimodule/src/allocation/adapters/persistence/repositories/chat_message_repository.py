# adapters/persistence/repositories/chat_repository.py
from sqlalchemy.orm import Session

from src.allocation.adapters.persistence.models.chat_message_model import ChatMessageModel
from src.allocation.domain.entities.chat_message import ChatMessage


class SqlAlchemyChatMessageRepository:
    def __init__(self, session: Session):
        self.session = session

    def add_message(self, account_id: str, message: ChatMessage):
        model = ChatMessageModel(
            session_id=account_id,
            role=message.role,
            content=message.content,
            created_at=message.created_at
        )
        self.session.add(model)
        self.session.commit()
 