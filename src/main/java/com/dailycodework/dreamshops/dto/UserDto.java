package com.dailycodework.dreamshops.dto;

import com.dailycodework.dreamshops.model.Cart;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Cart cart;
    private List<OrderDto> orders;
}
