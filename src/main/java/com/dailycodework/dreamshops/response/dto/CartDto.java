package com.dailycodework.dreamshops.response.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CartDto {

    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}
