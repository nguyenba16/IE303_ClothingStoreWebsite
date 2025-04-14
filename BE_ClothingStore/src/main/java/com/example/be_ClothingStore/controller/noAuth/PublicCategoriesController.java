package com.example.be_ClothingStore.controller.noAuth;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/no-auth")
public class PublicCategoriesController {
    private final CategoryService categoryService;
    public PublicCategoriesController (CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categrories>> getMethodName() {
        List<Categrories> categrories = this.categoryService.fetchAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categrories);
    }
    

}
