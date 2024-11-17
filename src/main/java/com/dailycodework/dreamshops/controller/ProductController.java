package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.response.dto.ProductDto;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;
import com.dailycodework.dreamshops.service.product.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {

        List<Product> products = productService.getAllProducts();
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .collect(Collectors.toList());

        Long count = productService.count();

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products: %s", count), response));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {

        ProductDto response = ProductMapper.convertToProductDto(productService.getProductById(productId));

        return ResponseEntity.ok(new ApiResponse("Success", response));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {

        Product product = productService.addProduct(productRequest);
        ProductDto response = ProductMapper.convertPersistedProductToProductDto(product);

        return ResponseEntity.ok(new ApiResponse("Product successfully added!", response));
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {

        Product product = productService.updateProduct(request, productId);
        ProductDto response = ProductMapper.convertToProductDto(product);

        return ResponseEntity.ok(new ApiResponse("Product Updated Successfully!", response));
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {

        productService.deleteProductById(productId);

        return ResponseEntity.ok(new ApiResponse("Product Successfully Deleted!", null));
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {

        Long count = productService.countByBrandAndName(brandName, productName);

        List<Product> products = productService.getAllProductsByBrandAndName(brandName, productName);
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse(String.format("Total %s Products with Brand %s: %s", productName, brandName, count), response));
    }

    @GetMapping("/by/category-and-brand/{category}/{brand}")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String category, @PathVariable String brand){

        Long count = productService.countByCategoryAndBrand(category, brand);

        List<Product> products = productService.getAllProductsByCategoryAndBrand(category, brand);
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Category and Brand - %s and %s: %s", category, brand, count), response));
    }

    @GetMapping("/by-name/{name}/product")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {

        Long count = productService.countByName(name);

        List<Product> products = productService.getAllProductsByName(name);
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Name - %s: %s",name, count), response));
    }

    @GetMapping("product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {

        Long count = productService.countByBrand(brand);

        List<Product> products = productService.getAllProductsByBrand(brand);
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Brand - %s: %s", brand, count), response));
    }

    @GetMapping("product/{category}/all/products")
    public ResponseEntity<ApiResponse> getAllProductByCategory(@PathVariable String category) {

        Long countByCategoryName = productService.countByCategoryName(category);

        List<Product> products = productService.getAllProductsByCategory(category);
        List<ProductDto> response = products
                .stream()
                .map(ProductMapper::convertToProductDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Category - %s: %s", category, countByCategoryName), response));
    }
}
