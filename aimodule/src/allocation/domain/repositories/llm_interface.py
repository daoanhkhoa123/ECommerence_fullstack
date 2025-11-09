from abc import ABC, abstractmethod
from typing import List

from src.allocation.domain.entities.chat_message import ChatMessage


class LLMInterface(ABC):

    @abstractmethod
    def generate_text(self, prompt: str) -> str:
        pass

    @abstractmethod
    def __call__(self, messages: List[ChatMessage]) -> str:
        """Accept a list of ChatMessage entities and return a generated string"""
        pass
