from abc import ABC, abstractmethod

from src.allocation.domain.entities.chat_state import ChatState


class ConversationStateRepository(ABC):
    @abstractmethod
    def get_by_user(self, user_id: int) -> ChatState | None:
        ...

    @abstractmethod
    def add_or_update(self, state: ChatState):
        ...
