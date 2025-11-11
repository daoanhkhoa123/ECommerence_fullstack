from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.category_event import (
    CategoryCreateUpdateEvent, CategoryReadDeleteEvent)
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.services import category_service
import logging

logger = logging.getLogger(__name__)

@register_topic("category.create.update")
async def handle_category_create_update(event: dict):
    """Handle event for creating or updating a category."""
    logger.info("Received category.create.update event: %s", event)

    data = CategoryCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        category = category_service.find_category_by_id(data.category_id, uow)
        if category:
            category_service.update_category(data.category_id, data, uow)
            logger.info(
                "Updated category record - id=%s, name=%s",
                data.category_id,
                data.name,
            )
        else:
            category_service.create_category(data, uow)
            logger.info(
                "Created new category - id=%s, name=%s",
                data.category_id,
                data.name,
            )

    except Exception as e:
        logger.exception(
            "Error handling category.create.update for id=%s, name=%s",
            data.category_id,
            getattr(data, "name", None),
        )


@register_topic("category.read.delete")
async def handle_category_delete(event: dict):
    """Handle event for deleting a category."""
    logger.info("Received category.read.delete event: %s", event)

    data = CategoryReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        category_service.delete_category(data.category_id, uow)
        logger.info("Deleted category record - id=%s", data.category_id)

    except Exception:
        logger.exception(
            "Error handling category.read.delete for id=%s", data.category_id
        )
