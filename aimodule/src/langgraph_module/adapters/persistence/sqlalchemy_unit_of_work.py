from src.allocation.adapters.persistence.database import SessionLocal
from src.langgraph_module.adapters.persistence.models.product_embedding_model import \
    ProductEmbeddingModel
from src.langgraph_module.adapters.persistence.repositories.chat_message_repository import \
    SqlAlchemyChatMessageRepository
from src.langgraph_module.adapters.persistence.repositories.product_embedding_repository import \
    SqlAlchemyProductEmbeddingRepository

ProductEmbeddingModel()

class SqlAlchemyUnitOfWork:
    def __init__(self):
        self.session = SessionLocal()

    def __enter__(self):
        self.chat_messages = SqlAlchemyChatMessageRepository(self.session)
        self.product_embeddings = SqlAlchemyProductEmbeddingRepository(self.session)
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
