from typing import List, Optional

from sqlalchemy.orm import Session
from src.allocation.adapters.persistence.models.product_model import \
    ProductModel
from src.allocation.domain.entities.product import Product


class SqlAlchemyProductRepository:
    def __init__(self, session: Session):
        self.session = session

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

    def list_all(self) -> List[Product]:
        models = self.session.query(ProductModel).all()
        return [self._to_entity(m) for m in models]

    def _to_entity(self, model: ProductModel) -> Product:
        if not model:
            return None
        return Product(
            vendor_product_id=model.vendor_product_id, # type: ignore
            product_id=model.product_id, # type: ignore
            name=model.name, # type: ignore
            description=model.description, # type: ignore
            brand=model.brand, # type: ignore
            image_url=model.image_url, # type: ignore
            price=model.price, # type: ignore
            stock=model.stock, # type: ignore
            sku=model.sku, # type: ignore
            is_featured=model.is_featured, # type: ignore
        )
