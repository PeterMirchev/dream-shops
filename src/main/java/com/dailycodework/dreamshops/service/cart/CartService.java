package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart getCart(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Card Not Found! Invalid Card ID - %s", id)));

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {

        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {

        return getCart(id).getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {

        Cart cart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        cart.setId(newCartId);

        return cartRepository.save(cart).getId();
    }
}
