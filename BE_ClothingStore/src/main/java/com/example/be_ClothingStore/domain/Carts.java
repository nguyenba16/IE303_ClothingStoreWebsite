package com.example.be_ClothingStore.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Carts")
public class Carts {
    @Id
    private String id;
    @DBRef
    private Users userID;
    // Map<productId, Map<size+color, quantity>>
    private Map<String, Map<String, Integer>> items = new HashMap<>();

    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    public Carts() {
        this.items = new HashMap<>();
    }

    public Carts(String id, Users userID, Map<String, Map<String, Integer>> items, LocalDateTime createAt,
            LocalDateTime updateAt) {
        this.id = id;
        this.userID = userID;
        this.items = items;
        this.createAt = createAt;
        this.updateAt = updateAt;
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

    public Map<String, Map<String, Integer>> getItems() {
        return items;
    }

    public void setItems(Map<String, Map<String, Integer>> items) {
        this.items = items;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void addItem(String productId, String size, String color, int quantity) {
        String key = size + "_" + color;
        items.computeIfAbsent(productId, k -> new HashMap<>())
                .merge(key, quantity, Integer::sum);
    }

    public void removeItem(String productId, String size, String color) {
        String key = size + "_" + color;
        Map<String, Integer> productItems = items.get(productId);
        if (productItems != null) {
            productItems.remove(key);
            if (productItems.isEmpty()) {
                items.remove(productId);
            }
        }
    }

    public void updateItemQuantity(String productId, String size, String color, int quantity) {
        String key = size + "_" + color;
        Map<String, Integer> productItems = items.get(productId);
        if (productItems != null) {
            if (quantity <= 0) {
                productItems.remove(key);
                if (productItems.isEmpty()) {
                    items.remove(productId);
                }
            } else {
                productItems.put(key, quantity);
            }
        }
    }

    public void clear() {
        items.clear();
    }
}
