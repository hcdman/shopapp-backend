package com.example.shopapp.controller;

import com.example.shopapp.dto.CartItemDTO;
import com.example.shopapp.model.Cart;
import com.example.shopapp.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {
    private  final ICartService cartService;
    @PostMapping("")
    ResponseEntity<?> addProductToCart(@RequestBody CartItemDTO cartItemDTO)
    {
        try {
            cartService.addProduct(cartItemDTO);
            return ResponseEntity.ok("Add product to cart successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("{userId}")
    ResponseEntity<?> getCartByUserId(@PathVariable("userId") Long userId)
    {
        try {
            List<Cart> carts = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(carts);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("")
    ResponseEntity<?> deleteProductInCart(@RequestBody CartItemDTO cartItemDTO)
    {
        try {
           cartService.deleteProductInCart(cartItemDTO);
            return ResponseEntity.ok("Delete product in cart successfully!");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
