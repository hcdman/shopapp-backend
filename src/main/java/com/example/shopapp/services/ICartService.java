package com.example.shopapp.services;

import com.example.shopapp.dto.CartItemDTO;
import com.example.shopapp.model.Cart;

import java.util.List;

public interface ICartService {
    void addProduct(CartItemDTO cartItemDTO) throws Exception;
    List<Cart> getCartByUserId(Long userId) throws  Exception;
    void deleteProductInCart(Long userId, Long productId) throws Exception;
    void deleteProductOfUser(Long userId) throws Exception;
}
