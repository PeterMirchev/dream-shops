package com.dailycodework.dreamshops.services.image;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.response.dto.ImageDto;
import com.dailycodework.dreamshops.service.image.ImageService;
import com.dailycodework.dreamshops.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
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
        @DisplayName("Throws Exception when provided non-existing image ID")
        void getImageById_thenThrowsResourceNotFoundException() {
            // Arrange
            when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> imageService.getImageById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Invalid Image"); // Optional: Verify exception message
        }

    }

    @Nested
    @DisplayName("Tests for deleteImageById()")
    class DeleteImageByIdTest {

        @Test
        @DisplayName("Successfully delete image by Id")
        void deleteImageById_thenSuccessfullyDeleteImage() {
            // Arrange
            Image image = new Image();
            image.setId(1L);

            when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

            // Act
            imageService.deleteImageById(1L);

            // Assert
            verify(imageRepository, times(1)).findById(1L);
            verify(imageRepository, times(1)).delete(image);
        }

        @Test
        @DisplayName("Throws Exception when Image ID does not exist")
        void deleteImageById_thenThrowsResourceNotFoundException() {
            // Arrange
            when(imageRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> imageService.deleteImageById(1L))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(imageRepository, times(1)).findById(1L);
            verify(imageRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests for saveImage()")
    class SaveImageTest {

        @Test
        @DisplayName("Successfully save image")
        void saveImage_thenSuccessfullySaveImage() throws IOException {
            // Mock input files
            MultipartFile mockFile = mock(MultipartFile.class);
            when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
            when(mockFile.getContentType()).thenReturn("image/jpeg");
            when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
            List<MultipartFile> files = List.of(mockFile);

            // Mock product
            Product mockProduct = new Product();
            mockProduct.setId(1L);
            when(productService.getProductById(1L)).thenReturn(mockProduct);

            // Mock image repository save
            Image mockImage = new Image();
            mockImage.setId(1L);
            mockImage.setFileName("test.jpg");
            mockImage.setFileType("image/jpeg");
            when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

            // Execute the service method
            List<ImageDto> imageDtos = imageService.saveImage(files, 1L);

            // Verify interactions and assert results
            verify(productService, times(1)).getProductById(1L);
            verify(imageRepository, times(2)).save(any(Image.class)); // Initial save + download URL update

            assertNotNull(imageDtos);
            assertEquals(1, imageDtos.size());
            assertEquals("test.jpg", imageDtos.get(0).getImageName());
        }

    }

    @Nested
    @DisplayName("Tests for updateImage()")
    class UpdateImageTest {

        @Test
        @DisplayName("Successfully update image")
        void updateImage_thenSuccessfullyUpdateImage() throws IOException {
            // Arrange
            MultipartFile mockFile = mock(MultipartFile.class);
            when(mockFile.getOriginalFilename()).thenReturn("updated.jpg");
            when(mockFile.getContentType()).thenReturn("image/jpeg");
            when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

            Image mockImage = new Image();
            mockImage.setId(1L);
            mockImage.setFileName("original.jpg");

            when(imageRepository.findById(1L)).thenReturn(Optional.of(mockImage));

            // Act
            imageService.updateImage(mockFile, 1L);

            // Assert
            verify(imageRepository, times(1)).findById(1L);
            verify(imageRepository, times(1)).save(mockImage);

            assertEquals("updated.jpg", mockImage.getFileName());
            assertEquals("image/jpeg", mockImage.getFileType());
            assertNotNull(mockImage.getImage()); // Verifies that the image blob was set
            assertTrue(mockImage.getDownloadUrl().contains("1")); // Verifies the download URL includes the ID
        }

        @Test
        @DisplayName("Throws Exception when Image ID does not exist")
        void updateImage_thenThrowsResourceNotFoundException() {
            // Arrange
            when(imageRepository.findById(1L)).thenReturn(Optional.empty());

            MultipartFile mockFile = mock(MultipartFile.class);

            // Act & Assert
            assertThatThrownBy(() -> imageService.updateImage(mockFile, 1L))
                    .isInstanceOf(NoSuchElementException.class); // Adjust if a custom exception is used

            verify(imageRepository, times(1)).findById(1L);
            verify(imageRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throws RuntimeException on file processing errors")
        void updateImage_thenThrowsRuntimeExceptionOnErrors() throws Exception {

            MultipartFile mockFile = mock(MultipartFile.class);
            when(mockFile.getBytes()).thenThrow(new IOException("File processing error"));

            Image mockImage = new Image();
            mockImage.setId(1L);

            when(imageRepository.findById(1L)).thenReturn(Optional.of(mockImage));


            assertThatThrownBy(() -> imageService.updateImage(mockFile, 1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("File processing error");

            verify(imageRepository, times(1)).findById(1L);
            verify(imageRepository, never()).save(any());
        }
    }
}
