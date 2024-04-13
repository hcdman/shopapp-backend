package com.example.shopapp.services;

import com.example.shopapp.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface IProductImageService {

    public void deleteImage(Long id);
}
