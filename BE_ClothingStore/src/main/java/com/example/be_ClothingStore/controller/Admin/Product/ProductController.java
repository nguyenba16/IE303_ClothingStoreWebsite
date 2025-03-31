package com.example.be_ClothingStore.controller.Admin.Product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/products/add")
    public ResponseEntity<Products> postMethodName(@RequestBody Products products) {
        this.productService.addAProduct(products);
        return ResponseEntity.ok().body(products);
    }
    
}
