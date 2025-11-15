from abc import ABC, abstractmethod
from typing import List, Optional

from src.langgraph_module.domain.entities.chat_message import ChatHistory


class AbstractChatHistoryRepository(ABC):
    @abstractmethod
    def add(self, history: ChatHistory) -> ChatHistory: ...
    @abstractmethod
    def get(self, history_id: int) -> Optional[ChatHistory]: ...
    @abstractmethod
    def list_by_session(self, session_id: int) -> List[ChatHistory]: ...
