package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotNull
    private String firstName;
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
