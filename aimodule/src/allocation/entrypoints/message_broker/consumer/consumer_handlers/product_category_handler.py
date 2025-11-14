from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.product_category_event import \
    ProductCategoryCreateDeleteEvent
from src.allocation.adapters.message_bus.broker.dispatcher import register_topic
from src.allocation.services import product_category_service
import logging

logger = logging.getLogger(__name__)
uow = SqlAlchemyUnitOfWork()


@register_topic("product-category.create.v1")
async def handle_product_category_create(event: dict):
    data = ProductCategoryCreateDeleteEvent(**event)
    product_category_service.add_product_category(
        product_id=data.product_id,
        category_ids=data.category_ids,
        uow=uow,
    )


@register_topic("product-category.delete.v1")
async def handle_product_category_delete(event: dict):
    data = ProductCategoryCreateDeleteEvent(**event)
    product_category_service.remove_product_category(
        product_id=data.product_id,
        category_ids=data.category_ids,
        uow=uow,
    )
