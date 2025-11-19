from dataclasses import dataclass
from typing import List, Optional

from src.allocation.domain.entities.product import Product

ProductVector=List[float]

@dataclass
class ProductEmbedding:
    product: Product
    embedding: ProductVector
    embedding_model: Optional[str] = None
    embedding_dim: Optional[int] = None