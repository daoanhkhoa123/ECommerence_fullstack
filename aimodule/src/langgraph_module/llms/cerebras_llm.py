from src.configs.settings import LLMSettings
from google import genai as _genai
from src.allocation.domain.entities.chat_message import ChatMessage
from src.langgraph_module.llms.llm_interface import LLMInterface


class CerebrasLLM(LLMInterface):
    
