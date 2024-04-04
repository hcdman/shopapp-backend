package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result)
    {
        try
        {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(orderResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get information of order base on user id
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getOrdersUser(@Valid @PathVariable("user_id") Long userId)
    {
        try {
            return ResponseEntity.ok().body(orderService.getAllOrders(userId));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnOrder(@Valid @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok().body(orderService.getOrder(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //update order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,@Valid @RequestBody OrderDTO orderDTO)
    {
        try {
            return ResponseEntity.ok().body(orderService.updateOrder(id,orderDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable("id") Long id)
    {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Delete order successfully!" +id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
