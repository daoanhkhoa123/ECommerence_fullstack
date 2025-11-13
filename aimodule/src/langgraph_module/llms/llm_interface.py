from abc import ABC, abstractmethod

from src.allocation.domain.entities.chat_message import ChatMessage


class LLMInterface(ABC):

    @abstractmethod
    def __call__(self, message: str) -> str:
        """Accept a list of ChatMessage entities and return a generated string"""
        pass

    @abstractmethod
    def chat_by_message(self, message:ChatMessage) -> ChatMessage:
        pass