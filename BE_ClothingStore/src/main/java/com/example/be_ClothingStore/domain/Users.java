package com.example.be_ClothingStore.domain;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Users")
public class Users {
    @Id 
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String nameAccount;

    @JsonProperty("password")
    private String password;
    private String address;

    private Role role;
    
    private Image avatar;
    private Image imageBody;
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;
    public Users (){}
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
    public void setImageBody(Image imageBody) {
        this.imageBody = imageBody;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getNameAccount() {
        return nameAccount;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    public String getAddress() {
        return address;
    }
    public Role getRole() {
        return role;
    }
    public Image getAvatar() {
        return avatar;
    }
    public Image getImageBody() {
        return imageBody;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}

