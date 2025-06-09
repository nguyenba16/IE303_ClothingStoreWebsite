package com.example.be_ClothingStore.controller;

import com.example.be_ClothingStore.domain.TryOn.TryOnResponse;
import com.example.be_ClothingStore.service.TryOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/try-on")
@CrossOrigin(origins = "*")
public class TryOnController {

    @Autowired
    private TryOnService tryOnService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateTryOn(
            @RequestParam("modelPhotoUrl") String modelPhotoUrl,
            @RequestParam("clothingPhotoUrl") String clothingPhotoUrl,
            @RequestParam("clothingType") String clothingType) {
        try {
            // Validate input URLs
            if (modelPhotoUrl == null || modelPhotoUrl.trim().isEmpty() ||
                    clothingPhotoUrl == null || clothingPhotoUrl.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Both model photo URL and clothing photo URL are required");
                return ResponseEntity.badRequest().body(error);
            }

            // Generate try-on image
            TryOnResponse response = tryOnService.generateTryOnImage(modelPhotoUrl, clothingPhotoUrl, clothingType);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/result/{id}")
    public ResponseEntity<?> getTryOnResult(@PathVariable("id") String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "ID is required");
                return ResponseEntity.badRequest().body(error);
            }

            TryOnResponse response = tryOnService.getTryOnResult(id);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}