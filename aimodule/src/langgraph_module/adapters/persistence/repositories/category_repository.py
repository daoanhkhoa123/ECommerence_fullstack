from typing import List, Optional

from sqlalchemy.orm import Session
from src.allocation.adapters.persistence.models.category_model import \
    CategoryModel
from src.allocation.domain.entities.category import Category


class SqlAlchemyCategoryRepository:
    def __init__(self, session: Session):
        self.session = session

    # --- CRUD operations ---
    def get(self, category_id: int) -> Optional[Category]:
        model = self.session.get(CategoryModel, category_id)
        if not model:
            return None
        return self._model_to_entity(model)

    def exists_by_name(self, name: str) -> bool:
        return self.session.query(CategoryModel).filter(CategoryModel.name == name).first() is not None

    # --- Helper mapping ---
    def _model_to_entity(self, model: CategoryModel) -> Category:
        return Category(id=model.id, name=model.name, description=model.description) # type: ignore
