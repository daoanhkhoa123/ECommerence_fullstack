from typing import Any, Dict, Sequence, Tuple
from db.backend_db import Repository
from db.backend_db.entities import VendorsProductsColumns

class VendorProductRepository(Repository):
    def __init__(self):
        super().__init__(table_name="vendors_products")

    def find_all(self) -> Sequence[Tuple]:
        return super().find_all()

    def find_by_vendor(self, vendor_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"vendor_id": vendor_id})

    def find_by_product(self, product_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"product_id": product_id})

    def find_by_vendor_and_product(self, vendor_id: int, product_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"vendor_id": vendor_id, "product_id": product_id})

    def select_columns(self, *columns: VendorsProductsColumns) -> Sequence[Tuple]:
        return super().find_fields(*columns)

    def find_fields_by_filters(self, fields: Sequence[VendorsProductsColumns], filters: Dict[VendorsProductsColumns, Any]) -> Sequence[Tuple]:
        return super().find_fields_by_filters(fields, filters)
