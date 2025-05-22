package com.ecommerce.beta.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Coupon;
import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.enums.OrderStatus;
import com.ecommerce.beta.enums.OrderType;

@Service
public interface OrderHistoryService {

//    boolean findByAddressId(UUID uuid);

	OrderHistory save(OrderHistory orderHistory);

    Page<OrderHistory> findAll(Pageable pageable);

    OrderHistory findById(UUID uuid);

    Page<OrderHistory> findByUserInfo(UserInfo userInfo, Pageable pageable);
    List<OrderHistory> findByUserInfo(UserInfo userInfo);
    
    Page<OrderHistory> findByUserInfoAndDeleted(UserInfo userInfo, Boolean b, Pageable pageable);


    List<OrderHistory> findOrdersByDate(Date startDate, Date endDate);

    List<OrderHistory> getLastFiveOrders();

    Page<OrderHistory> searchOrderByKeyword(String keyword, Pageable pageable);

    Page<OrderHistory> findByOrderType(OrderType orderType, Pageable pageable);
    Page<OrderHistory> findByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    Page<OrderHistory> findByIdLike(String keyword, Pageable pageable);
    Page<OrderHistory> findByCoupon(Coupon coupon, Pageable pageable);



}
