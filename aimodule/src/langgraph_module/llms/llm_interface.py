from abc import ABC, abstractmethod
from typing import List

from src.langgraph_module.domain.entities.chat_message import ChatMessage


class LLMInterface(ABC):

    @abstractmethod
    def __call__(self, message: str) -> str:
        """Accept a list of ChatMessage entities and return a generated string"""
        pass

    @abstractmethod
    def chat_by_message(self, message:ChatMessage) -> ChatMessage:
        pass

class EmbeddingInterface(ABC):
    @property
    @abstractmethod
    def model(str) -> str:
        pass

    @abstractmethod
    def __call__(self, text:str) -> List[float]:
        pass