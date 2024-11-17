package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.response.dto.UserDto;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.user.IUserService;
import com.dailycodework.dreamshops.service.user.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/users")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {

        User user = userService.getUserById(userId);
        UserDto response = UserMapper.mapToUserDto(user);

        return ResponseEntity.ok(new ApiResponse("Success!", response));
    }

    @PostMapping("/create/users")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {

        User user = userService.createUser(request);
        UserDto response = UserMapper.mapToUserDto(user);

        return ResponseEntity.ok(new ApiResponse("Successfully Created User: ", response));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {

        User user = userService.updateUser(request, userId);
        UserDto response = UserMapper.mapToUserDto(user);

        return ResponseEntity.ok(new ApiResponse("Successfully Updated User: ", response));
    }

    @DeleteMapping("{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok(new ApiResponse("User Deleted Successfully ", null));
    }
}
