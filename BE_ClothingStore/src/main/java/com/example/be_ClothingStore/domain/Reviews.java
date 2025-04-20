package com.example.be_ClothingStore.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "Reviews")
public class Reviews {
    @Id
    private String id;
    
    @DBRef
    @JsonIgnoreProperties({"password", "role", "imageBody"})
    private Users userID;
    private String productID;
    private String comment;
    private float rating;
    @CreatedDate
    private LocalDateTime createAt;

    public Reviews(){}
    public Reviews(String id, Users userID, String productID, String comment, float rating, LocalDateTime createAt) {
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
    public Users getUserID() {
        return userID;
    }
    public void setUserID(Users userID) {
        this.userID = userID;
    }
    public String getProductID() {
        return productID;
    }
    public void setProductID(String productID) {
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
