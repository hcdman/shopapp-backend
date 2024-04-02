package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    //Create one order detail
    @PostMapping("")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
        return ResponseEntity.ok("Create an order detail successfully!");
    }
    //Get order detail
    @GetMapping("{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id)
    {
        return ResponseEntity.ok("get order detail with id : "+id);
    }
    //get order detail base on order id
    //Update an order detail
    @PutMapping("{id}")
    public  ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id, OrderDetailDTO orderDetailDTO)
    {
        return  ResponseEntity.ok("Update order detail with id : "+id);
    }

    //Delete an order detail
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("id") Long id)
    {
        return  ResponseEntity.ok("Remove order detail successfully "+ id);
    }
}
