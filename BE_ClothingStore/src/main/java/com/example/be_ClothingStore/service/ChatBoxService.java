package com.example.be_ClothingStore.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.ChatBox.PromptRequest;
import com.example.be_ClothingStore.repository.ProductRepository;

@Service
public class ChatBoxService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, List<PromptRequest>> chatHistories = new HashMap<>();
    private final ProductRepository productRepository;

    private final String GeminiAPIUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=";

    @Value("${GEMINI_KEY}")
    private String apiKey;

    public ChatBoxService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public String uploadGuideFile() throws IOException {
        String url = "https://generativelanguage.googleapis.com/upload/v1beta/files?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Goog-Upload-Protocol", "raw");
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("X-Goog-Upload-File-Name", "guide.txt");

        byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/gemini-api-guide.txt"));

        HttpEntity<byte[]> request = new HttpEntity<>(bytes, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return ((Map<?, ?>) response.getBody().get("file")).get("uri").toString();
    }

    public String sendMessage(String prompt) {
    // Tạo nội dung gửi
    List<Map<String, Object>> contents = List.of(
        Map.of(
            "role", "user",
            "parts", List.of(Map.of("text", prompt))
        )
    );

    Map<String, Object> body = Map.of("contents", contents);

    // Header
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    String GeminiUrl = GeminiAPIUrl + apiKey;

    try {
        ResponseEntity<Map> response = restTemplate.postForEntity(GeminiUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object candidatesObj = response.getBody().get("candidates");
            if (candidatesObj instanceof List<?> candidatesList && !candidatesList.isEmpty()) {
                Map<?, ?> candidate = (Map<?, ?>) candidatesList.get(0);
                Map<?, ?> content = (Map<?, ?>) candidate.get("content");
                List<?> parts = (List<?>) content.get("parts");
                if (!parts.isEmpty()) {
                    Map<?, ?> part = (Map<?, ?>) parts.get(0);
                    return (String) part.get("text");
                }
            }
        }
        return "Không nhận được phản hồi phù hợp từ AI.";
    } catch (Exception e) {
        e.printStackTrace();
        return "Lỗi trong quá trình gửi yêu cầu đến Gemini API.";
    }
}


}
