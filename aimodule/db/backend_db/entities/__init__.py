from typing import Literal

AccountsColumns = Literal[
    "account_id",
    "email",
    "password_hash",
    "role"
]

CustomersColumns = Literal[
    "customer_id",
    "account_id",
    "full_name",
    "phone",
    "address",
    "birth_date"
]

VendorsColumns = Literal[
    "vendor_id",
    "account_id",
    "shop_name",
    "description",
    "phone"
]

ProductsColumns = Literal[
    "product_id",
    "name",
    "description",
    "category",
    "brand",
    "image_url"
]

VendorsProductsColumns = Literal[
    "vendor_product_id",
    "vendor_id",
    "product_id",
    "price",
    "stock",
    "sku",
    "is_featured"
]

OrdersColumns = Literal[
    "order_id",
    "customer_id",
    "order_status",
    "total_amount",
    "shipping_address",
    "placed_at"
]

OrdersStatus = Literal['pending', 
                       'paid', 
                       'shipped', 
                       'delivered', 
                       'cancelled']

OrdersItemsColumns = Literal[
    "order_item_id",
    "order_id",
    "vendor_product_id",
    "quantity",
    "price",
    "subtotal"
]

PaymentsColumns = Literal[
    "payment_id",
    "order_id",
    "payment_method",
    "payment_status",
    "transaction_ref",
    "paid_amount",
    "paid_at"
]
