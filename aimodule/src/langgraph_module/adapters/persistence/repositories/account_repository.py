from typing import Optional

from src.allocation.adapters.persistence.models.account_model import \
    AccountModel
from src.allocation.domain.entities.account import Account
from src.langgraph_module.domain.repositories.account_repository import \
    AbstractAccountRepository


class SqlAlchemyAccountRepository(AbstractAccountRepository):
    def __init__(self, session):
        self.session = session

    def get(self, account_id: int) -> Optional[Account]:
        model = self.session.get(AccountModel, account_id)
        if not model:
            return None
        return Account(id=model.id, role=model.role)
