package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Long count = productService.count();

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products: %s", count), products));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {

        Product product = productService.getProductById(productId);

        return ResponseEntity.ok(new ApiResponse("Success", product));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {

        Product product = productService.addProduct(productRequest);

        return ResponseEntity.ok(new ApiResponse("Product successfully added!", product));
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {

        Product product = productService.updateProduct(request, productId);

        return ResponseEntity.ok(new ApiResponse("Product Updated Successfully!", product));
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {

        productService.deleteProductById(productId);

        return ResponseEntity.ok(new ApiResponse("Product Successfully Deleted!", null));
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {

        List<Product> products = productService.getAllProductsByBrandAndName(brandName, productName);

        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/by/category-and-brand/{category}/{brand}")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String category, @PathVariable String brand){

        List<Product> products = productService.getAllProductsByCategoryAndBrand(category, brand);
        Long count = productService.countByCategoryAndBrand(category, brand);
        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Category and Brand - %s and %s: %s", category, brand, count), products));
    }

    @GetMapping("/by-name/{name}/product")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {

        List<Product> products = productService.getAllProductsByName(name);
        Long count = productService.countByName(name);

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Name - %s: %s",name, count), products));
    }

    @GetMapping("product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {

        List<Product> products = productService.getAllProductsByBrand(brand);

        Long count = productService.countByBrand(brand);

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Brand - %s: %s", brand, count),products));
    }

    @GetMapping("product/{category}/all/products")
    public ResponseEntity<ApiResponse> getAllProductByCategory(@PathVariable String category) {

        List<Product> products = productService.getAllProductsByCategory(category);
        Long countByCategoryName = productService.countByCategoryName(category);

        return ResponseEntity.ok(new ApiResponse(String.format("Total Products with Category - %s: %s", category, countByCategoryName), products));
    }
}
