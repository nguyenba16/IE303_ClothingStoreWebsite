package com.example.be_ClothingStore.controller.Admin.Categrories;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
public class CategoriesController {
    private final CategoryService categroryService;
    public CategoriesController(CategoryService categroryService){
        this.categroryService = categroryService;
    }

    @PostMapping("/categrories/add")
    public ResponseEntity<Categrories> addANewCategrory(@RequestBody Categrories categrories) {
        Categrories NewCate = this.categroryService.addANewCategrory(categrories);
        return ResponseEntity.ok().body(NewCate);
    }
    
}
