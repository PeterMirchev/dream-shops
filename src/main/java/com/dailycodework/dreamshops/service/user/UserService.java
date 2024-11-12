package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found! Invalid User ID - %s".formatted(userId)));
    }

    @Override
    public User createUser(CreateUserRequest request) {

        User user = mapRequestToUser(request);

        return userRepository.save(user);
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

    private static User mapRequestToUser(CreateUserRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        return user;
    }
}
