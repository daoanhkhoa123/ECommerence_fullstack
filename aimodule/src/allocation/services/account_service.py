from fastapi import HTTPException, status

from src.allocation.domain.entities.account import Account
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.vendor import Vendor
from src.allocation.services.unit_of_work import AbstractUnitOfWork


# --------------------------------------------------
# Helper functions
# --------------------------------------------------
def _create_account(role: str) -> Account:
    """Create a new Account entity with only id and role."""
    return Account(role=role)


def _set_customer_fields(customer: Customer, request) -> Customer:
    customer.full_name = request.full_name
    customer.phone = request.phone
    customer.address = request.address
    customer.birth_date = request.birth_date
    return customer


def _set_vendor_fields(vendor: Vendor, request) -> Vendor:
    vendor.shop_name = request.shop_name
    vendor.description = request.description
    vendor.phone = request.phone
    return vendor


# --------------------------------------------------
# Customer Operations
# --------------------------------------------------
def find_customer_by_id(customer_id: int, uow: AbstractUnitOfWork) -> Customer:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Customer not found with id: {customer_id}"
            )
        return customer


def register_customer(request, uow: AbstractUnitOfWork) -> Customer:
    with uow:
        account = _create_account(role="CUSTOMER")
        customer = Customer(account=account)
        _set_customer_fields(customer, request)

        uow.accounts.add(account)
        uow.customers.add(customer)
        uow.commit()
        return customer


def update_customer(customer_id: int, request, uow: AbstractUnitOfWork) -> Customer:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Customer not found"
            )

        # Account only has role
        .account.role = "CUSTOMER"
        _set_customer_fields(customer, request)
        uow.commit()
        return customer


def delete_customer(customer_id: int, uow: AbstractUnitOfWork):
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Customer not found"
            )

        if customer.account:
            uow.accounts.delete(customer.account)
        uow.customers.delete(customer)
        uow.commit()


# --------------------------------------------------
# Vendor Operations
# --------------------------------------------------
def find_vendor_by_id(vendor_id: int, uow: AbstractUnitOfWork) -> Vendor:
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Vendor not found with id: {vendor_id}"
            )
        return vendor


def register_vendor(request, uow: AbstractUnitOfWork) -> Vendor:
    with uow:
        account = _create_account(role="VENDOR")
        vendor = Vendor(account=account)
        _set_vendor_fields(vendor, request)

        uow.accounts.add(account)
        uow.vendors.add(vendor)
        uow.commit()
        return vendor


def update_vendor(vendor_id: int, request, uow: AbstractUnitOfWork) -> Vendor:
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Vendor not found"
            )

        _set_vendor_fields(vendor, request)
        uow.commit()
        return vendor


def delete_vendor(vendor_id: int, uow: AbstractUnitOfWork):
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Vendor not found"
            )

        if vendor.account:
            uow.accounts.delete(vendor.account)
        uow.vendors.delete(vendor)
        uow.commit()
