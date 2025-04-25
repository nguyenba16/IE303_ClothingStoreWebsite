package com.example.be_ClothingStore.domain.RequestSearch;


public class RequestSearch {
    private String productName;
    private String category;
    private int rating;
    private String sortPrice;
    private int currentPage;
    private int limmitItems;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getSortPrice() {
        return sortPrice;
    }
    public void setSortPrice(String sortPrice) {
        this.sortPrice = sortPrice;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getLimmitItems() {
        return limmitItems;
    }
    public void setLimmitItems(int limmitItems) {
        this.limmitItems = limmitItems;
    }
    
}
