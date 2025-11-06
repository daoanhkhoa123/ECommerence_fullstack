package com.example.backend.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.backend.dto.OrderItemProductRequest;
import com.example.backend.dto.OrderItemProductRespond;
import com.example.backend.dto.OrderRespond;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Product;
import com.example.backend.entity.Vendor;
import com.example.backend.entity.VendorProduct;
import com.example.backend.kafka.producer.OrderProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.AuthencationService;
import com.example.backend.service.OrderItemService;
import com.example.backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

    private final JwtService jwtService;
    private final OrderProducer orderProducer;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final AuthencationService authencationService;

    private OrderRespond buildFromOrder(Order order) {
        return new OrderRespond(
            order.getId(),
            order.getCustomer().getId(),
            order.getOrderStatus(),
            order.getTotalAmount(),
            order.getShippingAddress(),
            order.getOrderTime()
        );
    }

    private OrderItemProductRespond buildFromOrderItem(OrderItem orderItem) {
        VendorProduct vp = orderItem.getVendorProduct();
        Vendor v = vp.getVendor();
        Product p = vp.getProduct();

        return new OrderItemProductRespond(
            orderItem.getId(),
            vp.getId(),
            orderItem.getQuantity(),
            orderItem.getSubTotal(),
            v.getShopName(),
            v.getPhone(),
            p.getName(),
            p.getBrand(),
            p.getImageUrl()
        );
    }

    public List<OrderRespond> findOrdersByCustomerId(Integer customerId) {
        log.info("Fetching all orders for customer id: {}", customerId);
        List<Order> orders = orderService.findAllByCustomerId(customerId);
        log.info("Found {} orders for customer id: {}", orders.size(), customerId);

        return orders.stream().map(this::buildFromOrder).toList();
    }

    public OrderRespond updateOrderStatus(Integer orderId, UpdateOrderStatusRequest request) {
        Integer customerId = authencationService.findCurrentCustomerId();
        log.info("Updating order status for order id: {} by customer id: {}", orderId, customerId);

        Order order = orderService.updateOrderStatus(customerId, orderId, request);
        log.info("Order service updated status for order id: {} -> {}", orderId, order.getOrderStatus());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("Sending Kafka event for order status update by actor: {}", actorId);
        orderProducer.sendOrderStatusUpdate(actorId, orderId, request.status());
        log.info("Kafka event sent for order status update: orderId={}, status={}", orderId, request.status());

        return buildFromOrder(order);
    }

    public List<OrderItemProductRespond> findAllOrderItemProductByOrderId(Integer orderId) {
        Integer customerId = authencationService.findCurrentCustomerId();
        log.info("Fetching all order items for order id: {} and customer id: {}", orderId, customerId);

        List<OrderItem> items = orderItemService.findAllOrderItemProductByOrderId(customerId, orderId);
        log.info("Found {} order items for order id: {}", items.size(), orderId);

        return items.stream().map(this::buildFromOrderItem).toList();
    }

    public List<OrderItemProductRespond> findAllOrderItemProductInCart() {
        Integer customerId = authencationService.findCurrentCustomerId();
        log.info("Fetching all items in cart for customer id: {}", customerId);

        Order cart = orderService.findCartByCustomerId(customerId);
        log.info("Cart found with id: {}", cart.getId());

        List<OrderItemProductRespond> responses = findAllOrderItemProductByOrderId(cart.getId());
        log.info("Cart contains {} items", responses.size());
        return responses;
    }

    public OrderItemProductRespond createOrderItemProduct(OrderItemProductRequest request) {
        Integer customerId = authencationService.findCurrentCustomerId();
        log.info("Creating order item for customer id: {} with vendorProductId: {}", customerId, request.vendorProductId());

        OrderItem orderItem = orderItemService.createOrderItemProduct(customerId, request);
        log.info("Order item created successfully in service layer: id={}, subtotal={}",
                 orderItem.getId(), orderItem.getSubTotal());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("Sending Kafka event for order item create by actor: {}", actorId);
        orderProducer.sendOrderItemCreate(actorId, orderItem);
        log.info("Kafka event sent for order item create: id={}", orderItem.getId());

        return buildFromOrderItem(orderItem);
    }

    public OrderItemProductRespond findOrderItemProduct(Integer orderItemId) {
        Integer customerId = authencationService.findCurrentCustomerId();
        log.info("Fetching order item id: {} for customer id: {}", orderItemId, customerId);

        OrderItem orderItem = orderItemService.findOrderItemProduct(customerId, orderItemId);
        log.info("Order item found: id={}, quantity={}, subtotal={}",
                 orderItem.getId(), orderItem.getQuantity(), orderItem.getSubTotal());

        return buildFromOrderItem(orderItem);
    }

    public void deleteOrderItem(Integer orderItemId) {
        Integer customerId = authencationService.findCurrentCustomerId();
        Integer actorId = jwtService.getCurrentUserId();

        log.info("Deleting order item id: {} by actor: {}", orderItemId, actorId);
        orderProducer.sendOrderItemDelete(actorId, orderItemId);
        log.info("Kafka event sent for order item delete: id={}", orderItemId);

        orderItemService.deleteOrderItem(customerId, orderItemId);
        log.info("Order item id: {} deleted successfully from database", orderItemId);
    }
}
