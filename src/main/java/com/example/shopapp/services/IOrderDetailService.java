package com.example.shopapp.services;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(Long id);
    List<OrderDetail> getOrderDetails(Long orderId) throws DataNotFoundException;

}
