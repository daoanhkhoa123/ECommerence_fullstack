from src.allocation.services import order_service
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.entrypoints.message_broker.consumer.schemas.order_event import (
    OrderStatusUpdateEvent,
)

uow = SqlAlchemyUnitOfWork()


@register_topic("order-status.updated")
async def handle_order_status_updated(event: dict):
    data = OrderStatusUpdateEvent(**event)

    order_service.update_order_status(
        customer_id=data.actor_id,
        order_id=data.order_id,
        new_status=data.order_status,
        uow=uow,
    )
