# from fastapi import HTTPException, status

# from src.allocation.domain.entities.product import Product
# from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork


# def create_product(request, uow: AbstractUnitOfWork) -> Product:
#     """
#     Create a new product
#     """
#     with uow:
#         product = Product(
#             name=request.name,
#             description=request.description,
#             brand=request.brand,
#             image_url=request.image_url,
#             price=request.price,
#             stock=request.stock,
#             sku=request.sku,
#             is_featured=request.is_featured,
#         )
#         uow.products.add(product)
#         uow.commit()
#         return product


# def find_by_vendor_id(vendor_id: int, uow: AbstractUnitOfWork) -> Product:
#     """
#     Find a product by vendor_product_id
#     """
#     with uow:
#         product = uow.products.get_by_vendor_product_id(vendor_id)
#         if not product:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail=f"Product not found for vendor_product_id: {vendor_id}"
#             )
#         return product


# def update_product(product_id: int, request, uow: AbstractUnitOfWork) -> Product:
#     """
#     Update product details
#     """
#     with uow:
#         product = uow.products.get(product_id)
#         if not product:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail=f"Product not found with id: {product_id}"
#             )

#         product.name = request.name
#         product.description = request.description
#         product.brand = request.brand
#         product.image_url = request.image_url
#         product.price = request.price
#         product.stock = request.stock
#         product.sku = request.sku
#         product.is_featured = request.is_featured

#         uow.products.update(product)
#         uow.commit()
#         return product


# def delete_product(product_id: int, uow: AbstractUnitOfWork):
#     with uow:
#         product = uow.products.get(product_id)
#         if not product:
#             raise HTTPException(
#                 status_code=status.HTTP_404_NOT_FOUND,
#                 detail=f"Product not found with id: {product_id}"
#             )
#         uow.products.delete(product)
#         uow.commit()
