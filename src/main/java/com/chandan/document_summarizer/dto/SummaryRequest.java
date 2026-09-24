package com.chandan.document_summarizer.dto;
import lombok.Data;

@Data
public class SummaryRequest {
    private String documentText;
    private String summaryStyle; // e.g., "bullet_points", "executive_summary"
}