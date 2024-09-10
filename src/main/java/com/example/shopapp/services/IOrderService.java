package com.example.shopapp.services;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.Order;
import com.example.shopapp.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse getOrder(Long id) throws Exception;
    OrderResponse updateOrder(Long id,OrderDTO orderDTO) throws Exception;
    void deleteOrder(Long id) throws Exception;
    public Page<Order> getAllOrders(String keyWord, Long userId, Pageable pageable) ;
    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
}
