from fastapi import HTTPException, status
from src.allocation.domain.entities.account import Account, ACCOUNT_ROLE
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.vendor import Vendor
from src.allocation.services.unit_of_work import AbstractUnitOfWork


# --------------------------------------------------
# Helper functions
# --------------------------------------------------

def _set_account_fields(account: Account, request, role:ACCOUNT_ROLE) -> Account:
    account.id = request.actor_id
    account.role=role
    return account

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


from fastapi import HTTPException, status
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.vendor import Vendor
from src.allocation.domain.entities.account import Account
from src.allocation.services.unit_of_work import AbstractUnitOfWork

def find_user_by_account_id(account_id: int, uow: AbstractUnitOfWork) -> dict:
    """
    Find user information by account ID.
    Returns a dictionary with account info and either customer or vendor info.
    """
    with uow:
        account = uow.accounts.get(account_id)
        if not account:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Account not found with id: {account_id}"
            )

        user_info = {"account_id": account.id, "role": account.role}

        if account.role == "CUSTOMER":
            # Explicitly query the customer
            customer = uow.customers.get_by_account_id(account.id)
            if not customer:
                raise HTTPException(
                    status_code=status.HTTP_404_NOT_FOUND,
                    detail=f"Customer not found for account id: {account.id}"
                )
            user_info.update({
                "full_name": customer.full_name,
                "phone": customer.phone,
                "address": customer.address,
                "birth_date": customer.birth_date
            })
        elif account.role == "VENDOR":
            # Explicitly query the vendor
            vendor = uow.vendors.get_by_account_id(account.id)
            if not vendor:
                raise HTTPException(
                    status_code=status.HTTP_404_NOT_FOUND,
                    detail=f"Vendor not found for account id: {account.id}"
                )
            user_info.update({
                "shop_name": vendor.shop_name,
                "description": vendor.description,
                "phone": vendor.phone
            })
        else:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"No user data found for account id: {account.id}"
            )

        return user_info



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
        account = _set_account_fields(Account(), request, role="CUSTOMER")
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

        account = _set_account_fields(Account(), request, role="CUSTOMER")
        _set_customer_fields(customer, request)
        customer.account=account
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
        account = _set_account_fields(Account(), request, role="VENDOR")
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

        account = _set_account_fields(Account(), request, role="VENDOR")
        _set_vendor_fields(vendor, request)
        vendor.account=account
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
