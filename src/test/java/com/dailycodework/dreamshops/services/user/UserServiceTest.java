package com.dailycodework.dreamshops.services.user;

import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.service.cart.ICartService;
import com.dailycodework.dreamshops.service.user.UserService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private ICartService cartService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getUserById_returnUserWhenExists() {

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User persistedUser = userService.getUserById(1L);

        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getId()).isEqualTo(1L);
        assertThat(persistedUser.getFirstName()).isEqualTo("John");
        assertThat(persistedUser.getEmail()).isEqualTo("test@gmail.com");
    }

/*    @Test
    void getUserById_returnNullWhenUserDoesNotExist() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User user = userService.getUserById(1L);

        Assertions.assertNull(user);
    }*/
}
