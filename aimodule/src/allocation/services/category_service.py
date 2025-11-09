from typing import List

from fastapi import HTTPException, status
from src.allocation.domain.entities.category import Category
from src.allocation.services.unit_of_work import AbstractUnitOfWork


# -----------------------------
# Category service functions
# -----------------------------
def find_category_by_id(category_id: int, uow: AbstractUnitOfWork) -> Category:
    with uow:
        category = uow.categories.get(category_id)
        if not category:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Category not found with id: {category_id}"
            )
        return category


def find_all_categories_by_product_id(product_id: int, uow: AbstractUnitOfWork) -> List[Category]:
    with uow:
        categories = uow.product_categories.find_categories_by_product_id(product_id)
        return categories


def create_category(request, uow: AbstractUnitOfWork) -> Category:
    with uow:
        if uow.categories.exists_by_name(request.name):
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Category already exists with name: {request.name}"
            )

        category = Category(name=request.name, description=request.description)
        uow.categories.add(category)
        uow.commit()
        return category


def update_category(category_id: int, request, uow: AbstractUnitOfWork) -> Category:
    with uow:
        category = uow.categories.get(category_id)
        if not category:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Category not found"
            )

        # Check if new name conflicts with other categories
        if category.name != request.name and uow.categories.exists_by_name(request.name):
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail=f"Category already exists with name: {request.name}"
            )

        category.name = request.name
        category.description = request.description
        uow.commit()
        return category


def delete_category(category_id: int, uow: AbstractUnitOfWork):
    with uow:
        category = uow.categories.get(category_id)
        if not category:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Category not found"
            )

        # Delete all product-category relationships first
        uow.product_categories.delete_by_category_id(category_id)

        # Then delete the category
        uow.categories.delete(category)
        uow.commit()
