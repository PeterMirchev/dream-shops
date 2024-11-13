package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;

import java.util.List;

public class ProductMapper {

    public static ProductDto convertToProductDto(Product product) {
        List<ImageDto> imagesDto = imagesDto = product.getImages()
                .stream()
                .map(ProductMapper::convertToImageDto)
                .toList();


        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .price(product.getPrice())
                .inventory(product.getInventory())
                .description(product.getDescription())
                .category(product.getCategory())
                .images(imagesDto)
                .build();

    }

    private static ImageDto convertToImageDto(Image e) {

        return ImageDto.builder()
                .imageId(e.getId())
                .imageName(e.getFileName())
                .downloadUrl(e.getDownloadUrl())
                .build();
    }

    public static ProductDto convertPersistedProductToProductDto(Product product) {

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .price(product.getPrice())
                .inventory(product.getInventory())
                .description(product.getDescription())
                .category(product.getCategory())
                .build();
    }
}
