package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.response.dto.OrderDto;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.oder.IOrderService;
import com.dailycodework.dreamshops.service.oder.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {

        Order order = orderService.placeOrder(userId);
        OrderDto response = OrderMapper.mapToOrderDto(order);

        return ResponseEntity.ok(new ApiResponse("Item Order Success!", response));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {

        Order order = orderService.getOrder(orderId);
        OrderDto response = OrderMapper.mapToOrderDto(order);

        return ResponseEntity.ok(new ApiResponse("Order:", response));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {

        List<Order> orders = orderService.getUserOrders(userId);

        List<OrderDto> response = orders
                .stream().map(OrderMapper::mapToOrderDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse("Total User Orders: %s".formatted(orders.size()), response));
    }

}
