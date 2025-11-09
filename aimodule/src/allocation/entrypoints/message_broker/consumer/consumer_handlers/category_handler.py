from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.category_event import (
    CategoryCreateUpdateEvent, CategoryReadDeleteEvent)
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.services import category_service


@register_topic("category.create.update")
async def handle_category_create_update(event: dict):
    """Handle event for creating or updating a category."""
    data = CategoryCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    category = category_service.find_category_by_id(data.category_id, uow)
    if category:
        category_service.update_category(data.category_id, data, uow)
    else:
        category_service.create_category(data, uow)


@register_topic("category.read.delete")
async def handle_category_delete(event: dict):
    """Handle event for deleting a category."""
    data = CategoryReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    category_service.delete_category(data.category_id, uow)
