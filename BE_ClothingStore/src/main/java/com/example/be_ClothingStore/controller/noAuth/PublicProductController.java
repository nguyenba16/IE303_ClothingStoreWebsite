package com.example.be_ClothingStore.controller.noAuth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.service.ProductService;
import com.example.be_ClothingStore.service.error.IdInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/no-auth")
public class PublicProductController {
    private final ProductService productService;
    public PublicProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProduct() {
        List<Products> products = productService.fetchAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    
    @GetMapping("products/{productId}")
    public ResponseEntity<Products> getDetailProduct(@PathVariable("productId") String productId)throws IdInvalidException {
        Products product = this.productService.handleFetchProduct(productId);
        if (product == null) {
            throw new IdInvalidException("Không tìm thấy product");
        } 
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
    
}
