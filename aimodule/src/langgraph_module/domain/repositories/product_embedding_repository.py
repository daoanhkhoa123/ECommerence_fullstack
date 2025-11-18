from abc import ABC, abstractmethod
from typing import List, Optional

from src.allocation.domain.entities.product import Product
from src.langgraph_module.domain.entities.product_embedding import \
    ProductEmbedding


class ProductEmbeddingRepository(ABC):
    @abstractmethod
    def add(self, embedding: ProductEmbedding) -> None:
        ...

    @abstractmethod
    def get_by_product(self, product: Product) -> Optional[ProductEmbedding]:
        ...

    @abstractmethod
    def list_all(self) -> List[ProductEmbedding]:
        ...

    @abstractmethod
    def delete(self, product: Product) -> None:
        ...
