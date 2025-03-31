package com.example.be_ClothingStore.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
public class Products {
    @Id
    private String id;

    // Tham chiếu đến Categrories
    @DBRef
    private Categrories categroryID;

    private String productName;
    private Double price;
    private String[] colors;
    private String[] sizes;
    private int stock;
    private String desc;
    private Image[] productImage;
    private float rating;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    public Products() {}

    public Products(String id, String productName, Double price, String[] colors, String[] sizes, int stock, String desc,
            Image[] productImage, Categrories categroryID) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.colors = colors;
        this.sizes = sizes;
        this.stock = stock;
        this.desc = desc;
        this.productImage = productImage;
        this.categroryID = categroryID;
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
    public String[] getColors() {
        return colors;
    }
    public String[] getSizes() {
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
    public Categrories getCategroryID() {
        return categroryID;
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
    public void setColors(String[] colors) {
        this.colors = colors;
    }
    public void setSizes(String[] sizes) {
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
    public void setCategroryID(Categrories categroryID) {
        this.categroryID = categroryID;
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
