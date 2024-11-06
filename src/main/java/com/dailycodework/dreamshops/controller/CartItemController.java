package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;

    public CartItemController(ICartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {

        cartItemService.addItemToCart(cartId, productId, quantity);

        return ResponseEntity.ok(new ApiResponse("Add Item Success!", null));
    }

    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {

        cartItemService.removeItemFromCart(cartId, productId);

        return ResponseEntity.ok(new ApiResponse("Item Removed Successfully!", null));
    }
}
