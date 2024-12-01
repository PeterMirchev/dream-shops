package com.dailycodework.dreamshops.services.category;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.service.category.CategoryService;
import org.hibernate.collection.spi.PersistentBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("Tests for getCategoryById()")
    class GetCategoryById {

        @Test
        @DisplayName("Test for successfully get category")
        void getCategoryById_thenReturnSuccessfullyCategory() {

            Long categoryId = 1L;
            Category category = new Category();
            category.setId(categoryId);
            category.setName("Test");

            when(categoryRepository.findById(categoryId))
                    .thenReturn(Optional.of(category));

            Category response = categoryService.getCategoryById(categoryId);

            verify(categoryRepository, times(1)).findById(categoryId);

            assertEquals(category.getName(), response.getName());
            assertEquals(category.getId(), response.getId());
        }

        @Test
        @DisplayName("Throws exception when is provided non existing categoryId")
        void getCategoryById_thenReturnResourceNotFoundException() {

            when(categoryRepository.findById(anyLong()))
                    .thenThrow(ResourceNotFoundException.class);

            assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
        }
    }

    @Nested
    @DisplayName("Tests for getCategoryByName()")
    class GetCategoryByNameTest {

        @Test
        @DisplayName("Successfully get category by name")
        void getCategoryByName_thenReturnSuccessfullyCategory() {

            String categoryName = "Test";
            Category category = new Category();
            category.setName(categoryName);
            category.setId(1L);

            when(categoryRepository.findByName(categoryName))
                    .thenReturn(Optional.of(category));

            Category response = categoryService.getCategoryByName(categoryName);

            verify(categoryRepository, times(1)).findByName(categoryName);

            assertEquals(category.getName(), response.getName());
            assertEquals(category.getId(), response.getId());
        }

        @Test
        @DisplayName("Throws exception when the category name is non existing")
        void getCategoryByName_thenThrowResourceNotFoundException() {

            when(categoryRepository.findByName(anyString()))
                    .thenThrow(ResourceNotFoundException.class);

            assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryByName(anyString()));
        }
    }

    @Nested
    @DisplayName("Tests for getAllCategories()")
    class GetAlCategoriesTest {

        @Test
        @DisplayName("Successfully get all categories")
        void getAllCategories() {
            List<Category> categories = new ArrayList<>();
            Category category1 = new Category();
            category1.setName("Test");
            category1.setId(1L);
            Category category2 = new Category();
            category2.setName("Test2");
            category2.setId(2L);
            categories.add(category1);
            categories.add(category2);

            when(categoryRepository.findAll())
                    .thenReturn(categories);

            List<Category> response = categoryService.getAllCategories();

            verify(categoryRepository, times(1)).findAll();

            assertEquals(categories.size(), response.size());
            assertEquals(categories.get(0).getId(), response.get(0).getId());
        }
    }

}
