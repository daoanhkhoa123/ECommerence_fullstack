from typing import Optional

from src.allocation.adapters.persistence.models.vendor_model import VendorModel
from src.allocation.domain.entities.account import Account
from src.allocation.domain.entities.vendor import Vendor
from src.allocation.domain.repositories.vendor_repository import \
    AbstractVendorRepository


class SqlAlchemyVendorRepository(AbstractVendorRepository):
    def __init__(self, session):
        self.session = session

    def add(self, vendor: Vendor) -> None:
        model = VendorModel(
            id=vendor.id,
            account_id=vendor.account.id,
            shop_name=vendor.shop_name,
            description=vendor.description,
            phone=vendor.phone
        )
        self.session.add(model)

    def get(self, vendor_id: int) -> Optional[Vendor]:
        model = self.session.get(VendorModel, vendor_id)
        account = Account(model.account_id, "CUSTOMER")

        if not model:
            return None
        return Vendor(
            id=model.id,
            account=account,
            shop_name=model.shop_name,
            description=model.description,
            phone=model.phone
        )

    def delete(self, vendor: Vendor) -> None:
        model = self.session.get(VendorModel, vendor.id)
        if model:
            self.session.delete(model)

    def get_by_account_id(self, account_id: int) -> Optional[Vendor]:
        return self.session.query(VendorModel).filter_by(account_id=account_id).first()
