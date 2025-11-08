from typing import List
from fastapi import HTTPException, status
from decimal import Decimal

from src.allocation.domain.entities.order_item import OrderItem
from src.allocation.services.unit_of_work import AbstractUnitOfWork


def create_order_item_product(customer_id: int, request, uow: AbstractUnitOfWork) -> OrderItem:
    with uow:
        customer = uow.customers.get(customer_id)
        if not customer:
            raise HTTPException(status_code=404, detail=f"Customer {customer_id} not found")

        order = uow.orders.find_first_by_customer_and_status(customer_id, "PENDING")
        if not order:
            raise HTTPException(status_code=400, detail="Customer must have a pending order first")

        vp = uow.vendor_products.get(request.vendor_product_id)
        if not vp:
            raise HTTPException(status_code=404, detail=f"Vendor product {request.vendor_product_id} not found")

        order_item = OrderItem(
            order_id=order.id,
            product_id=request.vendor_product_id,
            quantity=request.quantity,
            price=vp.price
        )

        # Add order item and update order total
        uow.order_items.add(order_item)
        order.total_amount = (order.total_amount or Decimal("0.00")) + order_item.sub_total
        uow.commit()

        return order_item


def find_order_item_product(customer_id: int, order_item_id: int, uow: AbstractUnitOfWork) -> OrderItem:
    with uow:
        order_item = uow.order_items.get(order_item_id)
        if not order_item:
            raise HTTPException(status_code=404, detail=f"OrderItem {order_item_id} not found")

        order = uow.orders.get(order_item.order_id)
        if order.customer_id != customer_id:
            raise HTTPException(status_code=403, detail="Unauthorized access to this order item")

        return order_item


def find_all_order_items_by_order(customer_id: int, order_id: int, uow: AbstractUnitOfWork) -> List[OrderItem]:
    with uow:
        order = uow.orders.get(order_id)
        if not order:
            raise HTTPException(status_code=404, detail="Order not found")

        if order.customer_id != customer_id:
            raise HTTPException(status_code=403, detail="Unauthorized access to this order")

        return uow.order_items.list_by_order_id(order_id)


def decrease_stock_by_order_item(order_item: OrderItem, uow: AbstractUnitOfWork):
    with uow:
        vp = uow.vendor_products.get(order_item.product_id)
        if vp.stock < order_item.quantity:
            raise HTTPException(
                status_code=400,
                detail=f"Vendor product {vp.product_id} has insufficient stock"
            )
        vp.stock -= order_item.quantity
        uow.vendor_products.update(vp)
        uow.commit()
        return vp


def delete_order_item(customer_id: int, order_item_id: int, uow: AbstractUnitOfWork):
    with uow:
        order_item = uow.order_items.get(order_item_id)
        if not order_item:
            raise HTTPException(status_code=404, detail=f"OrderItem {order_item_id} not found")

        order = uow.orders.get(order_item.order_id)
        if order.customer_id != customer_id:
            raise HTTPException(status_code=403, detail="Unauthorized access to this order item")

        uow.order_items.delete(order_item)
        uow.commit()
