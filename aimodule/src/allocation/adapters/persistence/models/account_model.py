from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base


class AccountModel(Base):
    __tablename__ = "accounts"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=False, nullable=False)
    role: Mapped[str] = mapped_column(String(50), nullable=False)

    customer: Mapped["CustomerModel"] = relationship(back_populates="account", uselist=False) # type: ignore
    vendor: Mapped["VendorModel"] = relationship(back_populates="account", uselist=False) # type: ignore

    chat_messages: Mapped[list["ChatMessageModel"]] = relationship( # type: ignore
        back_populates="account",
        cascade="all, delete-orphan"
    )