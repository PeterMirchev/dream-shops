package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
    SELECT c FROM Category c
    WHERE c.name = :name
    """)
    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}
