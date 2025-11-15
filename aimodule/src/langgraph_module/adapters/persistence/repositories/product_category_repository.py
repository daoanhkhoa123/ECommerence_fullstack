# src/allocation/adapters/persistence/product_category_repository.py
from typing import List

from sqlalchemy import delete, insert
from sqlalchemy.orm import Session
from src.allocation.adapters.persistence.models.category_model import \
    CategoryModel
from src.allocation.adapters.persistence.models.products_categories_model import \
    products_categories
from src.allocation.domain.entities.category import Category


class SqlAlchemyProductCategoryRepository:
    def __init__(self, session: Session):
        self.session = session

    def find_categories_by_product_id(self, product_id: int) -> List[Category]:
        results = (
            self.session.query(CategoryModel)
            .join(products_categories, CategoryModel.id == products_categories.c.category_id)
            .filter(products_categories.c.product_id == product_id)
            .all()
        )
        return [Category(id=r.id, name=r.name, description=r.description) for r in results] # type: ignore