package com.ecommerce.beta.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Product;
import com.ecommerce.beta.entity.Variant;
import com.ecommerce.beta.repository.VariantRepository;
import com.ecommerce.beta.service.ProductService;
import com.ecommerce.beta.service.VariantService;

@Service
public class VariantServiceImpl implements VariantService {
    @Autowired
    VariantRepository variantRepository;

    private final ProductService productService;

    public VariantServiceImpl(@Lazy ProductService productService) {
        this.productService = productService;
    }



    @Override
    public List<Variant> findAll() {
        return variantRepository.findAll();
    }

    @Override
    public Variant save(Variant variant) {
        return variantRepository.save(variant);
    }

    @Override
    public Variant findById(UUID uuid) {
        return variantRepository.findById(uuid).orElse(null);
    }

    @Override
    public void delete(UUID uuid) {
        variantRepository.deleteById(uuid);
    }

    @Override
    public Variant update(Variant variant) {
        return variantRepository.save(variant);
    }

    public List<Variant> findVariantsByProductId(UUID uuid){
        return variantRepository.findVariantsByProductId(uuid);
    }

    @Override
    public Page<Variant> findByProductPaged(String filter, Pageable pageable) {

        try {
            UUID uuid = UUID.fromString(filter);
            Product product = productService.getProduct(uuid);
            return variantRepository.findByProductId(product, pageable);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return Page.empty();
        }
    }

    @Override
    public Page<Variant> findByNameLike(String keyword, Pageable pageable) {
        return variantRepository.findByNameLike("%"+keyword+"%", pageable);
    }

    @Override
    public Page<Variant> findAllPaged(Pageable pageable) {
        return variantRepository.findAll(pageable);
    }
}
