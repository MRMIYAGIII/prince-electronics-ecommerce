package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.Product;
import com.ecommerce.beta.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
	
    @Query(value = "SELECT * FROM variant WHERE product_id = :uuid", nativeQuery = true)
    List<Variant> findVariantsByProductId(@Param("uuid") UUID uuid);

    Page<Variant> findByProductId(Product product, Pageable pageable);

    Page<Variant> findByNameLike(String keyword, Pageable pageable);
}
