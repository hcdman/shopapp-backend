package com.example.shopapp.controller;

import com.example.shopapp.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @GetMapping("")
    ResponseEntity<String> getAllCategory( @RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "limit",defaultValue = "2") int limit)
    {
        return ResponseEntity.ok(String.format("Category get page %d, limit %d",page,limit));
    }
    @PostMapping("")
    ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO category, BindingResult result)
    {
        if(result.hasErrors())
        {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage).toList();
            return  ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("this is insert category : "+category);
    }
    @PutMapping("{id}")
    public ResponseEntity<String> updateCategory(@PathVariable String id)
    {
        return ResponseEntity.ok(id);
    }
    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteCategory(@PathVariable String id)
    {
        return ResponseEntity.ok("Delete "+id);
    }
}
