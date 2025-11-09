from sqlalchemy import Column, ForeignKey, Integer, String, Text
from sqlalchemy.orm import relationship
from src.allocation.adapters.persistence.database import Base


class VendorModel(Base):
    __tablename__ = "vendors"

    id = Column(Integer, primary_key=True, autoincrement=True)
    account_id = Column(Integer, ForeignKey("accounts.id"), nullable=False)

    shop_name = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)
    phone = Column(String(50), nullable=True)

    # relationships
    account = relationship("AccountModel", back_populates="vendor")
    products = relationship("ProductModel", back_populates="vendor", cascade="all, delete-orphan")
