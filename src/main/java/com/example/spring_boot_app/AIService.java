package com.example.spring_boot_app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Service for interacting with AI chat models.
 * Follows Spring best practices:
 * - Returns domain objects (List<Company>), not JSON strings
 * - JSON serialization is handled by the controller layer
 * - Includes proper error handling and logging
 */
@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AIService(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * Generates company information using AI.
     * 
     * @return List of Company objects, or empty list if generation fails
     */
    public List<Company> generateCompanyNames() {
        try {
            String jsonPrompt = """
                Generate 3 companies as JSON array: [{"name":"","industry":"","description":""}]
                Return only JSON, no markdown or backticks.
            """;

           

            // Spring AI ChatClient API: Use prompt().user() to set the user message
            // Then call() to execute and content() to get the response as a string
            String jsonResponse = chatClient.prompt()
                    .user(jsonPrompt)
                    .call()
                    .content();
            
            // Parse JSON string to List<Company> using Jackson ObjectMapper
            List<Company> companies = objectMapper.readValue(jsonResponse, new TypeReference<List<Company>>() {});
            
            logger.info("Successfully generated {} companies", companies != null ? companies.size() : 0);
            return companies != null ? companies : Collections.emptyList();
            
        } catch (Exception e) {
            logger.error("Error generating companies", e);
            // Return empty list instead of throwing to prevent breaking the UI
            return Collections.emptyList();
        }
    }
}