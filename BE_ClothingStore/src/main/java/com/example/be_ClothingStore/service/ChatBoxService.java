package com.example.be_ClothingStore.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.be_ClothingStore.controller.Admin.Product.ProductController;
import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.ChatBox.PromptRequest;
import com.example.be_ClothingStore.repository.ProductRepository;

@Service
public class ChatBoxService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String GeminiAPIUrl;
    private final Map<String, List<PromptRequest>> chatHistories = new HashMap<>();
    private final ProductRepository productRepository;

    public ChatBoxService(@Value("${GEMINI_API_URL}") String geminiAPIUrl, ProductRepository productRepository){
        this.GeminiAPIUrl = geminiAPIUrl;
        this.productRepository = productRepository;
    }

    public String sendMessage(String sessionId, String prompt) {
        List<PromptRequest> history = chatHistories.computeIfAbsent(sessionId, k -> new ArrayList<>());

        // đọc file hướng dẫn trước khi response
        InputStream inputDocs = getClass().getClassLoader().getResourceAsStream("gemini-api-guide.txt");
        StringBuilder guideBuilder = new StringBuilder();
        if (inputDocs != null) {
            Scanner guideDocs = new Scanner(inputDocs, StandardCharsets.UTF_8);
            while (guideDocs.hasNextLine()) {
                guideBuilder.append(guideDocs.nextLine()).append("\n");
            }
            guideDocs.close();
        } else {
            guideBuilder.append("File này không có nội dung!");
        }
        
        String guideData = guideBuilder.toString();

        // Dọc Database
        List<Products> products = this.productRepository.findAll();

        List<String> productsData = new ArrayList<>();
        for (Products i: products){
            String productText = "Tên sản phẩm: " + i.getProductName()
            + ", mô tả sản phẩm: " + i.getDesc()
            + ", giá: " + i.getPrice()
            + ", đánh giá: " + i.getRating()
            + ", số lượng còn trong kho: " + i.getStock()
            + ", danh sách màu: " + String.join(", ", i.getColors())
            + ", danh sách size: " + String.join(", ", i.getSizes());
            productsData.add(productText);
        }
        // Fine-tuning và RAG
        String messageRAG = "Hãy đọc cẩn thận các thông tin về website bán đồ nữ của chúng ta như sau: "+ guideData + "và dữ liệu về các sản phẩm trong database"+ productsData + "để chắt lọc kĩ dữ liệu và trả lời còn nếu không có dữ liệu nào liên quan thì dựa vào hiểu biết của bạn để trả lời câu hỏi của khách hàng như sau: " + prompt;
        
        PromptRequest userPrompt = new PromptRequest();
        userPrompt.setSessionId(sessionId);
        userPrompt.setRequestText(prompt);
        userPrompt.setRole("user");
    
        // Xây mảng contents từ toàn bộ history
        List<Map<String, Object>> contents = new ArrayList<>();
        for (PromptRequest msg : history) {
            contents.add(Map.of(
                "role", msg.getRole(),
                "parts", List.of(Map.of("text", msg.getRequestText() != null ? msg.getRequestText() : msg.getResponseText()))
            ));
        }
    
        // Thêm prompt hiện tại vào cuối contents
        contents.add(Map.of(
            "role", "user",
            "parts", List.of(Map.of("text", messageRAG))
        ));
    
        // Chuẩn bị body request
        Map<String, Object> body = Map.of("contents", contents);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    
        // Gửi request
        ResponseEntity<Map> response = restTemplate.postForEntity(GeminiAPIUrl, request, Map.class);
    
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object candidatesObj = response.getBody().get("candidates");
    
            if (candidatesObj instanceof List<?>) {
                List<?> candidatesList = (List<?>) candidatesObj;
                if (!candidatesList.isEmpty() && candidatesList.get(0) instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> candidates = (Map<String, Object>) candidatesList.get(0);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> content = (Map<String, Object>) candidates.get("content");
                    List<?> parts = (List<?>) content.get("parts");
    
                    if (!parts.isEmpty() && parts.get(0) instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> part = (Map<String, Object>) parts.get(0);
                        String reply = (String) part.get("text");
    
                        // Lưu vào history
                        PromptRequest aiResponse = new PromptRequest();
                        aiResponse.setSessionId(sessionId);
                        aiResponse.setResponseText(reply);
                        aiResponse.setRole("model");
    
                        history.add(userPrompt);
                        history.add(aiResponse);
    
                        return reply;
                    }
                }
            }
            return "Không tìm thấy nội dung phù hợp.";
        } else {
            return "Xin lỗi, tôi không thể trả lời câu hỏi lúc này.";
        }
    }
    
}
