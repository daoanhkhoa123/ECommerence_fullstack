from typing import Any, Dict, Sequence, Tuple
from db.backend_db import Repository
from db.backend_db.entities import ProductsColumns

class ProductRepository(Repository):
    def __init__(self):
        super().__init__(table_name="products")

    def find_all(self) -> Sequence[Tuple]:
        return super().find_all()

    def find_fields(self, *fields: ProductsColumns) -> Sequence[Tuple]:
        return super().find_fields(*fields)

    def find_by_name(self, name: str) -> Sequence[Tuple]:
        return super().find_by_filters({"name": name})

    def find_by_category(self, category: str) -> Sequence[Tuple]:
        return super().find_by_filters({"category": category})

    def select_columns(self, *columns: ProductsColumns) -> Sequence[Tuple]:
        return super().find_fields(*columns)

    def find_fields_by_filters(self, fields: Sequence[ProductsColumns], filters: Dict[ProductsColumns, Any]) -> Sequence[Tuple]:
        return super().find_fields_by_filters(fields, filters)
