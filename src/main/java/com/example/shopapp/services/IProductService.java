package com.example.shopapp.services;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    public Product getProductById(long id);
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existsByName(String name);
}
