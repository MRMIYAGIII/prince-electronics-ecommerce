package com.ecommerce.beta.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Category;
import com.ecommerce.beta.repository.CategoryRepository;

@Service
public interface CategoryService {

    Category getCategory(UUID uuid);

    List<Category> findAll();

    void addCategory(Category category);

    void delete(UUID uuid);

    void updateCategory(Category category);

    void save(Category category);

    // New method to find by slug
    Category findBySlug(String slug);
}