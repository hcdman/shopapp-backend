package com.example.shopapp.repositories;

import com.example.shopapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //find order of user base on user id
  //  List<Order> findByUserId(Long userId);
}
