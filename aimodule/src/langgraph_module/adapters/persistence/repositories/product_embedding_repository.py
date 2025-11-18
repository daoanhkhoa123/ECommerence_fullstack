from typing import List, Optional

from sqlalchemy.orm import Session, selectinload

from src.allocation.adapters.persistence.models.product_model import \
    ProductModel
from src.langgraph_module.adapters.persistence.models.product_embedding_model import \
    ProductEmbeddingModel
from src.langgraph_module.domain.entities.product_embedding import \
    ProductVector


class SqlAlchemyProductEmbeddingRepository:
    def __init__(self, session: Session):
        self.session = session

    def add_embedding(self, product_id:int, embedding:ProductVector, model:Optional[str]= None, dim:Optional[int] = None) -> ProductEmbeddingModel:
        obj = ProductEmbeddingModel(product_id=product_id,
                                     embedding=embedding,
                                     embedding_model = model,
                                     embedding_dim = dim)
        self.session.add(obj)
        return obj
    

    def get_by_product_id(self, product_id:int) -> Optional[ProductEmbeddingModel]:
        return (
            self.session.query(ProductEmbeddingModel)
            .filter(ProductEmbeddingModel.product_id == product_id)
            .first()
        )
    
    def get_products_by_similarity_cosine_top_k(self, embedding: ProductVector, top_k:int=3) -> List[ProductModel]:
        sims = (
            self.session.query(ProductEmbeddingModel)
            .options(selectinload(ProductEmbeddingModel.product))
            .order_by(ProductEmbeddingModel.embedding.cosine_distance(embedding))
            .limit(top_k)
            .all()
        )
        return [sim.product for sim in sims]
