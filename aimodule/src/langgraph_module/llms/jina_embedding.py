import json
from typing import List, Literal

import requests

from src.configs.keys import LLMApiKeys
from src.configs.settings import DatabaseSettings
from src.langgraph_module.llms.decorator import llm_factory
from src.langgraph_module.llms.llm_interface import EmbeddingInterface

db_setting = DatabaseSettings() # type: ignore
key_setting = LLMApiKeys() # type: ignore


JINA_NAMES = Literal["jina-embeddings-v3"]

@llm_factory
class JinaEmbedding(EmbeddingInterface):
    def __init__(self, llm_name:JINA_NAMES) -> None:
        super().__init__()
        self._embedding_dim=db_setting.database_embedding_dim
        self._model = llm_name

        self._url = "https://api.jina.ai/v1/embeddings"
        self._header = {
                "Content-Type": "application/json",
                "Authorization": key_setting.jina_api_key
            }
    
    @property
    def model(self) -> str:
        return self._model

    def __call__(self, text: str) -> List[float]:
        data = {
            "model": self.model,
            "task": "text-matching",
            "dimensions": self._embedding_dim,
            "input": text
        }

        response = requests.post(self._url, headers=self._header,
                       data=json.dumps(data))
        return response.json()["data"][0]["embedding"]