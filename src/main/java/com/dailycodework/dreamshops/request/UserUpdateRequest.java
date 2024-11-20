package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class UserUpdateRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
