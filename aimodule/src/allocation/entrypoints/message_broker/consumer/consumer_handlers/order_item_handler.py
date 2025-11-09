from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.order_item_event import (
    OrderItemCreateEvent, OrderItemDeleteEvent)
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.services import order_item_service

uow = SqlAlchemyUnitOfWork()


@register_topic("order-item.create.v1")
async def handle_order_item_create(event: dict):
    data = OrderItemCreateEvent(**event)

    class Request:
        vendor_product_id = data.vendor_product_id
        quantity = data.quantity

    uow = SqlAlchemyUnitOfWork()
    order_item_service.create_order_item_product(
        customer_id=data.actor_id,
        request=Request,
        uow=uow,
    )


@register_topic("order-item.delete.v1")
async def handle_order_item_delete(event: dict):
    data = OrderItemDeleteEvent(**event)

    uow = SqlAlchemyUnitOfWork()
    order_item_service.delete_order_item(
        customer_id=data.actor_id,
        order_item_id=data.order_item_id,
        uow=uow,
    )
