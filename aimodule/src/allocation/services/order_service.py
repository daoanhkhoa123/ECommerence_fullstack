from typing import List
from fastapi import HTTPException, status
from src.allocation.domain.entities.order import Order
from src.allocation.services.unit_of_work import AbstractUnitOfWork


# -----------------------------
# Order service functions
# -----------------------------
def find_all_orders_by_customer(customer_id: int, uow: AbstractUnitOfWork) -> List[Order]:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=404, detail=f"Customer {customer_id} not found")

        orders = uow.orders.find_by_customer_id(customer_id)
        return orders


def update_order_status(customer_id: int, order_id: int, new_status: str, uow: AbstractUnitOfWork) -> Order:
    with uow:
        order = uow.orders.get(order_id)
        if not order:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Order with id {order_id} not found"
            )

        if order.customer_id != customer_id:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail=f"User id {customer_id} does not have access to order id {order.id}"
            )

        order.order_status = new_status
        uow.commit()
        return order


def find_cart_by_customer(customer_id: int, uow: AbstractUnitOfWork) -> Order:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=404, detail=f"Customer {customer_id} not found")

        order = uow.orders.find_first_by_customer_and_status(customer_id, "PENDING")
        if not order:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"No pending cart found for customer id {customer_id}"
            )
        return order
