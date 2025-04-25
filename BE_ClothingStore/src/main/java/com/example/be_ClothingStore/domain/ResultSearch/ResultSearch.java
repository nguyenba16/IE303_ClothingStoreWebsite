package com.example.be_ClothingStore.domain.ResultSearch;

import java.util.List;

import com.example.be_ClothingStore.domain.Products;

public class ResultSearch {
    private List<Products> products;
    private Pagination pagination;
    public List<Products> getProducts() {
        return products;
    }
    public void setProducts(List<Products> products) {
        this.products = products;
    }
    public Pagination getPagination() {
        return pagination;
    }
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    
}
