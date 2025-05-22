package com.ecommerce.beta.service;

import java.util.List;
import java.util.UUID;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.entity.Wishlist;

public interface WishlistService {
    boolean addToWishlist(UUID productId);
    List<Wishlist> findByUser(UserInfo userInfo);
    Wishlist findById(UUID uuid);
    void deleteById(UUID uuid);
}