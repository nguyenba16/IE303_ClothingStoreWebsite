package com.example.be_ClothingStore.controller.noAuth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.service.ProductService;
import com.example.be_ClothingStore.service.error.IdInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




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
    
    @GetMapping("/products/detail/{productId}")
    public ResponseEntity<Products> getDetailProduct(@PathVariable("productId") String productId)throws IdInvalidException {
        Products product = this.productService.handleFetchProduct(productId);
        if (product == null) {
            throw new IdInvalidException("Không tìm thấy product");
        } 
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/products/category")
    public ResponseEntity<?> getMethodName(@RequestParam(value = "category", required = false) String category) {
        List<Products> products = null;
        if (category == null || category == ""){
            products = productService.fetchAllProducts();
        } else {
            products = productService.fetchProductsByCate(category);
        }
        if (products == null){
            RestResponse<List<Products>> error = new RestResponse<>(HttpStatus.BAD_REQUEST.value(), 
            "Không tìm thấy sản phẩm nào! Hoặc Category không tồn tai!", 
            "Không tìm thấy sản phẩm nào! Hoặc Category không tồn tai!", 
            null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    
    
}
