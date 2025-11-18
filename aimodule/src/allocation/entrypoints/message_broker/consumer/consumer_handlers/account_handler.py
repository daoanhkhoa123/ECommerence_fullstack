import logging

from src.allocation.adapters.message_bus.broker.dispatcher import register_topic
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.consumer.schemas.customer_event import (
    CustomerCreateUpdateEvent,
    CustomerReadDeleteEvent,
)
from src.allocation.entrypoints.message_broker.consumer.schemas.vendor_event import (
    VendorCreateUpdateEvent,
    VendorReadDeleteEvent,
)
from src.allocation.services import account_service

logger = logging.getLogger(__name__)

# --------------------------------------------------
# CUSTOMER HANDLERS
# --------------------------------------------------

@register_topic("customer.create.v1")
async def handle_customer_create(event: dict):
    """Handle event for creating a new customer."""
    data = CustomerCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.register_customer(data, uow)
    logger.info(
        "Customer created - customer_id=%s, account_id=%s, email=%s",
        data.customer_id,
        data.actor_id,
        data.email,
    )


@register_topic("customer.update.v1")
async def handle_customer_update(event: dict):
    """Handle event for updating an existing customer."""
    data = CustomerCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.update_customer(data.customer_id, data, uow)
    logger.info(
        "Customer updated - customer_id=%s, account_id=%s, email=%s",
        data.customer_id,
        data.actor_id,
        data.email,
    )


@register_topic("customer.delete.v1")
async def handle_customer_delete(event: dict):
    """Handle event for deleting a customer."""
    data = CustomerReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.delete_customer(data.customer_id, uow)
    logger.info(
        "Customer deleted - customer_id=%s, account_id=%s",
        data.customer_id,
        data.actor_id,
    )


@register_topic("customer.read.v1")
async def handle_customer_read(event: dict):
    """Handle event for reading a customer."""
    data = CustomerReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    customer = account_service.find_customer_by_id(data.customer_id, uow)
    if customer:
        logger.info(
            "Customer read - customer_id=%s, account_id=%s, full_name=%s",
            data.customer_id,
            data.actor_id,
            customer.full_name,
        )
    else:
        logger.info(
            "Customer read - Not found: customer_id=%s, account_id=%s",
            data.customer_id,
            data.actor_id,
        )


# --------------------------------------------------
# VENDOR HANDLERS
# --------------------------------------------------

@register_topic("vendor.create.v1")
async def handle_vendor_create(event: dict):
    """Handle event for creating a vendor."""
    data = VendorCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.register_vendor(data, uow)
    logger.info(
        "Vendor created - vendor_id=%s, account_id=%s, email=%s",
        data.vendor_id,
        data.actor_id,
        data.email,
    )


@register_topic("vendor.update.v1")
async def handle_vendor_update(event: dict):
    """Handle event for updating a vendor."""
    data = VendorCreateUpdateEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.update_vendor(data.vendor_id, data, uow)
    logger.info(
        "Vendor updated - vendor_id=%s, account_id=%s, email=%s",
        data.vendor_id,
        data.actor_id,
        data.email,
    )


@register_topic("vendor.delete.v1")
async def handle_vendor_delete(event: dict):
    """Handle event for deleting a vendor."""
    data = VendorReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    account_service.delete_vendor(data.vendor_id, uow)
    logger.info(
        "Vendor deleted - vendor_id=%s, account_id=%s",
        data.vendor_id,
        data.actor_id,
    )


@register_topic("vendor.read.v1")
async def handle_vendor_read(event: dict):
    """Handle event for reading a vendor."""
    data = VendorReadDeleteEvent(**event)
    uow = SqlAlchemyUnitOfWork()
    vendor = account_service.find_vendor_by_id(data.vendor_id, uow)
    if vendor:
        logger.info(
            "Vendor read - vendor_id=%s, account_id=%s, shop_name=%s",
            data.vendor_id,
            data.actor_id,
            vendor.shop_name,
        )
    else:
        logger.info(
            "Vendor read - Not found: vendor_id=%s, account_id=%s",
            data.vendor_id,
            data.actor_id,
        )