from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.base import Base 

class AccountModel(Base):
    __tablename__ = "accounts"

    id = Column(Integer, primary_key=True, autoincrement=True)
    role = Column(String, nullable=False)

    customer = relationship("CustomerModel", back_populates="account", uselist=False)
    vendor = relationship("VendorModel", back_populates="account", uselist=False)

    chat_messages = relationship(
        "ChatMessageModel",
        back_populates="account",
        cascade="all, delete-orphan"
    )
