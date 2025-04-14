package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.repository.ProductRepository;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.domain.Products;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categroryService;
    public ProductService(ProductRepository productRepository, CategoryService categroryService){
        this.productRepository = productRepository;
        this.categroryService = categroryService;
    }

    public  List<Products> fetchAllProducts(){
        return this.productRepository.findAll();
    }

    public List<Products> fetchProductsByCate(String category){
        Categrories cate = this.categroryService.findByCateName(category);
        if (cate == null){
            return null;
        }
        return this.productRepository.findByCategrory(cate);
    }

    public Products handleFetchProduct(String productId){
        ObjectId objectId = new ObjectId(productId);
        Optional<Products> product = this.productRepository.findById(objectId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public Products addAProduct(Products products){
        return this.productRepository.save(products);
    }

    public Boolean deleteProduct(String productId) throws IdInvalidException {
        Optional<Products> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IdInvalidException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        productRepository.deleteById(productId);
        return true;
    }

    public Optional<Products> findProductById(String id){
        Optional<Products> product = productRepository.findById(id);
        return product;
    }
}
