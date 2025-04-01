package com.example.be_ClothingStore.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
public class Products {
    @Id
    private String id;

    @DBRef
    private Categrories categrory;

    private String productName;
    private Double price;
    private List<String> colors;
    private List<String> sizes;
    private int stock;
    private String desc;
    private Image[] productImage;
    private float rating;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    public Products() {}

    public Products(String id, String productName, Double price, List<String> colors, List<String> sizes, int stock, String desc,
            Image[] productImage, Categrories categrory) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.colors = colors;
        this.sizes = sizes;
        this.stock = stock;
        this.desc = desc;
        this.productImage = productImage;
        this.categrory = categrory;
        this.rating = 0;
    }
    public String getId() {
        return id;
    }
    public String getProductName() {
        return productName;
    }
    public Double getPrice() {
        return price;
    }
    public List<String> getColors() {
        return colors;
    }
    public List<String> getSizes() {
        return sizes;
    }
    public int getStock() {
        return stock;
    }
    public String getDesc() {
        return desc;
    }
    public Image[] getProductImage() {
        return productImage;
    }
    public Categrories getCategrory() {
        return categrory;
    }
    public float getRating() {
        return rating;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setColors(List<String> colors) {
        this.colors = colors;
    }
    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setProductImage(Image[] productImage) {
        this.productImage = productImage;
    }
    public void setCategrory(Categrories categrory) {
        this.categrory = categrory;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    
}
