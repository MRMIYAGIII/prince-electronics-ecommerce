package com.ecommerce.beta.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.beta.entity.Cart;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.entity.Variant;
import com.ecommerce.beta.service.CartService;
import com.ecommerce.beta.service.CouponService;
import com.ecommerce.beta.service.UserInfoService;
import com.ecommerce.beta.service.VariantService;

@Controller
@RequestMapping("/cart")
public class CartRestController {
	
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    CartService cartService;
    @Autowired
    VariantService variantService;    
    @Autowired
    CouponService couponService;

    @PostMapping("/add")
    public String addToCart(@RequestParam("qty") int quantity,
    		@RequestParam("productUuid") UUID productUuid,Model model) {
        String currentUsername = String.valueOf(getCurrentUsername());
        System.out.println("inside addcart");

//        if (currentUsername.equals("anonymousUser")) {
//            return "login";
//        }

        if (quantity < 0 || quantity > 100) {
            return "invalid quantity, accepted values are 0-100";
        }

        if (cartService.addToCart(productUuid,quantity)) {
        	return "redirect:/productDetail?productUuid=" + productUuid;
        } else {
            return "not add";
        }
//        boolean addedToCart = cartService.addToCart(productUuid, quantity);
//
//        // Add the addedToCart value to the model
//        model.addAttribute("addedToCart", addedToCart);
//
//        // Redirect to the product detail page or a confirmation page
//        return "redirect:/productDetail?productUuid=" + productUuid;
        
    }
    
    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String deleteFromCart(@RequestParam Variant variantId){
        //get current user
        String currentUsername = String.valueOf(getCurrentUsername());
        //UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        UserInfo userInfo = userInfoService.findByUsername("test");

        List<Cart> carts = cartService.findByUser(userInfo);
        String deletedStatus = "not deleted";
        for(Cart cart : carts){
            if(cart.getVariant().equals(variantId)){
                cartService.delete(cart);
                deletedStatus = "deleted";
                break;
            }
        }
        return deletedStatus;
    }

//    @PostMapping("/update")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//    public String updateCart(@RequestBody List<cartDto> updatedCart){
//        List<Cart> existingCart = cartService.findByUser(userInfoService.findByUsername(getCurrentUsername()));
//
//       for(cartDto item : updatedCart){
//           if(item.getQty() == 0){
//               cartService.deleteCartById(item.getUuid());
//               System.out.println(item.getUuid()+ " deleted");
//
//           }else{
//               Cart existingCartItem = cartService.findCartByUuid(item.getUuid());
//               if(existingCartItem.getQuantity() != item.getQty()){
//                   System.out.println(item.getUuid()+ " updated");
//                   existingCartItem.setQuantity(item.getQty());
//                   cartService.save(existingCartItem);
//               }
//           }
//       }
//
//        return "redirect:/viewCart";
//    }

//    @PostMapping("/update")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//    public String updateCart(@ModelAttribute("updatedCart") List<cartDto> updatedCart) {
//        List<Cart> existingCart = cartService.findByUser(userInfoService.findByUsername(getCurrentUsername()));
//
//        for (cartDto item : updatedCart) {
//            if (item.getQty() == 0) {
//                cartService.deleteCartById(item.getUuid());
//                System.out.println(item.getUuid() + " deleted");
//            } else {
//                Cart existingCartItem = cartService.findCartByUuid(item.getUuid());
//                if (existingCartItem.getQuantity() != item.getQty()) {
//                    System.out.println(item.getUuid() + " updated");
//                    existingCartItem.setQuantity(item.getQty());
//                    cartService.save(existingCartItem);
//                }
//            }
//        }
//
//        return "redirect:/viewCart";
//    }
    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String updateCart(@RequestParam("cartItemId") List<UUID> cartItemIds,
                             @RequestParam("qty") List<Integer> quantities) {
        // Assuming the order of cartItemIds and quantities corresponds to the cart items in the table

        for (int i = 0; i < cartItemIds.size(); i++) {
            UUID cartItemId = cartItemIds.get(i);
            int quantity = quantities.get(i);

            if (quantity == 0) {
                cartService.deleteCartById(cartItemId);
                System.out.println(cartItemId + " deleted");
            } else {
                Cart existingCartItem = cartService.findCartByUuid(cartItemId);
                if (existingCartItem.getQuantity() != quantity) {
                    System.out.println(cartItemId + " updated");
                    existingCartItem.setQuantity(quantity);
                    cartService.save(existingCartItem);
                }
            }
        }

        return "redirect:/viewCart"; // Redirect back to the same update page after saving changes
    }

	//getting current username
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
