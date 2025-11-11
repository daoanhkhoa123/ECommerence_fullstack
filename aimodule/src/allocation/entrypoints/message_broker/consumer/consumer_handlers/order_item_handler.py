from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.order_item_event import (
    OrderItemCreateEvent, OrderItemDeleteEvent)
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.services import order_item_service
import logging

logger = logging.getLogger(__name__)
uow = SqlAlchemyUnitOfWork()


@register_topic("order-item.create.v1")
async def handle_order_item_create(event: dict):
    """Handle event for creating an order item."""
    logger.info("Received order-item.create.v1 event: %s", event)
    data = OrderItemCreateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        class Request:
            vendor_product_id = data.vendor_product_id
            quantity = data.quantity

        order_item_service.create_order_item_product(
            customer_id=data.actor_id,
            request=Request,
            uow=uow,
        )
        logger.info(
            "Order item created - order_item_id=%s, vendor_product_id=%s, quantity=%s, actor_id=%s",
            getattr(data, "order_item_id", None),
            data.vendor_product_id,
            data.quantity,
            data.actor_id,
        )
    except Exception:
        logger.exception(
            "Failed to create order item - vendor_product_id=%s, quantity=%s, actor_id=%s",
            data.vendor_product_id,
            data.quantity,
            data.actor_id,
        )


@register_topic("order-item.delete.v1")
async def handle_order_item_delete(event: dict):
    """Handle event for deleting an order item."""
    logger.info("Received order-item.delete.v1 event: %s", event)
    data = OrderItemDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        order_item_service.delete_order_item(
            customer_id=data.actor_id,
            order_item_id=data.order_item_id,
            uow=uow,
        )
        logger.info(
            "Order item deleted - order_item_id=%s, actor_id=%s",
            data.order_item_id,
            data.actor_id,
        )
    except Exception:
        logger.exception(
            "Failed to delete order item - order_item_id=%s, actor_id=%s",
            data.order_item_id,
            data.actor_id,
        )