package com.dailycodework.dreamshops.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
