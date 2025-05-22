package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.entity.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    List<Wishlist> findByUserInfo(UserInfo userInfo);
}