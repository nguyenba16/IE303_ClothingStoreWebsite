package com.example.be_ClothingStore.domain.ResultSearch;

public class Pagination {
    private int pageTotal;
    private int limitItem;
    private int currentPage;
    public int getPageTotal() {
        return pageTotal;
    }
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
    public int getLimitItem() {
        return limitItem;
    }
    public void setLimitItem(int limitItem) {
        this.limitItem = limitItem;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
}
