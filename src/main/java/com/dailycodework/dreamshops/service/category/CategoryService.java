package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.exception.ResourceAlreadyExistException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.dailycodework.dreamshops.service.ServiceMessages.*;

@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id)));
    }

    @Override
    public Category getCategoryByName(String name) {

        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CATEGORY_NAME_NOT_FOUND, name)));
    }

    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new ResourceAlreadyExistException(String.format(CATEGORY_ALREADY_EXIST, category.getName())));
    }

    @Override
    public Category updateCategory(Category category, Long id) {

        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id)));
    }

    @Override
    public void deleteCategoryById(Long id) {

        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id));
                        });
    }
}
