package com.example.be_ClothingStore.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Items {
    @DBRef
    private Product productID;
    private int quantity;
    private String size;
    private String color;
    
    public Product getProductID() {
        return productID;
    }
    
    public Items(Product productID, int quantity, String size) {
        this.productID = productID;
        this.quantity = quantity;
        this.size = size;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    } 
    
}
