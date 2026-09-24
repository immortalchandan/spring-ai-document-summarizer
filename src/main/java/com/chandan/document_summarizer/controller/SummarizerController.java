package com.chandan.document_summarizer.controller;

import com.chandan.document_summarizer.dto.SummaryRequest;
import com.chandan.document_summarizer.dto.SummaryResponse;
import com.chandan.document_summarizer.service.SummarizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class SummarizerController {

    private final SummarizerService summarizerService;

    @PostMapping("/summarize")
    public ResponseEntity<SummaryResponse> summarizeDocument(@RequestBody SummaryRequest request) {
        try {
            if (request.getDocumentText() == null || request.getDocumentText().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            SummaryResponse response = summarizerService.generateSummary(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}