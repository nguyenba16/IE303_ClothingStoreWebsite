package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.be_ClothingStore.repository.ProductRepository;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.Reviews;
import com.example.be_ClothingStore.domain.RequestSearch.RequestSearch;
import com.example.be_ClothingStore.domain.ResultSearch.Pagination;
import com.example.be_ClothingStore.domain.ResultSearch.ResultSearch;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categroryService;
    private final MongoTemplate mongoTemplate;
    public ProductService(MongoTemplate mongoTemplate,ProductRepository productRepository, CategoryService categroryService){
        this.productRepository = productRepository;
        this.categroryService = categroryService;
        this.mongoTemplate = mongoTemplate;
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

    public Boolean updateProductRating(String productId, List<Reviews> reviews) throws IdInvalidException{
        Products products = this.findProductById(productId).get();
        if (products == null){
            throw new IdInvalidException("Không tìm thấy product với id này!");
        }
        
        float ratingTotal = 0;
        for (Reviews r: reviews){
            ratingTotal += r.getRating();
        }
        
        float rating = Math.round((ratingTotal / reviews.size()) * 10f) / 10f;
        products.setRating(rating);
        this.productRepository.save(products);
        return true;
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

    public ResultSearch searchProducts(RequestSearch requestSearch){
        Query query = new Query();
        if (requestSearch.getProductName() != null && !requestSearch.getProductName().isEmpty()) {
            query.addCriteria(Criteria.where("productName").regex(requestSearch.getProductName(), "i")); // tìm không phân biệt hoa thường
        }

        if (requestSearch.getCategory() != null && !requestSearch.getCategory().isEmpty()) {
            Categrories cate = categroryService.findByCateName(requestSearch.getCategory());
            if (cate != null) {
                query.addCriteria(Criteria.where("categrory").is(cate));
            }
        }

        if (requestSearch.getRating() > 0) {
            query.addCriteria(Criteria.where("rating").gte(requestSearch.getRating()));
        }

        // Sắp xếp theo giá tăng/giảm
        if (requestSearch.getSortPrice() != null && requestSearch.getSortPrice().equals("asc")) {
            query.with(Sort.by(Sort.Direction.ASC, "price"));
        } else if (requestSearch.getSortPrice() != null && requestSearch.getSortPrice().equals("desc")) {
            query.with(Sort.by(Sort.Direction.DESC, "price"));
        }

        long totalItems = mongoTemplate.count(query, Products.class);
        // Phân trang
        int currentPage = requestSearch.getCurrentPage() > 0 ? requestSearch.getCurrentPage() : 1;
        int limit = requestSearch.getLimmitItems() > 0 ? requestSearch.getLimmitItems() : 10;
        int skip = (currentPage - 1) * limit;
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(currentPage);
        pagination.setLimitItem(limit);
        pagination.setPageTotal(totalPages);

        query.skip(skip).limit(limit);
        ResultSearch searchResult =  new ResultSearch();
        List<Products> products = mongoTemplate.find(query, Products.class);
        searchResult.setProducts(products);
        searchResult.setPagination(pagination);
        
        return searchResult;
    }
}
