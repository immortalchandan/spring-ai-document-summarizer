package com.chandan.document_summarizer.service;

import com.chandan.document_summarizer.dto.SummaryRequest;
import com.chandan.document_summarizer.dto.SummaryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class SummarizerService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public SummaryResponse generateSummary(SummaryRequest request) throws Exception {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey;

        String prompt = "Format the following text as a strictly concise " + request.getSummaryStyle() + " summary. You must aggressively compress the information into a maximum of 3 sentences or bullet points. Provide ONLY the summary with no introductory or concluding text:\n\n" + request.getDocumentText();

        // Sanitize input to prevent JSON breakage
        String sanitizedText = prompt.replace("\"", "\\\"").replace("\n", "\\n");
        String requestBody = "{\"contents\": [{\"parts\": [{\"text\": \"" + sanitizedText + "\"}]}]}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("GOOGLE API RESPONSE: " + response.body());

        String extractedSummary = extractTextFromJson(response.body());

        return SummaryResponse.builder()
                .originalLength(request.getDocumentText().length() + " characters")
                .summaryLength(extractedSummary.length() + " characters")
                .summary(extractedSummary)
                .build();
    }

    private String extractTextFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error parsing AI response. Please verify the API key and network connection.";
        }
    }
}