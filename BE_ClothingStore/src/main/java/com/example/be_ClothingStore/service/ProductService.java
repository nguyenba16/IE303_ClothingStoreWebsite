package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.repository.ProductRepository;
import com.example.be_ClothingStore.domain.Products;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public  List<Products> fetchAllProducts(){
        return this.productRepository.findAll();
    }

    public Products handleFetchProduct(String productId){
        ObjectId objectId = new ObjectId(productId);
        Optional<Products> product = this.productRepository.findById(objectId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public void addAProduct(Products products){
        this.productRepository.save(products);
    }
}
