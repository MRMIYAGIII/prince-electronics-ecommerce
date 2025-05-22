package com.ecommerce.beta.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.beta.dto.ImageDTO;
import com.ecommerce.beta.dto.ProductDTO;
import com.ecommerce.beta.entity.Product;
import com.ecommerce.beta.service.ProductService;

@RestController
public class ApiController {

    private final ProductService productService;

    @Autowired
    public ApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products/filter")
    public @ResponseBody List<ProductDTO> filterProducts(@RequestParam String filter) {
        try {
            UUID categoryUuid = UUID.fromString(filter);
            List<Product> products = productService.findByCategoryUuid(categoryUuid)
                    .stream()
                    .filter(Product::isEnabled)
                    .collect(Collectors.toList());
            products.forEach(product -> System.out.println("API Product: " + product.getName() + ", Images: " + (product.getImages() != null ? product.getImages().size() : "null")));
            System.out.println("Filter: " + filter + ", Products found: " + products.size());
            return products.stream().map(product -> {
                ProductDTO dto = new ProductDTO();
                dto.setUuid(product.getUuid());
                dto.setName(product.getName());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setCategoryName(product.getCategory().getName());
                dto.setImages(product.getImages() != null ? product.getImages().stream().map(image -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setUuid(image.getUuid());
                    imageDTO.setFileName(image.getFileName());
                    return imageDTO;
                }).collect(Collectors.toList()) : Collections.emptyList());
                System.out.println("ProductDTO: " + dto.getName() + ", Images: " + (dto.getImages() != null ? dto.getImages().size() : "null"));
                return dto;
            }).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category UUID: " + filter);
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error in filterProducts: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}