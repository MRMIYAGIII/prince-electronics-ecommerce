package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.beta.entity.Category;
import com.ecommerce.beta.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	List<Product> findByNameLike(String key);

	Page<Product> findByNameLike(String key, Pageable pageable);

	Page<Product> findByNameContainingAndEnabledIsTrue(String key, Pageable pageable);

	Page<Product> findAllByEnabledTrue(Pageable pageable);

	List<Product> findAllByEnabledTrue();

	Page<Product> findByCategory(Category category, Pageable pageable);

	@Query("SELECT p FROM Product p JOIN FETCH p.images WHERE p.category.uuid = :categoryUuid AND p.enabled = true")
	List<Product> findByCategoryUuid(@Param("categoryUuid") UUID categoryUuid);

	// Updated method with separate count query
	@Query(
			value = "SELECT p FROM Product p JOIN FETCH p.images WHERE p.category.uuid = :categoryUuid AND p.enabled = true",
			countQuery = "SELECT COUNT(p) FROM Product p WHERE p.category.uuid = :categoryUuid AND p.enabled = true"
	)
	Page<Product> findByCategoryUuid(@Param("categoryUuid") UUID categoryUuid, Pageable pageable);
}