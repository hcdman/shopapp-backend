package com.example.shopapp.services;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.Category;
import com.example.shopapp.model.Product;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
       Category category= categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->new DataNotFoundException("Can't find category id"));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(category).build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existedProduct = getProductById(id);
        if(existedProduct!=null)
        {
            Category category= categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()->new DataNotFoundException("Can't find category id"));
            existedProduct.setName(productDTO.getName());
            existedProduct.setPrice(productDTO.getPrice());
            existedProduct.setDescription(productDTO.getDescription());
            existedProduct.setThumbnail(productDTO.getThumbnail());
            existedProduct.setCategory(category);
            return existedProduct;

        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete); //delete entity of product
    }
    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
