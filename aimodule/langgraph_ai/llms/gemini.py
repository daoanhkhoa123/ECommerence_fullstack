from google import genai as _genai
from config.settings import LLMSettings

__all__ = ["generate_text"]

llm_settings = LLMSettings()
_client = _genai.Client(api_key=llm_settings.google_api_key)

def generate_text(prompt: str) -> str:
    return _client.models.generate_content(
        model=llm_settings.google_llm, # type: ignore
        contents=prompt
    ).text
