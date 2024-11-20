package com.dailycodework.dreamshops.response.dto;

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
    private CartDto cart;
    private List<OrderDto> orders;
}
