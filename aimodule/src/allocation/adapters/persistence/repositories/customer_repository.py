from typing import Optional
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.account import Account
from src.allocation.domain.repositories.customer_repository import AbstractCustomerRepository
from src.allocation.adapters.persistence.models.customer_model import CustomerModel

class CustomerRepository(AbstractCustomerRepository):
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

    def get(self, customer_id: int) -> Optional[Customer]:
        model = self.session.get(CustomerModel, customer_id)
        account = Account(model.account_id, "CUSTOMER")

        if not model:
            return None
        
        return Customer(
            id=model.id,
            account=account,
            full_name=model.full_name,
            phone=model.phone,
            address=model.address,
            birth_date=model.birth_date
        )

    def delete(self, customer: Customer) -> None:
        model = self.session.get(CustomerModel, customer.id)
        if model:
            self.session.delete(model)
