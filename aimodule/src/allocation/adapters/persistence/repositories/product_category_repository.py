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

    def delete_by_category_id(self, category_id: int):
        self.session.execute(
            delete(products_categories).where(products_categories.c.category_id == category_id)
        )

    def add(self, product_id: int, category_id: int):
        """Add a new product-category relationship."""
        self.session.execute(
            insert(products_categories).values(product_id=product_id, category_id=category_id)
        )

    def delete_by_product_and_category_ids(self, product_id: int, category_ids: List[int]):
        """Delete specific product-category links."""
        if not category_ids:
            return
        self.session.execute(
            delete(products_categories).where(
                products_categories.c.product_id == product_id,
                products_categories.c.category_id.in_(category_ids)
            )
        )

    def delete_all_for_product(self, product_id: int):
        """Delete all category links for a given product."""
        self.session.execute(
            delete(products_categories).where(products_categories.c.product_id == product_id)
        )
