from abc import ABC, abstractmethod
from typing import List

from src.langgraph_module.domain.entities.chat_message import ChatMessage


class ChatMessageRepository(ABC):
    @abstractmethod
    def add(self, message: ChatMessage):
        ...

    @abstractmethod
    def list_by_user(self, user_id: int) -> List[ChatMessage]:
        ...
