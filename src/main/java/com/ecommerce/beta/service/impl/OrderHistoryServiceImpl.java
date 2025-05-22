package com.ecommerce.beta.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.Coupon;
import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.enums.OrderStatus;
import com.ecommerce.beta.enums.OrderType;
import com.ecommerce.beta.repository.OrderHistoryRepository;
import com.ecommerce.beta.service.OrderHistoryService;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @Override
    public OrderHistory save(OrderHistory orderHistory) {
        return orderHistoryRepository.save(orderHistory);
    }

    @Override
    public Page<OrderHistory> findAll(Pageable pageable) {
        return orderHistoryRepository.findAll(pageable);
    }

    @Override
    public OrderHistory findById(UUID uuid) {
        return orderHistoryRepository.findById(uuid).orElse(null);
    }

    @Override
    public Page<OrderHistory> findByUserInfo(UserInfo userInfo, Pageable pageable) {

        return orderHistoryRepository.findByUserInfo(userInfo, pageable);
    }

    @Override
    public Page<OrderHistory> findByUserInfoAndDeleted(UserInfo userInfo, Boolean b, Pageable pageable) {

       return orderHistoryRepository.findByUserInfoAndDeleted(userInfo, b, pageable);
    }


    @Override
    public List<OrderHistory> findOrdersByDate(Date startDate, Date endDate) {
        return orderHistoryRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<OrderHistory> getLastFiveOrders() {
        return orderHistoryRepository.getLastFiveOrders();
    }

    @Override
    public Page<OrderHistory> searchOrderByKeyword(String keyword, Pageable pageable) {
        UUID uuid = null;
        try {
             uuid = UUID.fromString(keyword);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("INVALID UUID");
        }
        
        
        return orderHistoryRepository.findByUuidLike(uuid, pageable);

       // return orderHistoryRepository.findOrderByKeyword(keyword, pageable);
    }

    @Override
    public Page<OrderHistory> findByOrderType(OrderType orderType, Pageable pageable) {
      return orderHistoryRepository.findByOrderType(orderType, pageable);
    }

    @Override
    public Page<OrderHistory> findByOrderStatus(OrderStatus orderStatus, Pageable pageable) {
        return orderHistoryRepository.findByOrderStatus(orderStatus, pageable);
    }

    @Override
    public Page<OrderHistory> findByIdLike(String keyword, Pageable pageable) {

        try {
            UUID uuid = UUID.fromString(keyword); // Check if the string is a valid UUID
            return orderHistoryRepository.findByUuidLike(uuid, pageable);
        } catch (IllegalArgumentException e) {
            // Handle the case when the string is not a valid UUID
            // You can log an error, throw an exception, or return an empty page, etc.
            return Page.empty();
        }
    }

	@Override
	public List<OrderHistory> findByUserInfo(UserInfo userInfo) {
		return orderHistoryRepository.findByUserInfo(userInfo);
	}

	@Override
    public Page<OrderHistory> findByCoupon(Coupon coupon, Pageable pageable) {
        return orderHistoryRepository.findByCoupon(coupon, pageable );
    }

//	@Override
//	public boolean findByAddressId(UUID uuid) {
//		return orderHistoryRepository.findByAddressId(uuid);
//	}

  
}
