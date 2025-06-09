package com.example.be_ClothingStore.service;

import com.example.be_ClothingStore.domain.TryOn.TryOnResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Service
public class TryOnService {

    @Value("${thenewblack.api.email}")
    private String email;

    @Value("${thenewblack.api.password}")
    private String password;

    private final String VTO_URL = "https://thenewblack.ai/api/1.1/wf/vto";
    private final String RESULTS_URL = "https://thenewblack.ai/api/1.1/wf/results";
    private final RestTemplate restTemplate;

    public TryOnService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TryOnResponse generateTryOnImage(String modelPhotoUrl, String clothingPhotoUrl, String clothingType)
            throws IOException {
        // Validate clothing type
        if (!isValidClothingType(clothingType)) {
            throw new IllegalArgumentException("Invalid clothing type. Must be 'tops', 'bottoms' or 'one-pieces'");
        }

        // First request to get ID
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("email", email);
        body.add("password", password);
        body.add("model_photo", modelPhotoUrl);
        body.add("clothing_photo", clothingPhotoUrl);
        body.add("clothing_type", clothingType);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Get ID from first request
            ResponseEntity<String> idResponse = restTemplate.exchange(
                    VTO_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (idResponse.getStatusCode() == HttpStatus.OK && idResponse.getBody() != null) {
                String id = idResponse.getBody();

                // Create response with ID
                TryOnResponse response = new TryOnResponse();
                response.setId(id);
                response.setStatus("processing");
                return response;
            }
            throw new IOException("Failed to generate try-on image");
        } catch (Exception e) {
            System.err.println("Error calling The New Black API: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            throw new IOException("Error processing try-on request. Please try again later.");
        }
    }

    public TryOnResponse getTryOnResult(String id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> resultBody = new LinkedMultiValueMap<>();
        resultBody.add("email", email);
        resultBody.add("password", password);
        resultBody.add("id", id);

        HttpEntity<MultiValueMap<String, String>> resultRequestEntity = new HttpEntity<>(resultBody, headers);

        try {
            ResponseEntity<String> resultResponse = restTemplate.exchange(
                    RESULTS_URL,
                    HttpMethod.POST,
                    resultRequestEntity,
                    String.class);

            if (resultResponse.getStatusCode() == HttpStatus.OK && resultResponse.getBody() != null) {
                TryOnResponse response = new TryOnResponse();
                response.setId(id);
                response.setResultImage(resultResponse.getBody());
                response.setStatus("success");
                return response;
            }
            throw new IOException("Failed to get try-on result");
        } catch (Exception e) {
            System.err.println("Error calling The New Black API: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            throw new IOException("Error getting try-on result. Please try again later.");
        }
    }

    private boolean isValidClothingType(String clothingType) {
        return clothingType != null &&
                (clothingType.equals("tops") ||
                        clothingType.equals("bottoms") ||
                        clothingType.equals("one-pieces"));
    }
}