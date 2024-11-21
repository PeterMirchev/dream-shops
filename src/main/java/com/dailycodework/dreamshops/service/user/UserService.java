package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.exception.ResourceAlreadyExistException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import com.dailycodework.dreamshops.service.cart.ICartService;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ICartService cartService;

    public UserService(UserRepository userRepository, ICartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @Override
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found! Invalid User ID - %s".formatted(userId)));
    }

    @Override
    public User createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistException("Email Already Registered Within User!\n Email - %s".formatted(request.getEmail()));
        }

        User user = UserMapper.mapToUser(request);

        User persistedUser = userRepository.save(user);

        cartService.initializeNewCart(persistedUser);

        return userRepository.save(persistedUser);
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {

        User user = getUserById(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {

        userRepository.findById(userId)
                .ifPresentOrElse(userRepository :: delete,
                        () -> {
                            throw new ResourceNotFoundException("User Not Found! Invalid User ID - %s".formatted(userId));
                        } );
    }
}
