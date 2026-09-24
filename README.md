# AI Document Summarizer

A production-ready REST API and web application that leverages Google's Gemini AI to aggressively compress and summarize large text documents. 

**Live Demo:** [https://spring-ai-document-summarizer.onrender.com/]

## Tech Stack
* **Backend:** Java 17, Spring Boot 3, Maven
* **AI Integration:** Google Gemini API (gemini-3.6-flash)
* **JSON Parsing:** Jackson
* **Frontend:** HTML5, CSS3, Vanilla JavaScript
* **Deployment:** Docker, Render

## Features
* **Aggressive Compression:** Custom prompt engineering reduces document size by over 70% while retaining core context.
* **Dynamic Formatting:** Summarizes text into Executive Summaries, Bullet Points, or simplified explanations.
* **Fault-Tolerant Parsing:** Secure error handling prevents server crashes from failed upstream API calls.
* **Rate Limiting:** Protects the upstream Gemini API quota from abuse.

## Local Setup
1. Clone the repository.
2. Set your API key as an environment variable: `$env:GEMINI_API_KEY="your_key_here"`
3. Run the application: `./mvnw spring-boot:run`
4. Access the UI at `http://localhost:8080`

## API Endpoint
**POST** `/api/v1/documents/summarize`

**Payload:**
```json
{
  "documentText": "Your large text block here...",
  "summaryStyle": "executive_summary"
}