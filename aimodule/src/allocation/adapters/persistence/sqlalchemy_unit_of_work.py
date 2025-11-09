from src.allocation.adapters.persistence.database import SessionLocal
from src.allocation.adapters.persistence.repositories.account_repository import \
    SqlAlchemyAccountRepository
from src.allocation.adapters.persistence.repositories.category_repository import \
    SqlAlchemyCategoryRepository
from src.allocation.adapters.persistence.repositories.chat_message_repository import \
    SqlAlchemyChatMessageRepository
from src.allocation.adapters.persistence.repositories.customer_repository import \
    SqlAlchemyCustomerRepository
from src.allocation.adapters.persistence.repositories.order_item_repository import \
    SqlAlchemyOrderItemRepository
from src.allocation.adapters.persistence.repositories.order_repository import \
    SqlAlchemyOrderRepository
from src.allocation.adapters.persistence.repositories.product_category_repository import \
    SqlAlchemyProductCategoryRepository
from src.allocation.adapters.persistence.repositories.product_repository import \
    SqlAlchemyProductRepository
from src.allocation.adapters.persistence.repositories.vendor_repository import \
    SqlAlchemyVendorRepository


class SqlAlchemyUnitOfWork:
    def __init__(self):
        self.session = SessionLocal()

    def __enter__(self):
        # attach all repositories here
        self.accounts = SqlAlchemyAccountRepository(self.session)
        self.categories = SqlAlchemyCategoryRepository(self.session)
        self.chat_messages = SqlAlchemyChatMessageRepository(self.session)
        self.customers = SqlAlchemyCustomerRepository(self.session)
        self.order_items = SqlAlchemyOrderItemRepository(self.session)
        self.orders = SqlAlchemyOrderRepository(self.session)
        self.product_categories = SqlAlchemyProductCategoryRepository(self.session)
        self.products = SqlAlchemyProductRepository(self.session)
        self.vendors = SqlAlchemyVendorRepository(self.session)
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        if exc_type:
            self.rollback()
        else:
            self.commit()
        self.session.close()

    def commit(self):
        self.session.commit()

    def rollback(self):
        self.session.rollback()
