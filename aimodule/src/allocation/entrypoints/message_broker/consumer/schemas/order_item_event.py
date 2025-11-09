from decimal import Decimal

from pydantic import BaseModel, Field

class OrderItemEvent(BaseModel):
    account_id: int = Field(..., alias="accountId")
    customer_id: int = Field(..., alias="customerId")
    order_id: int = Field(..., alias="orderId")

    class Config:
        allow_population_by_field_name = True


class OrderItemCreateEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    order_id: int = Field(..., alias="orderId")
    order_item_id: int = Field(..., alias="orderItemId")
    vendor_product_id: int = Field(..., alias="vendorProductId")

    quantity: int
    sub_total: Decimal = Field(..., alias="subTotal")

    shop_name: str = Field(..., alias="shopName")
    shop_phone: str = Field(..., alias="shopPhone")

    product_name: str = Field(..., alias="productName")
    product_brand: str = Field(..., alias="productBrand")

    class Config:
        allow_population_by_field_name = True


class OrderItemDeleteEvent(BaseModel):
    actor_id: int = Field(..., alias="actorId")
    order_item_id: int = Field(..., alias="orderItemId")

    class Config:
        allow_population_by_field_name = True

