package com.example.be_ClothingStore.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Reviews")
public class Reviews {
    @Id
    private String id;

    @DBRef
    private User userID;

    @DBRef
    private Product productID;

    private String comment;
    private float rating;
    @CreatedDate
    private LocalDateTime createAt;

    public Reviews(String id, User userID, Product productID, String comment, float rating, LocalDateTime createAt) {
        this.id = id;
        this.userID = userID;
        this.productID = productID;
        this.comment = comment;
        this.rating = rating;
        this.createAt = createAt;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public User getUserID() {
        return userID;
    }
    public void setUserID(User userID) {
        this.userID = userID;
    }
    public Product getProductID() {
        return productID;
    }
    public void setProductID(Product productID) {
        this.productID = productID;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    
}
