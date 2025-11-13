from src.configs.settings import LLMSettings
from google import genai as _genai
from src.allocation.domain.entities.chat_message import ChatMessage
from src.langgraph_module.llms.llm_interface import LLMInterface


class GoogleLLM(LLMInterface):

    def __init__(self):
        self.settings = LLMSettings() # type: ignore
        self._client = _genai.Client(api_key=self.settings.google_api_key)

    def _generate_text(self, prompt: str) -> str:
        response = self._client.models.generate_content(
            model=self.settings.google_llm,  # type: ignore
            contents=prompt,
        )
        return response.text # type: ignore

    def __call__(self, message: str) -> str:
        return self._generate_text(message)

    def chat_by_message(self, message: ChatMessage) -> ChatMessage:
        text = self(str(message))
        return ChatMessage(text, "SYSTEM")