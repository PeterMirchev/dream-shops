package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotNull(message = "first name required")
    private String firstName;
    @NotNull(message = "last name required")
    private String lastName;
    @NotNull
    @Email(message = "invalid Email input")
    private String email;
    @NotNull(message = "password required")
    private String password;

    public CreateUserRequest(String firstName, String lastName, String email, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

}
