package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
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
    //Insert a product
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@Valid ProductDTO productDTO,
                                           @ModelAttribute("file") MultipartFile file,
                                           BindingResult result) {
        try {
            // Check file size limit
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("Maximum file size allowed is 10MB");
            }
            // Validate file type
            if (!isImage(file)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }

            // Process the file (store, rename, etc.)
            String filename = storeFile(file);
            productDTO.setThumbnail(filename);

            // Handle validation errors
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Save the product
            // productService.saveProduct(product);

            return ResponseEntity.status(HttpStatus.OK).body("create a product successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the file: " + e.getMessage());
        }
    }


    @PostMapping(value = "uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try {
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > 0) {
                return ResponseEntity.badRequest().body("");
            }

            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("");
                }
                // Lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file); // Thay thế hàm này với code của bạn để lưu file
                //lưu vào đối tượng product trong DB
            }
            return ResponseEntity.ok().body("ok");
        } catch (Exception e) {
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
