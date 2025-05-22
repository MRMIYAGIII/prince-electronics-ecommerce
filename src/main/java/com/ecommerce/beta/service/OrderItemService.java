package com.ecommerce.beta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.OrderItems;

@Service
public interface OrderItemService {

    OrderItems save(OrderItems item);

    List<OrderItems> findByOrder(OrderHistory orderHistory);
}
