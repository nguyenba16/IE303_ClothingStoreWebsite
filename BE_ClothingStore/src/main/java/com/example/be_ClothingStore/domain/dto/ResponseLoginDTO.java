package com.example.be_ClothingStore.domain.dto;

public class ResponseLoginDTO {
    private String accessToken;
    public String getAccessToken(){
        return accessToken;
    }
    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}
