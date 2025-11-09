from abc import ABC, abstractmethod
from typing import List, Optional

from src.allocation.domain.entities.category import Category


class AbstractCategoryRepository(ABC):
    @abstractmethod
    def add(self, category: Category) -> Category: ...
    @abstractmethod
    def get(self, category_id: int) -> Optional[Category]: ...
    @abstractmethod
    def list(self) -> List[Category]: ...
    @abstractmethod
    def delete(self, category_id: int) -> None: ...
