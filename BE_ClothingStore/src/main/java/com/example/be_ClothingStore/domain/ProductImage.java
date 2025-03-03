package com.example.be_ClothingStore.domain;

public class ProductImage {
    private String url;
    private String publicId;
    private String color;
    public ProductImage() {
    }
    public ProductImage(String url, String publicId, String color) {
        this.url = url;
        this.publicId = publicId;
        this.color = color;
    }
    public String getUrl() {
        return url;
    }
    public String getPublicId() {
        return publicId;
    }
    public String getColor() {
        return color;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    public void setColor(String color) {
        this.color = color;
    }
    
}
