package com.example.be_ClothingStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.be_ClothingStore.domain.Image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // Inject thông tin từ application.properties
    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    public Image uploadImage(MultipartFile file, String color, String oldPublicId) {
        try {
            // Xóa ảnh cũ
            if (oldPublicId != null && !oldPublicId.isEmpty()) {
                cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
            }
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "IE303_ClothingStore"));
            String url = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();
            Image img = new Image(url, publicId, color);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
