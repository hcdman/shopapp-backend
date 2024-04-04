package com.example.shopapp.responses;

import com.example.shopapp.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty(namespace = "category_id")
    private Long categoryId;
    public static ProductResponse fromProduct(Product product)
    {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .categoryId(product.getCategory().getId())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription()).build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
