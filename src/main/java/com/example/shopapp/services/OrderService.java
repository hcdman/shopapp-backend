package com.example.shopapp.services;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.Category;
import com.example.shopapp.model.Order;
import com.example.shopapp.model.OrderStatus;
import com.example.shopapp.model.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        //Check exist user id
        User user = userRepository.findById(orderDTO.getUserID()).orElseThrow(
                ()-> new DataNotFoundException("Can't not find use with id "+orderDTO.getUserID())
        );
        //convert order dto -> order use model mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
        // update all field except id of order
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate()==null?LocalDate.now():orderDTO.getShippingDate();
        //shipping date > today
        if(shippingDate==null||shippingDate.isBefore(LocalDate.now()))
        {
            throw new DataNotFoundException("Date shipping must be at least today");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) throws Exception {
        Order existedOrder = orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Order not exist !")
        );

        return modelMapper.map(existedOrder,OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order existedOrder = orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Order not exist !")
        );
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
        // update all field except id of order
        modelMapper.map(orderDTO,existedOrder);
        orderRepository.save(existedOrder);
        return modelMapper.map(existedOrder,OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) throws Exception {
        Order existedOrder = orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Order not exist !")
        );
        orderRepository.deleteById(id);

    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        List<OrderResponse> listOrder = new ArrayList<>();
        List<Order>  orders= orderRepository.findByUserId(userId);
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
        for (Order order:orders)
        {
            listOrder.add(modelMapper.map(order,OrderResponse.class));
        }
        return listOrder;
    }
}
