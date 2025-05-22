package com.ecommerce.beta.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.beta.dto.CouponValidityResponseDto;
import com.ecommerce.beta.entity.Cart;
import com.ecommerce.beta.entity.UserInfo;

@Service
public interface CartService {
	
    List<Cart> findByUser(UserInfo userInfo);

    Cart save(Cart newCartItem);

    void delete(Cart cart);

    void deleteCartById(UUID uuid);

    Cart findCartByUuid(UUID uuid);

    boolean addToCart(UUID productId, int qty);

	CouponValidityResponseDto checkCouponValidity();

}
