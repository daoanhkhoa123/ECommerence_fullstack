from typing import List

from config.settings import LLMSettings
from google import genai as _genai
from src.allocation.domain.entities.chat_message import ChatMessage
from src.allocation.domain.repositories.llm_interface import LLMInterface


class GoogleLLM(LLMInterface):

    def __init__(self, settings: LLMSettings | None = None):
        self.settings = settings or LLMSettings()
        self._client = _genai.Client(api_key=self.settings.google_api_key)

    def generate_text(self, prompt: str) -> str:
        response = self._client.models.generate_content(
            model=self.settings.google_llm,  # type: ignore
            contents=prompt,
        )
        return response.text

    def __call__(self, messages: List[ChatMessage]) -> str:
        # Convert ChatMessage entities to strings for the LLM
        prompt = "\n".join(f"{m.role}: {m.content}" for m in messages)
        return self.generate_text(prompt)
