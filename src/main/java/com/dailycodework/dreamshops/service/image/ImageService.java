package com.dailycodework.dreamshops.service.image;

import com.dailycodework.dreamshops.response.dto.ImageDto;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.dailycodework.dreamshops.service.ServiceMessages.DOWNLOAD_URL;
import static com.dailycodework.dreamshops.service.ServiceMessages.INVALID_IMAGE_ID;

@Service
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;

    public ImageService(ImageRepository imageRepository, ProductService productService) {
        this.imageRepository = imageRepository;
        this.productService = productService;
    }

    @Override
    public Image getImageById(Long id) {

        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INVALID_IMAGE_ID, id)));
    }

    @Override
    public void deleteImageById(Long id) {

        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete,
                        () ->  {
                    throw new ResourceNotFoundException(String.format(INVALID_IMAGE_ID, id));
                        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {

        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {

            convertImageToPersist(product, savedImageDtos, file);
        }
        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        Image image = imageRepository.findById(imageId).get();

        try {
            image.setFileType(file.getContentType());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            String downloadUrl = DOWNLOAD_URL + image.getId();
            image.setDownloadUrl(downloadUrl);
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void convertImageToPersist(Product product, List<ImageDto> savedImageDtos, MultipartFile file) {

        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setProduct(product);
            Image savedImage = imageRepository.save(image);

            savedImage.setDownloadUrl(DOWNLOAD_URL + savedImage.getId());
            imageRepository.save(savedImage);

            ImageDto imageDto = mapImageDto(savedImage);

            savedImageDtos.add(imageDto);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static ImageDto mapImageDto(Image savedImage) {

        ImageDto imageDto = new ImageDto();
        imageDto.setImageId(savedImage.getId());
        imageDto.setImageName(savedImage.getFileName());
        imageDto.setDownloadUrl(savedImage.getDownloadUrl());
        return imageDto;
    }
}
