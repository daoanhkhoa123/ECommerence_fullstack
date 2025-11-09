from abc import ABC, abstractmethod
from typing import List, Optional

from src.allocation.domain.entities.order import Order


class AbstractOrderRepository(ABC):
    @abstractmethod
    def add(self, order: Order) -> Order: ...
    @abstractmethod
    def get(self, order_id: int) -> Optional[Order]: ...
    @abstractmethod
    def list(self) -> List[Order]: ...
    @abstractmethod
    def update_status(self, order_id: int, status: str) -> None: ...
