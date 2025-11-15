from abc import ABC, abstractmethod
from typing import Optional

from src.allocation.domain.entities.customer import Customer


class AbstractCustomerRepository(ABC):

    @abstractmethod
    def add(self, customer: Customer) -> None:
        ...

    @abstractmethod
    def get(self, customer_id: int) -> Optional[Customer]:
        ...

    @abstractmethod
    def delete(self, customer: Customer) -> None:
        ...
