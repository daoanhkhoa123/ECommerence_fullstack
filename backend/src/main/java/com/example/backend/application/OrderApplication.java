package com.example.backend.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.config.JwtService;
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
import com.example.backend.service.AuthencationService;
import com.example.backend.service.OrderItemService;
import com.example.backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderApplication {
    private final JwtService jwtService;
    private final OrderProducer orderProducer;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final AuthencationService authencationService;

    private OrderRespond buidFromOrder(Order order)
    {
        return new OrderRespond(
                        order.getId(),
                        order.getCustomer().getId(),
                        order.getOrderStatus(),
                        order.getTotalAmount(),
                        order.getShippingAddress(),
                        order.getOrderTime());
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

    public List<OrderRespond> findOrdersByCustomerId(Integer customerId)
    {
        return orderService.findAllByCustomerId(customerId).stream()
        .map(this::buidFromOrder).toList();
    }

    public OrderRespond updateOrderStatus(Integer orderId, UpdateOrderStatusRequest request)
    {
        Integer customerId = authencationService.findCurrentCustomerId();
        Order order = orderService.updateOrderStatus(customerId, orderId, request);

        orderProducer.sendorderStatusUpdate(jwtService.getCurrentUserId(), 
            orderId, request.status());

        return buidFromOrder(order);
    }

    public List<OrderItemProductRespond> findAllOrderItemProductByOrderId(Integer orderId)
    {
        // No one need this event yeah?
        Integer customerId = authencationService.findCurrentCustomerId();
        
        List<OrderItemProductRespond> responds = orderItemService.
        findAllOrderItemProductByOrderId(customerId, orderId).stream()
        .map(this::buildFromOrderItem).toList();

        return responds;
    }

    public List<OrderItemProductRespond> findAllOrderItemProductInCart()
    {
        // No one need this event yeah?
        Integer customerId = authencationService.findCurrentCustomerId();

        Order cart = orderService.findCartByCustomerId(customerId);
        List<OrderItemProductRespond> respond = findAllOrderItemProductByOrderId(cart.getId());

        return respond;
    }

    public OrderItemProductRespond createOrderItemProduct(OrderItemProductRequest request)
    {   Integer customerId = jwtService.getCurrentUserId();
        OrderItem orderItem = orderItemService.createOrderItemProduct(customerId, request);

        orderProducer.sendOrderItemCreate(customerId, orderItem);

        return buildFromOrderItem(orderItem);
    }

    public OrderItemProductRespond findOrderItemProduct(Integer orderItemId)
    {
        // No one need this event yeah?
        Integer customerId = authencationService.findCurrentCustomerId();
        OrderItem orderItem = orderItemService.findOrderItemProduct(customerId, orderItemId);

        return buildFromOrderItem(orderItem);
    }

    public void deleteOrderItem(Integer orderItemId)
    {
        Integer customerId = authencationService.findCurrentCustomerId();

        orderProducer.sendOrderItemDelete(customerId, orderItemId);

        orderItemService.deleteOrderItem(customerId, orderItemId);
    }


}
