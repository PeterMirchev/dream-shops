package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.response.dto.CartDto;
import com.dailycodework.dreamshops.service.cart.CartMapper;
import com.dailycodework.dreamshops.service.cart.ICartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}/my-cart")
    private ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {

        Cart cart = cartService.getCart(cartId);
        CartDto response = CartMapper.mapToCartDto(cart);

        return ResponseEntity.ok(new ApiResponse("Success:", response));
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {

        cartService.clearCart(cartId);

        return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {

        BigDecimal totalPrice = cartService.getCart(cartId).getTotalAmount();

        return ResponseEntity.ok(new ApiResponse("Total Price:", totalPrice));
    }
}
