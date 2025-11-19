from fastapi import HTTPException, status

from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.domain.entities.account import ACCOUNT_ROLE, Account
from src.allocation.domain.entities.customer import Customer
from src.allocation.domain.entities.vendor import Vendor


def _set_account_fields(account: Account, request, role: ACCOUNT_ROLE) -> Account:
    account.id = request.actor_id
    account.role = role
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


def find_user_by_account_id(account_id: int, uow: SqlAlchemyUnitOfWork) -> dict:
    with uow:
        account = uow.accounts.get(account_id)
        if not account:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail=f"Account not found with id: {account_id}")

        user_info = {"account_id": account.id, "role": account.role}

        if account.role == "CUSTOMER":
            customer = uow.customers.get_by_account_id(account.id) # type: ignore
            if not customer:
                raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                    detail=f"Customer not found for account id: {account.id}")
            user_info.update({
                "full_name": customer.full_name,
                "phone": customer.phone,
                "address": customer.address,
                "birth_date": customer.birth_date
            })
        elif account.role == "VENDOR":
            vendor = uow.vendors.get_by_account_id(account.id) # type: ignore
            if not vendor:
                raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                    detail=f"Vendor not found for account id: {account.id}")
            user_info.update({
                "shop_name": vendor.shop_name,
                "description": vendor.description,
                "phone": vendor.phone
            })
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail=f"No user data found for account id: {account.id}")
        return user_info


def find_customer_by_id(customer_id: int, uow: SqlAlchemyUnitOfWork) -> Customer:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail=f"Customer not found with id: {customer_id}")
        return customer


def register_customer(request, uow: SqlAlchemyUnitOfWork) -> Customer:
    with uow:
        account = _set_account_fields(Account(), request, role="CUSTOMER")
        customer = Customer(account=account)
        _set_customer_fields(customer, request)
        uow.accounts.add(account)
        uow.customers.add(customer)
        uow.commit()
        return customer


def update_customer(customer_id: int, request, uow: SqlAlchemyUnitOfWork) -> Customer:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail="Customer not found")
        _set_customer_fields(customer, request)
        customer.account = _set_account_fields(customer.account or Account(), request, role="CUSTOMER")
        uow.commit()
        return customer


def delete_customer(customer_id: int, uow: SqlAlchemyUnitOfWork):
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail="Customer not found")
        if customer.account:
            uow.accounts.delete(customer.account)
        uow.customers.delete(customer)
        uow.commit()


def find_vendor_by_id(vendor_id: int, uow: SqlAlchemyUnitOfWork) -> Vendor:
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail=f"Vendor not found with id: {vendor_id}")
        return vendor


def register_vendor(request, uow: SqlAlchemyUnitOfWork) -> Vendor:
    with uow:
        account = _set_account_fields(Account(), request, role="VENDOR")
        vendor = Vendor(account=account)
        _set_vendor_fields(vendor, request)
        uow.accounts.add(account)
        uow.vendors.add(vendor)
        uow.commit()
        return vendor


def update_vendor(vendor_id: int, request, uow: SqlAlchemyUnitOfWork) -> Vendor:
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail="Vendor not found")
        _set_vendor_fields(vendor, request)
        vendor.account = _set_account_fields(vendor.account or Account(), request, role="VENDOR")
        uow.commit()
        return vendor


def delete_vendor(vendor_id: int, uow: SqlAlchemyUnitOfWork):
    with uow:
        vendor = uow.vendors.get(vendor_id)
        if not vendor:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                                detail="Vendor not found")
        if vendor.account:
            uow.accounts.delete(vendor.account)
        uow.vendors.delete(vendor)
        uow.commit()
