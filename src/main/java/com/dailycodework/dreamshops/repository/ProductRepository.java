package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    @Query("""
    SELECT COUNT(*) FROM Product p
    WHERE p.brand = :brand
    AND p.name = :name
        """)
    Long countByBrandAndName(String brand, String name);

    @Query("""
    SELECT COUNT(*) FROM Product p
    WHERE p.brand = :brand
        """)
    Long countByBrand(String brand);
    @Query("""
    SELECT COUNT(*) FROM Product p
    WHERE p.category.name = :category
        """)
    Long countByCategoryName(String category);
    @Query("""
    SELECT COUNT(*) FROM Product p
    WHERE p.name = :name
        """)
    Long countByProductName(String name);
    @Query("""
    SELECT COUNT(*) FROM Product p
    WHERE p.category.name = :category
    AND p.brand =:brand
        """)
    Long countByCategoryAndBrand(String category, String brand);
}
