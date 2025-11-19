from sqlalchemy import ForeignKey, Integer, String, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship

from src.allocation.adapters.persistence.database import Base


class VendorModel(Base):
    __tablename__ = "vendors"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    account_id: Mapped[int] = mapped_column(ForeignKey("accounts.id"), nullable=False)

    shop_name: Mapped[str] = mapped_column(String(255), nullable=False)
    description: Mapped[str | None] = mapped_column(Text, nullable=True)
    phone: Mapped[str | None] = mapped_column(String(50), nullable=True)

    account: Mapped["AccountModel"] = relationship(back_populates="vendor") # type: ignore
    products: Mapped[list["ProductModel"]] = relationship( # type: ignore
        back_populates="vendor",
        cascade="all, delete-orphan"
    )
