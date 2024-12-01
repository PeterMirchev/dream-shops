package com.dailycodework.dreamshops.services.product;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.service.cart.CartService;
import com.dailycodework.dreamshops.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("Tests for getUserById()")
    class GetUserByIdTest {

        @Test
        @DisplayName("Get user successfully")
        void getUserById_successfully() {

            User user = new User();
            user.setFirstName("Pesho");
            user.setLastName("Peshov");
            user.setId(1L);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            User response = userService.getUserById(1L);

            assertEquals(response.getId(), user.getId());
            assertEquals(response.getFirstName(), user.getFirstName());
            assertEquals(response.getLastName(), user.getLastName());
        }

        @Test
        @DisplayName("Throws exception when provide non existing user id")
        void getUserById_nonExistingUserIdThrowsException() {

            when(userRepository.findById(1L))
                    .thenThrow(ResourceNotFoundException.class);

            assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        }
    }
}
