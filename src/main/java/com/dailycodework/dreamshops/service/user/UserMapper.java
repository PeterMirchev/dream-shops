package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.service.oder.OrderMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {

        List<OrderDto> orders = user.getOrders()
                .stream()
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .cart(user.getCart())
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
