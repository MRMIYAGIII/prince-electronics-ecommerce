package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.beta.entity.Image;

public interface ImageRepository extends JpaRepository<Image, UUID> {
	
	@Query(value = "SELECT * FROM image WHERE product_id = :uuid", nativeQuery = true)
	List<Image> findByProductId(@Param("uuid") UUID uuid);

}
