package com.dailycodework.dreamshops.services.user;

import com.dailycodework.dreamshops.exception.ResourceAlreadyExistException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import com.dailycodework.dreamshops.service.cart.ICartService;
import com.dailycodework.dreamshops.service.user.UserMapper;
import com.dailycodework.dreamshops.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private ICartService cartService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private Cart cart;

    @Mock
    private UserMapper userMapper;

    private User user;

    private CreateUserRequest createUserRequest;


    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        createUserRequest = new CreateUserRequest("John",
                "Doe",
                "test@gmail.com",
                "123456");
    }

    @Nested
    @DisplayName("Tests for method getUserById()")
    class GetUserByIdTest {
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

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user does not exist")
        void getUserById_returnNullWhenUserDoesNotExist() {
            when(userRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User Not Found! Invalid User ID - 1");
        }
    }

    @Nested
    @DisplayName("Tests for method createUser()")
    class CreateUserTest {

        @Test
        @DisplayName("Should create user successfully")
        void createUser_shouldCreateUserSuccessfully() {

            CreateUserRequest request = new CreateUserRequest(
                    "John",
                    "Doe",
                    "ttest@gmail.com",
                    "123456"
            );

            User userToPersist = new User();
            userToPersist.setFirstName("John");
            userToPersist.setLastName("Doe");
            userToPersist.setEmail("test@gmail.com");
            userToPersist.setPassword("123456");

            when(userRepository.findByEmail(request.getEmail()))
                    .thenReturn(null);

            passwordEncoder.encode(userToPersist.getPassword());

            User persistedUser = userRepository.save(userToPersist);

            cartRepository.save(cart);

            verify(passwordEncoder, times(1)).encode(userToPersist.getPassword());
            verify(cartService, times(1)).initializeNewCart(userToPersist);

            assertEquals(persistedUser.getFirstName(), request.getFirstName());
        }

        @Test
        @DisplayName("Should throw error due to existing email")
        void createUser_throwEmailExists() {
            when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> userService.createUser(createUserRequest))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessageContaining("Email Already Registered Within User")
                    .hasMessageContaining("test@gmail.com");
        }
    }

    @Nested
    @DisplayName("Tests for method updateUser()")
    class UpdateUserTest {

        @Test
        @DisplayName("Should update user successfully")
        void updateUser_updateUserSuccessfully() {

            Long userId = 1L;
            User existingUser = new User();
            existingUser.setId(userId);
            existingUser.setFirstName("John");
            existingUser.setLastName("Doe");

            UserUpdateRequest request = new UserUpdateRequest();
            request.setFirstName("Gogo");
            request.setLastName("Peshev");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User updatedUser = userService.updateUser(request, userId);

            assertThat(updatedUser).isNotNull();
            assertThat(updatedUser.getFirstName()).isEqualTo("Gogo");
            assertThat(updatedUser.getLastName()).isEqualTo("Peshev");
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        @DisplayName("Should throw error for user not found")
        void updateUser_throwUserNotFound() {

            Long invalidUserId = 1L;
            UserUpdateRequest request = new UserUpdateRequest();
            request.setFirstName("Gogo");
            request.setLastName("Peshev");

            when(userRepository.findById(invalidUserId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(request, invalidUserId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User Not Found! Invalid User ID - 1");
        }
    }

    @Nested
    @DisplayName("Tests for method deleteUser()")
    class DeleteUserTest {

        @Test
        @DisplayName("Successfully delete user")
        void deleteUser_deleteUserSuccessfully() {

            Long userId = 1L;
            User existingUser = new User();
            existingUser.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            userService.deleteUser(userId);
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).delete(existingUser);
        }

        @Test
        @DisplayName("Should throw error when try to delete user with invalid Id")
        void deleteUser_thenThrowError() {

            Long userId = 1L;

            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User Not Found! Invalid User ID - %s".formatted(userId));
        }
    }
}
