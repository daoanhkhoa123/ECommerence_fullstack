from sqlalchemy import Column, Integer, String, JSON
from src.allocation.adapters.persistence.base import Base

class ConversationStateModel(Base):
    __tablename__ = "conversation_states"
    user_id = Column(Integer, primary_key=True)
    current_node_id = Column(String, default="START")
    history = Column(JSON, default=[])
