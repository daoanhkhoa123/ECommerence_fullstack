from typing import Any, Dict, Sequence, Tuple
from db.backend_db import Repository
from db.backend_db.entities import VendorsColumns

class VendorRepository(Repository):
    def __init__(self):
        super().__init__(table_name="vendors")

    def find_all(self) -> Sequence[Tuple]:
        return super().find_all()

    def find_by_shop_name(self, shop_name: str) -> Sequence[Tuple]:
        return super().find_by_filters({"shop_name": shop_name})

    def find_by_phone(self, phone: str) -> Sequence[Tuple]:
        return super().find_by_filters({"phone": phone})

    def select_columns(self, *columns: VendorsColumns) -> Sequence[Tuple]:
        return super().find_fields(*columns)

    def find_fields_by_filters(self, fields: Sequence[VendorsColumns], filters: Dict[VendorsColumns, Any]) -> Sequence[Tuple]: # type: ignore
        return super().find_fields_by_filters(fields, filters)

v =VendorRepository()
print(v.find_all())