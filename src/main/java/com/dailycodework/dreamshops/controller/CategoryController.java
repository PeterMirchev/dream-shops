package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.category.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {

        List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(new ApiResponse("Found!", categories));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {

        Category category = categoryService.addCategory(name);

        return ResponseEntity.ok(new ApiResponse("Category successfully added", category));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {

        Category category = categoryService.getCategoryById(id);

        return ResponseEntity.ok(new ApiResponse("Found!", category));
    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {

        Category category = categoryService.getCategoryByName(name);

        return ResponseEntity.ok(new ApiResponse("Found!", category));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Category Deleted!", null));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {

        Category updatedCategory = categoryService.updateCategory(category, id);

        return ResponseEntity.ok(new ApiResponse("Successfully Updated Category!", updatedCategory));
    }
}
