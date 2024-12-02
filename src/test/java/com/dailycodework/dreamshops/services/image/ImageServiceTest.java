package com.dailycodework.dreamshops.services.image;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.service.image.ImageService;
import com.dailycodework.dreamshops.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Tests for getImageById()")
    class GetImageByIdTest {

        @Test
        @DisplayName("Successfully get Image by id")
        void getImageById_thenSuccessfullyReturnImage() {

            Image image = new Image();
            image.setId(1L);
            image.setFileName("test.jpg");

            when(imageRepository.findById(1L))
                    .thenReturn(Optional.of(image));

            Image response = imageService.getImageById(1L);

            assertNotNull(response);
            assertEquals(response.getId(), image.getId());
            assertEquals(response.getFileName(), image.getFileName());
        }

        @Test
        @DisplayName("Throws Exception when is provided non existing image Id")
        void getImageById_thenThrowsResourceNotFoundException() {

            when(imageRepository.findById(anyLong()))
                    .thenThrow(ResourceNotFoundException.class);

            assertThatThrownBy(() -> imageService.getImageById(1L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Tests for deleteImageById()")
    class DeleteImageByIdTest {

        @Test
        @DisplayName("Successfully delete image by Id")
        void deleteImageById_thenSuccessfullyDeleteImage() {

            Image image = new Image();
            image.setId(1L);

            when(imageRepository.findById(image.getId()))
                    .thenReturn(Optional.of(image));

            imageService.deleteImageById(1L);

            verify(imageRepository, times(1)).delete(image);
        }
    }
}
