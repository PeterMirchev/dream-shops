package com.dailycodework.dreamshops.services.cart;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.service.cart.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Nested
    @DisplayName("Tests for getCart()")
    class GetCartTests {

        @Test
        void testGetCartById() {

            Long cartId = 1L;
            Cart expectedCart = new Cart();
            when(cartRepository.findById(cartId)).thenReturn(Optional.of(expectedCart));

            when(cartRepository.save(any()))
                    .thenReturn(expectedCart);

            Cart actualCart = cartService.getCart(cartId);

            assertThat(actualCart).isEqualTo(expectedCart);
        }

        @Test
        @DisplayName("Throw exception when is given invalid cart id")
        void getCart_throwExceptionWhenIsGivenInvalidCartId() {

            when(cartRepository.findById(anyLong()))
                    .thenThrow(ResourceNotFoundException.class);

            assertThrows(ResourceNotFoundException.class, () -> cartService.getCart(1L));
        }
    }

    @Nested
    @DisplayName("Tests for clearCart()")
    class ClearCartTests {

        @Test
        @DisplayName("clear cart")
        void clearCart_successfully() {

            cartService.clearCart(1L);

            verify(cartItemRepository, times(1)).deleteAllByCartId(1L);
            verify(cartRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Tests for initializeNewCart()")
    class InitializeNewCartTest {

        @Test
        @DisplayName("initialize new cart successfully")
        void initializeNewCart_successfully() {

            Cart cart = new Cart();
            User user = new User();
            user.setId(1L);

            when(cartRepository.findByUserId(1L))
                    .thenReturn(cart);

            Cart retrievedCart = cartService.initializeNewCart(user);

            assertEquals(cart, retrievedCart);
        }

        @Test
        @DisplayName("initialize new cart successfully")
        void initializeNewCart_createNewCart() {

            User user = new User();
            user.setId(1L);

            when(cartRepository.findByUserId(1L))
                    .thenReturn(null);

            cartService.initializeNewCart(user);

            verify(cartRepository, times(1)).save(any());
        }
    }
}
