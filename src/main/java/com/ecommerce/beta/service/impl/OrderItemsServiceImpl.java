package com.ecommerce.beta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.OrderItems;
import com.ecommerce.beta.repository.OrderItemsRepository;
import com.ecommerce.beta.service.OrderItemService;

@Service
public class OrderItemsServiceImpl implements OrderItemService {
    @Autowired
    OrderItemsRepository orderItemsRepository;
    @Override
    public OrderItems save(OrderItems item) {
        return orderItemsRepository.save(item);
    }

    @Override
    public List<OrderItems> findByOrder(OrderHistory orderHistory) {
        return orderItemsRepository.findByOrderHistory(orderHistory);
    }
}
