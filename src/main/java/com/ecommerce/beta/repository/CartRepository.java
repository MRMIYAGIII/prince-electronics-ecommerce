package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.Cart;
import com.ecommerce.beta.entity.UserInfo;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
	
    List<Cart> findByUserInfo(UserInfo userInfo);
}
