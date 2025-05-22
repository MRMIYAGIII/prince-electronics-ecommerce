package com.ecommerce.beta.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Product;
import com.ecommerce.beta.repository.ProductRepository;
import com.ecommerce.beta.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid).orElse(null);
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.deleteById(uuid);
    }

    @Override
    public List<Product> findByNameLike(String key) {
        return productRepository.findByNameLike(key);
    }

    @Override
    public Page<Product> findByNameLike(String key, Pageable pageable) {
        return productRepository.findByNameLike(key, pageable);
    }

    @Override
    public Page<Product> findByCategory(String filter, Pageable pageable) {
        return productRepository.findByCategoryUuid(UUID.fromString(filter), pageable);
    }

    @Override
    public Page<Product> findAllPaged(Pageable pageable) {
        return productRepository.findAllByEnabledTrue(pageable);
    }

    @Override
    public Page<Product> findByNameLikePaged(String key, Pageable pageable) {
        return productRepository.findByNameContainingAndEnabledIsTrue(key, pageable);
    }

    @Override
    public List<Product> findAllEnabled() {
        return productRepository.findAllByEnabledTrue();
    }

    @Override
    public List<Product> findByCategoryUuid(UUID categoryUuid) {
        return productRepository.findByCategoryUuid(categoryUuid);
    }

    @Override
    public Page<Product> findByCategoryUuid(UUID categoryUuid, Pageable pageable) {
        return productRepository.findByCategoryUuid(categoryUuid, pageable);
    }
}