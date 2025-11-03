package com.example.backend.kafka.enums;

import lombok.Getter;

@Getter
public enum KafkaTopic {

    // Customer topics
    CUSTOMER_READ("customer.read.v1"),
    CUSTOMER_CREATE("customer.create.v1"),
    CUSTOMER_UPDATE("customer.update.v1"),
    CUSTOMER_DELETE("customer.delete.v1"),

    // Vendor topics
    VENDOR_READ("vendor.read.v1"),
    VENDOR_CREATE("vendor.create.v1"),
    VENDOR_UPDATE("vendor.update.v1"),
    VENDOR_DELETE("vendor.delete.v1"),

    // Category topics
    CATEGORY_READ("category.read.v1"),
    CATEGORY_CREATE("category.create.v1"),
    CATEGORY_UPDATE("category.update.v1"),
    CATEGORY_DELETE("category.delete.v1"),

    // Product category topics
    PRODUCT_CATEGORY_CREATE("product-category.create.v1"),
    PRODUCT_CATEGORY_DELETE("product-category.delete.v1"),

    // Order
    ORDER_STATUS_UPDATED("order-status.updated"),

    // Order item topics
    ORDER_ITEM_CREATE("order-item.create.v1"),
    ORDER_ITEM_DELETE("order-item.delete.v1"),

    // Cart topics
    CART_PAY("cart.pay.v1"),

    // Vendor product topics
    VENDOR_PRODUCT_READ("vendor-product.read.v1"),
    VENDOR_PRODUCT_CREATE("vendor-product.create.v1"),
    VENDOR_PRODUCT_UPDATE("vendor-product.update.v1"),
    VENDOR_PRODUCT_DELETE("vendor-product.delete.v1");

    private final String name;

    KafkaTopic(String name) {
        this.name = name;
    }
}
