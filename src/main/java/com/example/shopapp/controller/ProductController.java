package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductImage;
import com.example.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    //Get all products
    @GetMapping("")
    ResponseEntity<String> getAllProducts(@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam(value="page",defaultValue = "2") int limit)
    {
        return  ResponseEntity.status(HttpStatus.OK).body("Get all products");
    }

    //Get a product base on id
    @GetMapping("{id}")
    ResponseEntity<String> getProduct(@PathVariable String id)
    {
        return ResponseEntity.status(HttpStatus.OK).body("Product have id "+ id);
    }
    //Create a product
    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(@Valid ProductDTO productDTO,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(newProduct);
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //upload image of product
    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages( @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files) {
        try {
            List<ProductImage> productImages = new ArrayList<>();
            files = files == null ? new ArrayList<>() : files;
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large");
                }
                String contentType = file.getContentType();
                if (isImage(file)) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                Product existedProduct = productService.getProductById(productId);
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(existedProduct.getId(), ProductImageDTO.builder()
                        .imageUrl(fileName)
                        .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException
    {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString()+"_"+filename;
        Path uploadDir = Paths.get("uploads");
        //Check exists folder exits and create new folder if not
        if(!Files.exists(uploadDir))
        {
            Files.createDirectories(uploadDir);
        }
        //Files destination
        Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        //Copy file to destination
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    //Delete a product base on id
    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteProduct(@PathVariable String id)
    {
        return ResponseEntity.status(HttpStatus.OK).body("Delete product "+id);
    }
    //Update a product base on id
    @PutMapping("{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id)
    {
        return ResponseEntity.status(HttpStatus.OK).body("update product");
    }
}
