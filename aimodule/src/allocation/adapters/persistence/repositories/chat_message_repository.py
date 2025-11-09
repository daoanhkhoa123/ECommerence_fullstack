from typing import List
from sqlalchemy.orm import Session

from src.allocation.adapters.persistence.models.chat_message_model import ChatMessageModel
from src.allocation.domain.entities.chat_message import ChatMessage


class SqlAlchemyChatMessageRepository:
    def __init__(self, session: Session):
        self.session = session

    def add_message(self, account_id: int, message: ChatMessage) -> None:
        """Persist a new chat message for a given account."""
        model = ChatMessageModel(
            account_id=account_id,
            role=message.role,
            content=message.content,
            created_at=message.created_at,
        )
        self.session.add(model)
        self.session.commit()

    def get_by_user_id(self, account_id: int) -> List[ChatMessage]:
        """Return all chat messages for a given account, ordered by time."""
        records = (
            self.session.query(ChatMessageModel)
            .filter(ChatMessageModel.account_id == account_id)
            .order_by(ChatMessageModel.created_at.asc())
            .all()
        )

        # Convert ORM models → domain entities
        return [
            ChatMessage(
                role=record.role, # type: ignore
                content=record.content, # type: ignore
                created_at=record.created_at, # type: ignore
            )
            for record in records
        ]
