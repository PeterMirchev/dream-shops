package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.exception.ProductNotFoundException;
import com.dailycodework.dreamshops.exception.ResourceAlreadyExistException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.dailycodework.dreamshops.service.ServiceMessages.PRODUCT_ALREADY_EXIST;
import static com.dailycodework.dreamshops.service.ServiceMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product addProduct(AddProductRequest request) {

        String categoryName = request.getCategory().getName();

        if (productExists(request.getName(), request.getBrand())) {
            throw new ResourceAlreadyExistException((PRODUCT_ALREADY_EXIST.formatted(request.getName(), request.getBrand())));
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(categoryName))
                .orElseGet(() -> {
                    Category newCategory = new Category(categoryName);
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);

        return productRepository.save(createProduct(request, category));
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
    }

    @Override
    public void deleteProductById(Long id) {

        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                    throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                        });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {

        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {

        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getAllProductsByBrand(String brand) {

        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getAllProductsByCategoryAndBrand(String category, String brand) {

        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getAllProductsByName(String name) {

        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getAllProductsByBrandAndName(String brand, String name) {

        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


    @Override
    public Long count() {
        return productRepository.count();
    }

    @Override
    public Long countByBrand(String brand) {
        return productRepository.countByBrand(brand);
    }

    @Override
    public Long countByCategoryName(String category) {
        return productRepository.countByCategoryName(category);
    }

    @Override
    public Long countByName(String name) {
        return productRepository.countByProductName(name);
    }

    @Override
    public Long countByCategoryAndBrand(String category, String brand) {
        return productRepository.countByCategoryAndBrand(category, brand);
    }

    @Override
    public Long countByBrandAndName(String brandName, String productName) {
        return productRepository.countByBrandAndName(brandName, productName);
    }

    private Product createProduct(AddProductRequest request, Category category) {

        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    private boolean productExists(String name, String brand) {

        return productRepository.existsByNameAndBrand(name, brand);
    }
}
