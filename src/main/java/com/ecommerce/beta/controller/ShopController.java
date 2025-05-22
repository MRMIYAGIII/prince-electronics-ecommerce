package com.ecommerce.beta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.ecommerce.beta.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.beta.dto.CouponValidityResponseDto;
import com.ecommerce.beta.dto.OrderDto;
import com.ecommerce.beta.dto.UserDto;
import com.ecommerce.beta.enums.OrderStatus;
import com.ecommerce.beta.enums.OrderType;
import com.ecommerce.beta.service.AddressService;
import com.ecommerce.beta.service.CartService;
import com.ecommerce.beta.service.CategoryService;
import com.ecommerce.beta.service.ImageService;
import com.ecommerce.beta.service.OrderHistoryService;
import com.ecommerce.beta.service.OrderItemService;
import com.ecommerce.beta.service.ProductService;
import com.ecommerce.beta.service.UserInfoService;
import com.ecommerce.beta.service.WishlistService;
import com.ecommerce.beta.worker.TemplateInvoiceGenerator;

import javax.validation.Valid;

@Controller
public class ShopController {

    private final UserInfoService userInfoService;
    private final AddressService userAddressService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderHistoryService orderHistoryService;
    private final OrderItemService orderItemService;
    private final WishlistService wishlistService;
    private final TemplateInvoiceGenerator templateInvoiceGenerator;

    @Autowired
    public ShopController(UserInfoService userInfoService, AddressService userAddressService,
                          PasswordEncoder passwordEncoder, CategoryService categoryService,
                          ImageService imageService, ProductService productService,
                          CartService cartService, OrderHistoryService orderHistoryService,
                          OrderItemService orderItemService, WishlistService wishlistService,
                          TemplateInvoiceGenerator templateInvoiceGenerator) {
        this.userInfoService = userInfoService;
        this.userAddressService = userAddressService;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderHistoryService = orderHistoryService;
        this.orderItemService = orderItemService;
        this.wishlistService = wishlistService;
        this.templateInvoiceGenerator = templateInvoiceGenerator;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "anonymousUser";
    }

    @GetMapping("/")
    public String getHomePage(@RequestParam(required = false, defaultValue = "") String keyword,
                              @RequestParam(required = false, defaultValue = "") String filter,
                              Model model) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        List<Category> categories = categoryService.findAll();
        List<Product> filteredProducts;

        try {
            if (!keyword.isEmpty() && filter.isEmpty()) {
                filteredProducts = productService.findByNameLike("%" + keyword + "%")
                        .stream()
                        .filter(Product::isEnabled)
                        .collect(Collectors.toList());
            } else if (!filter.isEmpty() && keyword.isEmpty()) {
                filteredProducts = productService.findByCategoryUuid(UUID.fromString(filter))
                        .stream()
                        .filter(Product::isEnabled)
                        .collect(Collectors.toList());
            } else if (!keyword.isEmpty() && !filter.isEmpty()) {
                filteredProducts = productService.findByNameLike("%" + keyword + "%")
                        .stream()
                        .filter(Product::isEnabled)
                        .filter(product -> product.getCategory().getUuid().toString().equals(filter))
                        .collect(Collectors.toList());
            } else {
                filteredProducts = productService.findAll()
                        .stream()
                        .filter(Product::isEnabled)
                        .collect(Collectors.toList());
            }
            filteredProducts.forEach(product -> System.out.println("Product: " + product.getName() + ", Images: " + (product.getImages() != null ? product.getImages().size() : "null")));
            System.out.println("Categories: " + categories.size() + ", Filtered Products: " + filteredProducts.size());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid filter UUID: " + filter + ", Error: " + e.getMessage());
            filteredProducts = Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            filteredProducts = Collections.emptyList();
        }

        int cartCount = (userInfo != null && !currentUsername.equals("anonymousUser")) ? cartService.findByUser(userInfo).size() : 0;
        int wishlistCount = (userInfo != null && !currentUsername.equals("anonymousUser")) ? wishlistService.findByUser(userInfo).size() : 0;

