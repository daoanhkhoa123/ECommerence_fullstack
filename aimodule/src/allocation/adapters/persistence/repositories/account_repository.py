from typing import Optional
from src.allocation.domain.entities.account import Account
from src.allocation.domain.repositories.account_repository import AbstractAccountRepository
from src.allocation.adapters.persistence.models.account_model import AccountModel

class AccountRepository(AbstractAccountRepository):
    def __init__(self, session):
        self.session = session

    def add(self, account: Account) -> None:
        model = AccountModel(id=account.id, role=account.role)
        self.session.add(model)

    def get(self, account_id: int) -> Optional[Account]:
        model = self.session.get(AccountModel, account_id)
        if not model:
            return None
        return Account(id=model.id, role=model.role)

    def delete(self, account: Account) -> None:
        model = self.session.get(AccountModel, account.id)
        if model:
            self.session.delete(model)
