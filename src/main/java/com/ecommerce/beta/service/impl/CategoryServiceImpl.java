package com.ecommerce.beta.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Category;
import com.ecommerce.beta.repository.CategoryRepository;
import com.ecommerce.beta.service.CategoryService;

import javax.annotation.PostConstruct;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @PostConstruct
    public void migrateSlugs() {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getSlug() == null || category.getSlug().isEmpty()) {
                category.setSlug(category.getName().toLowerCase().replaceAll("\\s+", "-"));
                categoryRepository.save(category);
            }
        }
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(UUID uuid) {
        categoryRepository.deleteById(uuid);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void save(Category category) {
        // Generate slug if not set
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            String slug = category.getName().toLowerCase().replaceAll("\\s+", "-");
            // Ensure slug uniqueness
            int counter = 1;
            String originalSlug = slug;
            while (categoryRepository.findBySlug(slug) != null) {
                slug = originalSlug + "-" + counter++;
            }
            category.setSlug(slug);
        }
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(UUID uuid) {
        return categoryRepository.findById(uuid).orElse(null);
    }

    @Override
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
}