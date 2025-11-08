# adapters/persistence/repositories/chat_repository.py
from domain.entities.chat_message import ChatMessage
from adapters.persistence.models.chat_message_model import ChatMessageModel
from sqlalchemy.orm import Session

class ChatRepository:
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
 