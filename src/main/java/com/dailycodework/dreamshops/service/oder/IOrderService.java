package com.dailycodework.dreamshops.service.oder;

import com.dailycodework.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