        model.addAttribute("categories", categories);
        model.addAttribute("filteredProducts", filteredProducts);
        model.addAttribute("loggedIn", !currentUsername.equals("anonymousUser"));
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("wishlistCount", wishlistCount);
        return "shop/index";
    }

    @GetMapping("/productDetail")
    public String productView(@RequestParam(value = "productUuid", required = false) String productUuid,
                              Model model) {
        try {
            Product selectedProduct = productService.getProduct(UUID.fromString(productUuid));
            if (selectedProduct != null && selectedProduct.isEnabled()) {
                model.addAttribute("product", selectedProduct);
                return "shop/productView";
            } else {
                return "redirect:/?error=productNotFound";
            }
        } catch (IllegalArgumentException e) {
            return "redirect:/?error=invalidProductUuid";
        }
    }

    @PostMapping("/products/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") UUID productId, @RequestParam int qty) {
        Product product = productService.getProduct(productId);
        if (product != null && product.isEnabled()) {
            boolean added = cartService.addToCart(productId, qty);
            return "redirect:/" + (added ? "?added=true" : "?error=addToCartFailed");
        } else {
            return "redirect:/?error=productNotAvailable";
        }
    }

    @PostMapping("/products/add-to-wishlist/{id}")
    public String addToWishlist(@PathVariable("id") UUID productId) {
        Product product = productService.getProduct(productId);
        if (product != null && product.isEnabled()) {
            boolean added = wishlistService.addToWishlist(productId);
            return "redirect:/" + (added ? "?wishlisted=true" : "?error=addToWishlistFailed");
        } else {
            return "redirect:/?error=productNotAvailable";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(@RequestParam(name = "addAddress", defaultValue = "false", required = false) boolean addAddress,
                              Model model) {
        String currentUsername = getCurrentUsername();
        if (currentUsername.equals("anonymousUser")) {
            return "redirect:/login";
        }
        model.addAttribute("loggedIn", true);

        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        boolean noAddressFound = userInfo.getSavedAddresses().isEmpty() ||
                userInfo.getSavedAddresses().stream().noneMatch(a -> a.getFlat() != null && !a.getFlat().isEmpty());

        if (noAddressFound) {
            model.addAttribute("setupAddressWarning", true);
        }

        model.addAttribute("user", userInfo);

        List<Address> addresses = noAddressFound ? new ArrayList<>() : userAddressService.findByUser(userInfo);
        model.addAttribute("addresses", addresses);
        model.addAttribute("addAddress", addAddress);
        return "shop/profile";
    }

    @PostMapping("/address/save")
    public String save(@Valid @ModelAttribute Address userAddress, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/profile?error=invalidAddress";
        }
        UserInfo user = userInfoService.findByUsername(getCurrentUsername());
        userAddress.setUserInfo(user);

        List<Address> userAddressList = userAddressService.findByUser(user);

        if (userAddressList.isEmpty()) {
            userAddress.setDefaultAddress(true);
            userAddressService.save(userAddress);
            return "redirect:/profile";
        } else {
            if (userAddress.isDefaultAddress()) {
                Address existingDefaultAddress = userAddressList.stream()
                        .filter(Address::isDefaultAddress)
                        .findFirst()
                        .orElse(null);
                if (existingDefaultAddress != null) {
                    existingDefaultAddress.setDefaultAddress(false);
                    userAddressService.save(existingDefaultAddress);
                }
                userAddressService.save(userAddress);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/user/save")
    public String save(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/profile?error=invalidUser";
        }
        UserInfo user = userInfoService.findByUsername(getCurrentUsername());
        if (userDto.getUuid().equals(user.getUuid())) {
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());

            if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                if (!userDto.getNewPassword().equals("") && userDto.getNewPassword().equals(userDto.getNewPasswordRe())) {
                    user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                }
            }
            userInfoService.updateUser(user);
        } else {
            return "redirect:/profile?error=unauthorized";
        }
        return "redirect:/profile";
    }

    @GetMapping("/address/delete/{id}")
    public String deleteAddress(@PathVariable("id") UUID uuid) {
        userAddressService.deleteById(uuid);
        return "redirect:/profile";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model, @RequestParam(required = false) UUID addressUUID) {
        String currentUsername = getCurrentUsername();
        if (currentUsername.equals("anonymousUser")) {
            return "redirect:/login";
        }

        UserInfo userInfo = userInfoService.findByUsername(currentUsername);

        long count = userInfo.getSavedAddresses()
                .stream()
                .filter(a -> a.isEnabled())
                .count();

        if (count == 0) {
            System.out.println("No addresses found for user, redirecting to profile");
            return "redirect:/profile?addAddress=true";
        }

        List<Cart> cartItems = cartService.findByUser(userInfo);
        List<Wishlist> wishlistItems = wishlistService.findByUser(userInfo); // Added to fetch wishlist items
        List<Address> addressList = userAddressService.findByUserInfoAndEnabled(userInfo, true);
        List<Category> categories = categoryService.findAll(); // Added for search bar categories

        // Calculate counts
        int cartCount = cartItems.size();
        int wishlistCount = wishlistItems.size();

        model.addAttribute("addressList", addressList);
        model.addAttribute("nameOfUser", userInfo.getFirstName() + " " + userInfo.getLastName());
        model.addAttribute("cartEmpty", cartItems.isEmpty());
        model.addAttribute("loggedIn", true);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartCount", cartCount); // Added
        model.addAttribute("wishlistCount", wishlistCount); // Added
        model.addAttribute("categories", categories); // Added for search bar

        CouponValidityResponseDto couponValidityResponseDto = cartService.checkCouponValidity();
        double cartTotal = couponValidityResponseDto.getCartTotal();
        float total = (float) cartTotal;
        if (userInfo.getCoupon() != null) {
            model.addAttribute("appliedCouponCode", userInfo.getCoupon());
            model.addAttribute("couponApplied", true);
        }

        model.addAttribute("cartTotal", Math.round(total));

        List<Address> addresses = userAddressService.findByUserInfoAndEnabled(userInfo, true);
        Address defaultAddress = addresses
                .stream()
                .filter(Address::isDefaultAddress)
                .findFirst()
                .orElse(null);
        if (defaultAddress == null) {
            defaultAddress = addresses.get(0);
        }

        if (addressUUID == null) {
            model.addAttribute("address", defaultAddress);
        } else {
            model.addAttribute("address", userAddressService.findById(addressUUID));
        }

        return "shop/checkout";
    }

    @GetMapping("changeAddress/{uuid}")
    public String changeAddress(@PathVariable(name = "uuid") UUID uuid) {
        return "redirect:/viewCart?addressUUID=" + uuid;
    }

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("addressUUID") UUID addressUUID,
            RedirectAttributes redirectAttributes) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);

        if (userInfo == null || currentUsername.equals("anonymousUser")) {
            return "redirect:/login";
        }

        Address address = userAddressService.findById(addressUUID);
        if (address == null || !address.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            redirectAttributes.addFlashAttribute("error", "Invalid address selected.");
            return "redirect:/viewCart";
        }

        List<Cart> cartItems = cartService.findByUser(userInfo);
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cart is empty.");
            return "redirect:/viewCart";
        }

        CouponValidityResponseDto couponValidityResponseDto = cartService.checkCouponValidity();
        float total = (float) couponValidityResponseDto.getCartTotal();
        float tax = (float) (total * 0.18); // Assuming 18% tax rate
        float priceOff = (float) couponValidityResponseDto.getPriceOff();

        OrderHistory order = new OrderHistory();
        order.setUserInfo(userInfo);
        order.setUserAddress(address);
        order.setTotal(total);
        order.setTax(tax);
        order.setOffPrice(priceOff);
        order.setCreatedAt(new Date());

        // Set order type and status based on payment method
        if ("Online".equalsIgnoreCase(paymentMethod)) {
            order.setOrderType(OrderType.ONLINE);
            order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
        } else if ("COD".equalsIgnoreCase(paymentMethod)) {
            order.setOrderType(OrderType.COD);
            order.setOrderStatus(OrderStatus.PENDING);
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid payment method.");
            return "redirect:/viewCart";
        }

        orderHistoryService.save(order);

        for (Cart cart : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderHistory(order);
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setOrderPrice(cart.getProductId().getPrice().floatValue());
            orderItem.setVariant(cart.getVariant());
            orderItemService.save(orderItem);
            cartService.delete(cart);
        }

        redirectAttributes.addAttribute("orderId", order.getUuid());
        redirectAttributes.addAttribute("newOrderFlag", true);
        return "redirect:/orderDetails";
    }

    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam(name = "orderId") UUID orderId,
                               @RequestParam(name = "newOrderFlag", required = false, defaultValue = "false") boolean newOrderFlag,
                               Model model) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        OrderHistory orderHistory = orderHistoryService.findById(orderId);

        if (orderHistory == null || !orderHistory.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            return "redirect:/?error=orderNotFound";
        }

        List<OrderItems> orderItems = orderItemService.findByOrder(orderHistory);
        OrderDto orderDto = new OrderDto();
        orderDto.setUuid(orderHistory.getUuid());
        orderDto.setOrderStatus(orderHistory.getOrderStatus());

        model.addAttribute("orderDate", formatDate(orderHistory.getCreatedAt()));
        model.addAttribute("order", orderHistory);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orderSuccessfulAnimation", newOrderFlag);
        model.addAttribute("statusList", OrderStatus.values());
        return "shop/orderDetails";
    }

    @PostMapping("/order/update")
    public String updateOrder(@ModelAttribute("orderDto") OrderDto orderDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        OrderHistory existingOrder = orderHistoryService.findById(orderDto.getUuid());

        if (existingOrder == null || !existingOrder.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or you do not have permission to update this order.");
            return "redirect:/order/all";
        }

        existingOrder.setOrderStatus(orderDto.getOrderStatus());
        orderHistoryService.save(existingOrder);
        redirectAttributes.addAttribute("orderId", existingOrder.getUuid());
        return "redirect:/orderDetails";
    }

    @PostMapping("/user/order/cancel")
    public String cancelOrder(@RequestParam("uuid") UUID uuid, RedirectAttributes redirectAttributes) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        OrderHistory order = orderHistoryService.findById(uuid);

        if (order == null || !order.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or you do not have permission to cancel this order.");
            return "redirect:/orders";
        }

        if (order.getOrderStatus() != OrderStatus.CANCELLED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderHistoryService.save(order);
            redirectAttributes.addFlashAttribute("success", "Order cancelled successfully.");
        }
        return "redirect:/orders";
    }

    @PostMapping("/user/order/return")
    public String returnOrder(@RequestParam("uuid") UUID uuid, RedirectAttributes redirectAttributes) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        OrderHistory order = orderHistoryService.findById(uuid);

        if (order == null || !order.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or you do not have permission to return this order.");
            return "redirect:/orders";
        }

        if (order.getOrderStatus() != OrderStatus.CANCELLED) {
            order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
            orderHistoryService.save(order);
            redirectAttributes.addFlashAttribute("success", "Return request submitted successfully.");
        }
        return "redirect:/orders";
    }

    @GetMapping("/wishlist")
    public String viewWishlist(Model model) {
        String currentUsername = getCurrentUsername();
        if (currentUsername.equals("anonymousUser")) {
            return "redirect:/login";
        }

        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        List<Wishlist> wishlistItems = wishlistService.findByUser(userInfo);
        List<Category> categories = categoryService.findAll();
        int cartCount = cartService.findByUser(userInfo).size();
        int wishlistCount = wishlistItems.size();

        model.addAttribute("wishlistItems", wishlistItems);
        model.addAttribute("categories", categories);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("loggedIn", true);

        return "shop/wishlist";
    }

    @PostMapping("/wishlist/remove/{id}")
    public String removeFromWishlist(@PathVariable("id") UUID wishlistId, RedirectAttributes redirectAttributes) {
        String currentUsername = getCurrentUsername();
        if (currentUsername.equals("anonymousUser")) {
            return "redirect:/login";
        }

        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        Wishlist wishlistItem = wishlistService.findById(wishlistId);

        if (wishlistItem != null && wishlistItem.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            wishlistService.deleteById(wishlistId); // Use deleteById directly
            redirectAttributes.addFlashAttribute("removed", true);
            return "redirect:/wishlist";
        } else {
            redirectAttributes.addFlashAttribute("error", "removeFromWishlistFailed");
            return "redirect:/wishlist";
        }
    }

    @GetMapping("/order/generateInvoice")
    public ResponseEntity<byte[]> generateInvoice(@RequestParam("uuid") UUID orderId) throws IOException {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        OrderHistory orderHistory = orderHistoryService.findById(orderId);

        if (orderHistory == null || !orderHistory.getUserInfo().getUuid().equals(userInfo.getUuid())) {
            return ResponseEntity.badRequest().body("Order not found or you do not have permission to access this order.".getBytes());
        }

        templateInvoiceGenerator.generateInvoice(orderHistory);
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + "/src/main/resources/static/uploads/invoices/" + orderId + ".pdf";
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.badRequest().body("Invoice not generated".getBytes());
        }

        byte[] pdfBytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(pdfBytes);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_" + orderId + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/orders")
    public String orderHistory(Model model) {
        String currentUsername = getCurrentUsername();
        UserInfo userInfo = userInfoService.findByUsername(currentUsername);
        List<OrderHistory> orderList = orderHistoryService.findByUserInfo(userInfo);

        // Add categories, cartCount, and wishlistCount to the model to avoid null errors
        List<Category> categories = categoryService.findAll();
        int cartCount = cartService.findByUser(userInfo).size();
        int wishlistCount = wishlistService.findByUser(userInfo).size();

        model.addAttribute("orderList", orderList);
        model.addAttribute("categories", categories);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("wishlistCount", wishlistCount);
        return "shop/orderHistory";
    }

    String formatDate(Date date) {
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }
}