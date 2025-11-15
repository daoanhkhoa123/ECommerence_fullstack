from typing import Optional

from src.allocation.adapters.persistence.models.customer_model import \
    CustomerModel
from src.allocation.domain.entities.account import Account
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.repositories.customer_repository import \
    AbstractCustomerRepository


class SqlAlchemyCustomerRepository(AbstractCustomerRepository):
    def __init__(self, session):
        self.session = session

    def add(self, customer: Customer) -> None:
        model = CustomerModel(
            id=customer.id,
            account_id=customer.account.id,
            full_name=customer.full_name,
            phone=customer.phone,
            address=customer.address,
            birth_date=customer.birth_date
        )
        self.session.add(model)

    def get_by_account_id(self, account_id: int) -> Optional[Customer]:
        return self.session.query(CustomerModel).filter_by(account_id=account_id).first()
