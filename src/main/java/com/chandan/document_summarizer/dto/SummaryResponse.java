package com.chandan.document_summarizer.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryResponse {
    private String originalLength;
    private String summaryLength;
    private String summary;
}