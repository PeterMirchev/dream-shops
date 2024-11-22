package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductQuantityUpdate {

    @NotNull
    private String name;
    @NotNull
    private String brand;
    @NotNull
    private int quantity;

}
