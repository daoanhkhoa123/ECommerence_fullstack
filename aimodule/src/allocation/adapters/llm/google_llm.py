from typing import List

from src.allocation.configs.settings import LLMSettings
from google import genai as _genai
from src.allocation.domain.entities.chat_message import ChatMessage
from src.allocation.domain.repositories.llm_interface import LLMInterface


class GoogleLLM(LLMInterface):

    def __init__(self):
        self.settings = LLMSettings() # type: ignore
        self._client = _genai.Client(api_key=self.settings.google_api_key)

    def generate_text(self, prompt: str) -> str:
        response = self._client.models.generate_content(
            model=self.settings.google_llm,  # type: ignore
            contents=prompt,
        )
        return response.text # type: ignore

    def __call__(self, message: str) -> str:
        # Convert ChatMessage entities to strings for the LLM
        return self.generate_text(message)
