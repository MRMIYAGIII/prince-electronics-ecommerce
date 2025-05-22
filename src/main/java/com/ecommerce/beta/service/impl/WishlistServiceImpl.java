package com.ecommerce.beta.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Product;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.entity.Wishlist;
import com.ecommerce.beta.repository.WishlistRepository;
import com.ecommerce.beta.service.ProductService;
import com.ecommerce.beta.service.WishlistService;
import com.ecommerce.beta.worker.UsernameProvider;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UsernameProvider usernameProvider;

    @Override
    public boolean addToWishlist(UUID productId) {
        UserInfo userInfo = usernameProvider.get();
        if (userInfo == null) {
            return false;
        }

        Product product = productService.getProduct(productId);
        if (product == null) {
            return false;
        }

        // Check if the product is already in the wishlist
        List<Wishlist> existingItems = wishlistRepository.findByUserInfo(userInfo);
        for (Wishlist item : existingItems) {
            if (item.getProductId().getUuid().equals(productId)) {
                return true; // Already in wishlist, no need to add again
            }
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserInfo(userInfo);
        wishlist.setProductId(product);
        wishlistRepository.save(wishlist);
        return true;
    }

    @Override
    public List<Wishlist> findByUser(UserInfo userInfo) {
        if (userInfo == null) {
            return Collections.emptyList();
        }
        return wishlistRepository.findByUserInfo(userInfo);
    }

    @Override
    public void deleteById(UUID uuid) {
        wishlistRepository.deleteById(uuid);
    }

    @Override
    public Wishlist findById(UUID uuid) {
        return wishlistRepository.findById(uuid).orElse(null);
    }
}