package com.chandan.document_summarizer.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter implements Filter {

    private final Bucket bucket;

    public RateLimitFilter() {
        // Limit to 10 requests per minute
        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(10, Duration.ofMinutes(1))
                .build();
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Only rate limit the summarize endpoint
        if (httpRequest.getRequestURI().startsWith("/api/v1/documents/summarize")) {
            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response); // Allow request
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.getWriter().write("{\"summary\": \"Rate limit exceeded. Please wait a minute before trying again.\"}");
                httpResponse.setContentType("application/json");
            }
        } else {
            chain.doFilter(request, response); // Ignore static frontend files
        }
    }
}