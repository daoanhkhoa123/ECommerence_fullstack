from sqlalchemy import event, select
from sqlalchemy.orm import Session

from src.allocation.adapters.persistence.models.product_model import \
    ProductModel
from src.configs.settings import LLMSettings
from src.langgraph_module.adapters.persistence.models.product_embedding_model import \
    ProductEmbeddingModel
from src.langgraph_module.exceptions.embedding_model_mismatch_exception import \
    EmbeddingModelMismatchException
from src.langgraph_module.llms.jina_embedding import JinaEmbedding

llm_setting = LLMSettings() # type: ignore

@event.listens_for(ProductModel, "after_insert")
@event.listens_for(ProductModel, "after_update")
def update_product_embedding(mapper, connection, target: ProductModel):
    embedder = JinaEmbedding("jina-embeddings-v3")
    vector = embedder(str(target))

    if llm_setting.embedding_model != embedder.model:
        raise EmbeddingModelMismatchException(llm_setting.embedding_model, embedder.model)

    with Session(bind=connection) as session:
        result = session.execute(select(ProductEmbeddingModel)
                                 .where(ProductEmbeddingModel.product_id== target.product_id))
        
    embedding_obj = result.scalars().first()
    if embedding_obj:
        embedding_obj.embedding = vector
        embedding_obj.embedding_model = "jina-embeddings-v3"
        embedding_obj.embedding_dim = len(vector)
        
    else:
        embedding_obj = ProductEmbeddingModel(
            product_id=target.product_id,
            embedding=vector,
            embedding_model="jina-embeddings-v3",
            embedding_dim=len(vector),
        )
        session.add(embedding_obj)

    session.commit()
