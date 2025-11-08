from abc import ABC, abstractmethod
from typing import Optional
from src.allocation.domain.entities.account import Account

class AbstractAccountRepository(ABC):

    @abstractmethod
    def add(self, account: Account) -> None:
        ...

    @abstractmethod
    def get(self, account_id: int) -> Optional[Account]:
        ...

    @abstractmethod
    def delete(self, account: Account) -> None:
        ...
