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

    def add(self, category: Category):
        model = CategoryModel(name=category.name, description=category.description)
        self.session.add(model)
        self.session.flush()  # ensures ID is available
        category.id = model.id # type: ignore

    def delete(self, category: Category):
        model = self.session.get(CategoryModel, category.id)
        if model:
            self.session.delete(model)

    def exists_by_name(self, name: str) -> bool:
        return self.session.query(CategoryModel).filter(CategoryModel.name == name).first() is not None

    # --- Helper mapping ---
    def _model_to_entity(self, model: CategoryModel) -> Category:
        return Category(id=model.id, name=model.name, description=model.description) # type: ignore
