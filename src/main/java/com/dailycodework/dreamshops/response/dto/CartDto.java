package com.dailycodework.dreamshops.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}
