package com.dailycodework.dreamshops.service.oder;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.dto.OrderItemDto;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {

        List<OrderItemDto> items = order
                .getOrderItems()
                .stream()
                .map(OrderMapper::mapToOrderItemDto)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getOrderId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(String.valueOf(order.getOrderStatus()))
                .items(items)
                .build();
    }

    public static OrderItemDto mapToOrderItemDto(OrderItem item) {

        return OrderItemDto.builder()
                .productId(item.getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}
