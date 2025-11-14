from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.order_event import \
    OrderStatusUpdateEvent
from src.allocation.adapters.message_bus.broker.dispatcher import register_topic
from src.allocation.services import order_service
import logging

logger = logging.getLogger(__name__)
uow = SqlAlchemyUnitOfWork()

import logging
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.order_event import OrderStatusUpdateEvent
from src.allocation.adapters.message_bus.broker.dispatcher import register_topic
from src.allocation.services import order_service

logger = logging.getLogger(__name__)


@register_topic("order-status.updated")
async def handle_order_status_updated(event: dict):
    """Handle event for updating an order status."""
    logger.info("Received order-status.updated event: %s", event)
    data = OrderStatusUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        order_service.update_order_status(
            customer_id=data.actor_id,
            order_id=data.order_id,
            new_status=data.order_status,
            uow=uow,
        )
        logger.info(
            "Order status updated successfully - order_id=%s, new_status=%s, actor_id=%s",
            data.order_id,
            data.order_status,
            data.actor_id,
        )
    except Exception:
        logger.exception(
            "Failed to update order status - order_id=%s, new_status=%s, actor_id=%s",
            data.order_id,
            data.order_status,
            data.actor_id,
        )
