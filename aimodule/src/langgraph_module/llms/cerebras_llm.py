from typing import Literal

from langchain_openai import ChatOpenAI

from src.configs.keys import LLMApiKeys
from src.configs.settings import LLMSettings
from src.langgraph_module.domain.entities.chat_message import ChatMessage
from src.langgraph_module.llms.decorator import llm_factory
from src.langgraph_module.llms.llm_interface import LLMInterface

_BASE_URL = r"https://api.cerebras.ai/v1"
CEREBRAS_NAMES = Literal[
    "oss-120b",
    "llama-3.3-70b",
    "llama3.1-8b",
    "qwen-3-235b-a22b-instruct-2507",
    "qwen-3-235b-a22b-thinking-2507",
    "qwen-3-32b",
    "zai-glm-4.6",
]

key_setting = LLMApiKeys() # type: ignore

@llm_factory
class CerebrasLLM(LLMInterface):
    def __init__(self, model_name:CEREBRAS_NAMES, temperature: float = 0.8, **model_kwargs) -> None:
        self._client = ChatOpenAI(
            model=model_name,
            api_key=key_setting.cerebras_api_key,  # type: ignore
            base_url=_BASE_URL,
            temperature=temperature,
            model_kwargs = model_kwargs # type: ignore
        )

    def __call__(self, message: str) -> str:
        response = self._client.invoke(message)
        return response.content if hasattr(response, "content") else str(response) # type: ignore
    
    def chat_by_message(self, message: ChatMessage) -> ChatMessage:
        """Wrap ChatMessage input/output."""
        text = self(message.content)
        return ChatMessage(text, "SYSTEM")