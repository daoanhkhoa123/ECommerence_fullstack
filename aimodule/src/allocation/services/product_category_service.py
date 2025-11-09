from typing import List

from fastapi import HTTPException, status
from src.allocation.domain.entities.category import Category
from src.allocation.domain.entities.product import Product
from src.allocation.services.unit_of_work import AbstractUnitOfWork


def add_product_category(
    product_id: int,
    category_ids: List[int],
    uow: AbstractUnitOfWork
):
    """Attach one or more categories to a product using the join table."""
    with uow:
        # Fetch product
        product: Product = uow.products.get(product_id)
        if not product:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Product not found: {product_id}"
            )

        # Link each category
        for cat_id in category_ids:
            category: Category = uow.categories.get(cat_id)
            if not category:
                raise HTTPException(
                    status_code=status.HTTP_404_NOT_FOUND,
                    detail=f"Category not found: {cat_id}"
                )

            # Persist link directly in join table
            uow.product_categories.add(product_id, cat_id)

        uow.commit()


def remove_product_category(
    product_id: int,
    category_ids: List[int],
    uow: AbstractUnitOfWork
):
    """Remove one or more category links from a product using the join table."""
    if not category_ids:
        return

    with uow:
        uow.product_categories.delete_by_product_and_category_ids(product_id, category_ids)
        uow.commit()
