package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartItemService;
import com.dailycodework.dreamshops.service.cart.ICartService;
import com.dailycodework.dreamshops.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    public CartItemController(ICartItemService cartItemService, ICartService cartService, IUserService userService) {

        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                     @RequestParam Integer quantity) {


        User user = userService.getUserById(1L);
        Cart cart = cartService.initializeNewCart(user);


        cartItemService.addItemToCart(cart.getId(), productId, quantity);

        return ResponseEntity.ok(new ApiResponse("Add Item Success!", null));
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {

        cartItemService.removeItemFromCart(cartId, itemId);

        return ResponseEntity.ok(new ApiResponse("Item Removed Successfully!", null));
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {

        cartItemService.updateItemQuantity(cartId, itemId, quantity);

        return ResponseEntity.ok(new ApiResponse("Update Item Success!", null));
    }
}
