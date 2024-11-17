package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.response.dto.CartDto;
import com.dailycodework.dreamshops.response.dto.CartItemDto;
import com.dailycodework.dreamshops.service.product.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartMapper {


    public static CartDto mapToCartDto(Cart cart) {

        Set<CartItemDto> items = cart.getItems().stream()
                .map(CartMapper::mapToCartItemDto)
                .collect(Collectors.toSet());

        return CartDto.builder()
                .cartId(cart.getId())
                .items(items)
                .build();
    }


    public static CartItemDto mapToCartItemDto(CartItem cartItem) {

        return CartItemDto.builder()
                .itemId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .product(ProductMapper.convertToProductDto(cartItem.getProduct()))
                .build();
    }
}