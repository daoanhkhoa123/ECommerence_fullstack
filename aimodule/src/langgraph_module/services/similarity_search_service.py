from typing import List

from src.allocation.adapters.persistence.models.product_model import \
    ProductModel
from src.configs.settings import LLMSettings
from src.langgraph_module.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.langgraph_module.exceptions.embedding_model_mismatch_exception import \
    EmbeddingModelMismatchException
from src.langgraph_module.llms.jina_embedding import JinaEmbedding

llm_setting = LLMSettings() # type: ignore

def get_product_by_similarity(data: str, uow: SqlAlchemyUnitOfWork) -> List[ProductModel]:
    embedder = JinaEmbedding("jina-embeddings-v3")
    if llm_setting.embedding_model != embedder.model:
        raise EmbeddingModelMismatchException(llm_setting.embedding_model, embedder.model)

    vector = embedder(str(data))   
    with uow: 
        return uow.product_embeddings.get_products_by_similarity_cosine_top_k(vector)
