import logging

from src.allocation.adapters.message_bus.broker.dispatcher import \
    register_topic
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.vendor_product_event import (
    VendorProductCreateUpdateEvent, VendorProductDeleteEvent,
    VendorProductReadEvent)
from src.allocation.services import product_service

logger = logging.getLogger(__name__)
uow = SqlAlchemyUnitOfWork()

@register_topic("vendor-product.create.v1")
async def handle_vendor_product_create(event: dict):
    """Handle event for creating a vendor product."""
    logger.info("Received vendor-product.create.v1 event: %s", event)
    data = VendorProductCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        product_service.create_product(request=data, uow=uow)
        logger.info(
            "Vendor product created - actor_id=%s, name=%s, price=%s, stock=%d",
            data.actor_id,
            data.name,
            data.price,
            data.stock,
        )
    except Exception:
        logger.exception(
            "Failed to create vendor product - actor_id=%s, name=%s",
            data.actor_id,
            data.name,
        )

@register_topic("vendor-product.update.v1")
async def handle_vendor_product_update(event: dict):
    """Handle event for updating a vendor product."""
    logger.info("Received vendor-product.update.v1 event: %s", event)
    data = VendorProductCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        product_service.update_product(
            product_id=data.actor_id,  # ⚠️ make sure actor_id is the correct product_id
            request=data,
            uow=uow,
        )
        logger.info(
            "Vendor product updated - actor_id=%s, name=%s, price=%s, stock=%d",
            data.actor_id,
            data.name,
            data.price,
            data.stock,
        )
    except Exception:
        logger.exception(
            "Failed to update vendor product - actor_id=%s, name=%s",
            data.actor_id,
            data.name,
        )

@register_topic("vendor-product.delete.v1")
async def handle_vendor_product_delete(event: dict):
    """Handle event for deleting a vendor product."""
    logger.info("Received vendor-product.delete.v1 event: %s", event)
    data = VendorProductDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        product_service.delete_product(product_id=data.vendor_product_id, uow=uow)
        logger.info(
            "Vendor product deleted - vendor_product_id=%s, actor_id=%s",
            data.vendor_product_id,
            data.actor_id,
        )
    except Exception:
        logger.exception(
            "Failed to delete vendor product - vendor_product_id=%s, actor_id=%s",
            data.vendor_product_id,
            data.actor_id,
        )

@register_topic("vendor-product.read.v1")
async def handle_vendor_product_read(event: dict):
    """Handle event for reading vendor products by vendor_id."""
    logger.info("Received vendor-product.read.v1 event: %s", event)
    data = VendorProductReadEvent(**event)
    uow = SqlAlchemyUnitOfWork()

    try:
        products = product_service.find_by_vendor_id(vendor_id=data.vendor_id, uow=uow)
        logger.info(
            "Fetched products for vendor_id=%s - count=%d",
            data.vendor_id,
            len(products) if products else 0,
        )
    except Exception:
        logger.exception(
            "Failed to fetch products for vendor_id=%s", data.vendor_id
        )
