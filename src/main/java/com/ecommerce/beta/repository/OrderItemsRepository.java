package com.ecommerce.beta.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.OrderItems;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {
	
    List<OrderItems> findByOrderHistory(OrderHistory orderHistory);
}
