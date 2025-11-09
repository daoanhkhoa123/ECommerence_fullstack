from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.vendor_product_event import (
    VendorProductCreateUpdateEvent, VendorProductDeleteEvent,
    VendorProductReadEvent)
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.services import product_service

uow = SqlAlchemyUnitOfWork()


@register_topic("vendor-product.create.v1")
async def handle_vendor_product_create(event: dict):
    data = VendorProductCreateUpdateEvent(**event)
    product_service.create_product(
        request=data,
        uow=uow,
    )


@register_topic("vendor-product.update.v1")
async def handle_vendor_product_update(event: dict):
    data = VendorProductCreateUpdateEvent(**event)
    product_service.update_product(
        product_id=data.actor_id,
        request=data,
        uow=uow,
    )


@register_topic("vendor-product.delete.v1")
async def handle_vendor_product_delete(event: dict):
    data = VendorProductDeleteEvent(**event)
    product_service.delete_product(
        product_id=data.vendor_product_id,
        uow=uow,
    )


@register_topic("vendor-product.read.v1")
async def handle_vendor_product_read(event: dict):
    data = VendorProductReadEvent(**event)
    product_service.find_by_vendor_id(
        vendor_id=data.vendor_id,
        uow=uow,
    )
