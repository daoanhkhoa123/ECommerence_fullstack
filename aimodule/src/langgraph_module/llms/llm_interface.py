from abc import ABC, abstractmethod
from typing import Callable, TypeVar
from src.langgraph_module.domain.entities.chat_message import ChatMessage

class LLMInterface(ABC):

    @abstractmethod
    def __call__(self, message: str) -> str:
        """Accept a list of ChatMessage entities and return a generated string"""
        pass

    @abstractmethod
    def chat_by_message(self, message:ChatMessage) -> ChatMessage:
        pass

    def deco_func(self, func: Callable[..., str]) -> Callable[..., str]:

        def wrapper(*args, **kwargs):
            kwargs["llm"] = self
            return func(*args, **kwargs)
        
        return wrapper
    