package com.dailycodework.dreamshops.service.oder;

import com.dailycodework.dreamshops.model.Order;

public interface IOrderService {

    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
}
