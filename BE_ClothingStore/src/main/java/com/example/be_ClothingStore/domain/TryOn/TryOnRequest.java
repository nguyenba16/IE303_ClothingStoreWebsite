package com.example.be_ClothingStore.domain.TryOn;

import lombok.Data;

@Data
public class TryOnRequest {
    private String modelPhoto;
    private String clothingPhoto;
    private String clothingType; // tops, bottoms, one-pieces
}