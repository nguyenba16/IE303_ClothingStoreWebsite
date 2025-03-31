// package com.example.be_ClothingStore.service;

// import com.cloudinary.Cloudinary;
// import com.cloudinary.utils.ObjectUtils;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.util.Map;

// @Service
// public class CloudinaryService {

//     private final Cloudinary cloudinary;

//     // Inject thông tin từ application.properties
//     public CloudinaryService(
//             @Value("${cloudinary.cloud-name}") String cloudName,
//             @Value("${cloudinary.api-key}") String apiKey,
//             @Value("${cloudinary.api-secret}") String apiSecret) {
        
//         this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//             "cloud_name", cloudName,
//             "api_key", apiKey,
//             "api_secret", apiSecret
//         ));
//     }

//     // Upload ảnh lên Cloudinary
//     public String uploadImage(MultipartFile file) throws IOException {
//         Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//         return uploadResult.get("secure_url").toString(); // Trả về link ảnh
//     }
// }

