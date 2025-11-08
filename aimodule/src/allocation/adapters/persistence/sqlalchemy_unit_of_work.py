from src.allocation.services.unit_of_work import AbstractUnitOfWork
from src.allocation.adapters.persistence.repositories.customer_repository import CustomerRepository
from src.allocation.adapters.persistence.repositories.vendor_repository import VendorRepository
from src.allocation.adapters.persistence.database import SessionLocal

class SqlAlchemyUnitOfWork(AbstractUnitOfWork):
    def __init__(self, session_factory = SessionLocal):
        self.session_factory = session_factory

    def __enter__(self):
        self.session = self.session_factory()
        self.customers = CustomerRepository(self.session)
        self.vendors = VendorRepository(self.session)
        return super().__enter__()

    def __exit__(self, *args):
        self.rollback()
        self.session.close()

    def commit(self):
        self.session.commit()

    def rollback(self):
        self.session.rollback()
