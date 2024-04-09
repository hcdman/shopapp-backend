package com.example.shopapp.services;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductImage;
import com.example.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws Exception;
    public Product getProductById(long id);
    Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existsByName(String name);
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    public List<Product> findProductsByIds(List<Long> productIds);
}
