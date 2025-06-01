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

    private final String GeminiAPIUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=";

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

    public String sendMessage(String sessionId, String prompt) {
    // Lấy history cho session hoặc khởi tạo mới
    List<PromptRequest> history = chatHistories.computeIfAbsent(sessionId, k -> new ArrayList<>());

    // Nếu history rỗng (tức là lần đầu), thêm hướng dẫn và dữ liệu sản phẩm
    if (history.isEmpty()) {
        StringBuilder guideBuilder = new StringBuilder();
        InputStream inputDocs = getClass().getClassLoader().getResourceAsStream("gemini-api-guide.txt");
        if (inputDocs != null) {
            Scanner guideDocs = new Scanner(inputDocs, StandardCharsets.UTF_8);
            while (guideDocs.hasNextLine()) {
                guideBuilder.append(guideDocs.nextLine()).append("\n");
            }
            guideDocs.close();
        } else {
            guideBuilder.append("Không tìm thấy file hướng dẫn.");
        }
        String guideData = guideBuilder.toString();

        // Đọc từ database
        List<Products> products = productRepository.findAll();
        List<String> productsData = new ArrayList<>();
        for (Products p : products) {
            productsData.add("Tên sản phẩm: " + p.getProductName()
                + ", mô tả: " + p.getDesc()
                + ", giá: " + p.getPrice()
                + ", đánh giá: " + p.getRating()
                + ", tồn kho: " + p.getStock()
                + ", màu: " + String.join(", ", p.getColors())
                + ", size: " + String.join(", ", p.getSizes()));
        }

        // Tạo "hướng dẫn hệ thống" dưới dạng một PromptRequest giả
        PromptRequest systemContext = new PromptRequest();
        systemContext.setSessionId(sessionId);
        systemContext.setRole("user");
        systemContext.setRequestText("Bạn là trợ lý cho website bán quần áo nữ. Dưới đây là mô tả hệ thống:\n"
            + guideData + "\nDanh sách sản phẩm hiện có:\n" + String.join("\n", productsData) );
        history.add(systemContext);
    }
    // + String.join("\n", productsData)
    // Thêm prompt mới từ người dùng
    PromptRequest userPrompt = new PromptRequest();
    userPrompt.setSessionId(sessionId);
    userPrompt.setRequestText(prompt);
    userPrompt.setRole("user");

    // Tạo contents từ toàn bộ history (bao gồm context cố định đầu và các câu hỏi trước đó)
    List<Map<String, Object>> contents = new ArrayList<>();
    for (PromptRequest msg : history) {
        String text = msg.getRequestText() != null ? msg.getRequestText() : msg.getResponseText();
        contents.add(Map.of(
            "role", msg.getRole(),
            "parts", List.of(Map.of("text", text))
        ));
    }

    // Thêm prompt hiện tại vào cuối
    contents.add(Map.of(
        "role", "user",
        "parts", List.of(Map.of("text", prompt))
    ));

    // Chuẩn bị request
    Map<String, Object> body = Map.of("contents", contents);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    System.out.println("sdfsfsfsdf==sf=sd=f======" + request);
    // Gửi request
    String GeminiUrl =  GeminiAPIUrl + apiKey;
    ResponseEntity<Map> response = restTemplate.postForEntity(GeminiUrl, request, Map.class);
    // ResponseEntity<Map> response = null;
    // Xử lý response
    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        Object candidatesObj = response.getBody().get("candidates");
        if (candidatesObj instanceof List<?> candidatesList && !candidatesList.isEmpty()) {
            Map<?, ?> candidate = (Map<?, ?>) candidatesList.get(0);
            Map<?, ?> content = (Map<?, ?>) candidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            if (!parts.isEmpty()) {
                Map<?, ?> part = (Map<?, ?>) parts.get(0);
                String reply = (String) part.get("text");

                // Lưu lịch sử
                PromptRequest aiResponse = new PromptRequest();
                aiResponse.setSessionId(sessionId);
                aiResponse.setResponseText(reply);
                aiResponse.setRole("model");

                history.add(userPrompt);
                history.add(aiResponse);

                return reply;
            }
        }
        return "Không tìm thấy nội dung phù hợp.";
    } else {
        return "Xin lỗi, tôi không thể trả lời câu hỏi lúc này.";
    }
}

}
