# src/domain/entities/product.py
from dataclasses import dataclass
from decimal import Decimal
from typing import Optional

from src.allocation.domain.entities.vendor import Vendor


@dataclass
class Product:
    # Flattened identifiers
    vendor_product_id: Optional[int] = None
    product_id: Optional[int] = None
    vendor: Vendor = Vendor()

    # Product details
    name: str = ""
    description: Optional[str] = None
    brand: Optional[str] = None
    image_url: Optional[str] = None

    # Vendor-specific details
    price: Decimal = Decimal("0.00")
    stock: Optional[int] = None
    sku: Optional[str] = None
    is_featured: bool = False

    