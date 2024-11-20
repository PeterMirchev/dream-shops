package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.response.dto.CartDto;
import com.dailycodework.dreamshops.response.dto.OrderDto;
import com.dailycodework.dreamshops.response.dto.UserDto;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.service.cart.CartMapper;
import com.dailycodework.dreamshops.service.oder.OrderMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {

        List<OrderDto> orders;

        if (user.getOrders() == null) {
            orders = new ArrayList<>();
        } else {
            orders = user.getOrders()
                    .stream()
                    .map(OrderMapper::mapToOrderDto)
                    .collect(Collectors.toList());
        }

        CartDto cart = CartMapper.mapToCartDto(user.getCart());

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .cart(cart)
                .orders(orders)
                .build();
    }

    public static User mapToUser(CreateUserRequest request) {

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
