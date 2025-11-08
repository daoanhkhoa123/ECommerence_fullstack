from abc import ABC, abstractmethod
from typing import Optional, List
from src.allocation.domain.entities.order_item import OrderItem

class AbstractOrderItemRepository(ABC):
    @abstractmethod
    def add(self, item: OrderItem) -> OrderItem: ...
    @abstractmethod
    def get(self, item_id: int) -> Optional[OrderItem]: ...
    @abstractmethod
    def list_by_order(self, order_id: int) -> List[OrderItem]: ...
