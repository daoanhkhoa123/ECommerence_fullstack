from functools import partial
from google import genai
from config.settings import LLMSettings

llm_settings =LLMSettings()
_client = genai.Client(api_key=llm_settings.google_api_key)

generate_text = partial(_client.models.generate_content, 
                        model = llm_settings.google_llm) # type: ignore