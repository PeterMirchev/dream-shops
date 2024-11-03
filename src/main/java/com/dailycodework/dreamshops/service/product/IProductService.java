package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

import java.util.List;


public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product  updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getAllProductsByBrand(String brand);
    List<Product> getAllProductsByCategoryAndBrand(String category, String brand);
    List<Product> getAllProductsByName(String name);
    List<Product> getAllProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    Long count();

    Long countByBrand(String brand);

    Long countByCategoryName(String category);

    Long countByName(String name);

    Long countByCategoryAndBrand(String category, String brand);

    Long countByBrandAndName(String brandName, String productName);
}
