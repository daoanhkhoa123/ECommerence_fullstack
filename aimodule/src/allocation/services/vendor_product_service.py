# from typing import List

# from fastapi import HTTPException, status

# from src.allocation.domain.entities.product import Product
# from src.allocation.domain.entities.vendor import Vendor
# from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork


# def find_by_vendor_id(vendor_id: int, uow: AbstractUnitOfWork) -> List[Product]:
#     with uow:
#         return uow.products.find_by_vendor_id(vendor_id)


# def create_product(vendor_id: int, request, uow: AbstractUnitOfWork) -> Product:
#     with uow:
#         vendor: Vendor = uow.vendors.get(vendor_id)
#         if not vendor:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail=f"Vendor not found with id {vendor_id}"
#             )

#         product = Product(
#             vendor=vendor,
#             name=request.name,
#             description=request.description,
#             brand=request.brand,
#             image_url=request.image_url,
#             price=request.price,
#             stock=request.stock,
#             sku=request.sku,
#             is_featured=request.is_featured
#         )
#         uow.products.add(product)
#         uow.commit()
#         return product


# def update_product(product_id: int, request, uow: AbstractUnitOfWork) -> Product:
#     with uow:
#         product: Product = uow.products.get(product_id)
#         if not product:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail="Product not found"
#             )

#         product.name = request.name
#         product.description = request.description
#         product.brand = request.brand
#         product.image_url = request.image_url
#         product.price = request.price
#         product.stock = request.stock
#         product.sku = request.sku
#         product.is_featured = request.is_featured

#         uow.commit()
#         return product


# def delete_product(product_id: int, uow: AbstractUnitOfWork):
#     with uow:
#         product = uow.products.get(product_id)
#         if not product:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail="Product not found"
#             )
#         uow.products.delete(product)
#         uow.commit()
