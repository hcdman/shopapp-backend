package com.example.shopapp.controller;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.model.Category;
import com.example.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "limit",defaultValue = "2") int limit)
    {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO category, BindingResult result)
    {
        if(result.hasErrors())
        {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage).toList();
            return  ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok(   categoryService.createCategory(category));
    }
    @PutMapping("{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO)
    {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update category successfully!");
    }
    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteCategory(@PathVariable Long id)
    {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category"+id);
    }
}
