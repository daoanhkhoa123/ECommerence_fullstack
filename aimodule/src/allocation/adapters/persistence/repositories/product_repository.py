from typing import Optional, List
from sqlalchemy.orm import Session

from src.allocation.adapters.persistence.models.product_model import ProductModel
from src.allocation.domain.entities.product import Product


class ProductRepository:
    def __init__(self, session: Session):
        self.session = session

    def add(self, product: Product):
        model = ProductModel(
            name=product.name,
            description=product.description,
            brand=product.brand,
            image_url=product.image_url,
            price=product.price,
            stock=product.stock,
            sku=product.sku,
            is_featured=product.is_featured,
            vendor_id=product.vendor.id if product.vendor else None,
            vendor_product_id=product.vendor_product_id,
        )
        self.session.add(model)
        self.session.flush()  # assigns id
        product.product_id = model.product_id
        return product

    def get(self, product_id: int) -> Optional[Product]:
        model = self.session.get(ProductModel, product_id)
        return self._to_entity(model) if model else None

    def get_by_vendor_product_id(self, vendor_product_id: int) -> Optional[Product]:
        model = (
            self.session.query(ProductModel)
            .filter_by(vendor_product_id=vendor_product_id)
            .first()
        )
        return self._to_entity(model) if model else None

    def update(self, product: Product):
        model = self.session.get(ProductModel, product.product_id)
        if not model:
            return None

        model.name = product.name
        model.description = product.description
        model.brand = product.brand
        model.image_url = product.image_url
        model.price = product.price
        model.stock = product.stock
        model.sku = product.sku
        model.is_featured = product.is_featured

        self.session.add(model)
        return product

    def list_all(self) -> List[Product]:
        models = self.session.query(ProductModel).all()
        return [self._to_entity(m) for m in models]

    def _to_entity(self, model: ProductModel) -> Product:
        if not model:
            return None
        return Product(
            vendor_product_id=model.vendor_product_id,
            product_id=model.product_id,
            name=model.name,
            description=model.description,
            brand=model.brand,
            image_url=model.image_url,
            price=model.price,
            stock=model.stock,
            sku=model.sku,
            is_featured=model.is_featured,
        )
