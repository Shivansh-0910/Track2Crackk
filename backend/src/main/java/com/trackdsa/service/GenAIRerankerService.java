package com.trackdsa.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackdsa.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lightweight GenAI reranker using OpenAI-compatible Chat Completions API via Java 11 HttpClient.
 * - No Spring AI dependency (compatible with Spring Boot 2.7 / Java 11)
 * - Graceful fallback when API key missing or call fails
 */
@Service
public class GenAIRerankerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${genai.openai.api-key:}")
    private String configuredApiKey;

    @Value("${genai.openai.model:gpt-4o-mini}")
    private String model;

    @Value("${genai.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${genai.timeout-ms:2500}")
    private int timeoutMs;

    /**
     * Rerank base recommendations using an LLM and enrich with rationale.
     * Falls back to base list on any error or when API key is not set.
     */
    public List<AIRecommendationService.RecommendationResult> rerankWithGenAI(
            User user,
            List<AIRecommendationService.RecommendationResult> baseResults,
            int limit
    ) {
        if (baseResults == null || baseResults.isEmpty()) {
            return baseResults;
        }

        String apiKey = getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            return baseResults;
        }

        try {
            // Trim candidate list and build compact payload
            int topK = Math.min(25, baseResults.size());
            List<Map<String, Object>> candidates = new ArrayList<>();
            for (int i = 0; i < topK; i++) {
                AIRecommendationService.RecommendationResult rr = baseResults.get(i);
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", rr.getProblem().getId());
                item.put("name", rr.getProblem().getName());
                item.put("topic", rr.getProblem().getTopic());
                item.put("difficulty", rr.getProblem().getDifficulty());
                item.put("companies", safeList(rr.getProblem().getCompanies(), 4));
                item.put("tags", safeList(rr.getProblem().getTags(), 6));
                item.put("patterns", safeList(rr.getProblem().getPatterns(), 6));
                item.put("freq", rr.getProblem().getFrequencyScore());
                item.put("baseScore", rr.getScore());
                candidates.add(item);
            }

            Map<String, Object> userCtx = new LinkedHashMap<>();
            userCtx.put("level", inferLevelFromEmail(user));
            userCtx.put("targets", Optional.ofNullable(user.getTargetCompanies()).orElse(Collections.emptyList()));

            String systemPrompt = "You are a senior DSA coach. Rerank problems to maximize learning effectiveness " +
                    "for the user considering skill gaps, gradual difficulty progression, company relevance, and variety. " +
                    "Prefer medium problems if user appears intermediate, ensure topic diversity, and include at least 1 review-worthy high-frequency item. " +
                    "Respond ONLY as strict JSON with the schema {\"ranked\":[{\"id\":string,\"score\":number,\"reason\":string}]} with ids drawn from candidates. No extra text.";

            Map<String, Object> messages = Map.of(
                    "model", model,
                    "temperature", 0.2,
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", buildUserPrompt(userCtx, candidates, limit))
                    )
            );

            String requestBody = objectMapper.writeValueAsString(messages);

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(timeoutMs))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .timeout(Duration.ofMillis(timeoutMs))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return baseResults;
            }

            // Parse OpenAI response shape
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.size() == 0) {
                return baseResults;
            }
            String content = choices.get(0).path("message").path("content").asText("");
            if (content.isBlank()) {
                return baseResults;
            }

            // Content is expected to be strict JSON, but local models may wrap in code fences or extra text
            JsonNode parsed = tryParseContentToJson(content);
            if (parsed == null) {
                return baseResults;
            }
            JsonNode ranked = parsed.path("ranked");
            if (!ranked.isArray() || ranked.size() == 0) {
                return baseResults;
            }

            Map<String, AIRecommendationService.RecommendationResult> idToBase = baseResults.stream()
                    .collect(Collectors.toMap(rr -> rr.getProblem().getId(), rr -> rr, (a, b) -> a, LinkedHashMap::new));

            List<AIRecommendationService.RecommendationResult> reranked = new ArrayList<>();
            for (JsonNode node : ranked) {
                String id = node.path("id").asText(null);
                if (id == null || !idToBase.containsKey(id)) {
                    continue;
                }
                double score = node.path("score").asDouble(idToBase.get(id).getScore());
                String reason = node.path("reason").asText("GenAI: recommended");
                AIRecommendationService.RecommendationResult base = idToBase.get(id);
                reranked.add(new AIRecommendationService.RecommendationResult(
                        base.getProblem(),
                        score,
                        mergeReasons(base.getReasoning(), reason)
                ));
            }

            // Append any missing items in original order to preserve completeness
            for (AIRecommendationService.RecommendationResult rr : baseResults) {
                boolean exists = reranked.stream().anyMatch(x -> Objects.equals(x.getProblem().getId(), rr.getProblem().getId()));
                if (!exists) {
                    reranked.add(rr);
                }
            }

            // Trim to requested limit
            return reranked.stream().limit(limit).collect(Collectors.toList());

        } catch (Exception e) {
            return baseResults;
        }
    }

    private JsonNode tryParseContentToJson(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception ignored) {
        }
        try {
            String cleaned = content
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```", "")
                    .trim();
            return objectMapper.readTree(cleaned);
        } catch (Exception ignored) {
        }
        try {
            int first = content.indexOf('{');
            int last = content.lastIndexOf('}');
            if (first >= 0 && last > first) {
                String sub = content.substring(first, last + 1);
                return objectMapper.readTree(sub);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getApiKey() {
        if (configuredApiKey != null && !configuredApiKey.isBlank()) {
            return configuredApiKey;
        }
        String env = System.getenv("OPENAI_API_KEY");
        if (env != null && !env.isBlank()) {
            return env;
        }
        return null;
    }

    private List<String> safeList(List<String> input, int max) {
        if (input == null) return Collections.emptyList();
        if (input.size() <= max) return input;
        return new ArrayList<>(input.subList(0, max));
    }

    private String buildUserPrompt(Map<String, Object> userCtx, List<Map<String, Object>> candidates, int limit) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("user", userCtx);
        payload.put("limit", limit);
        payload.put("candidates", candidates);
        payload.put("instructions", List.of(
                "Rerank to maximize learning effectiveness",
                "Balance difficulty and topic variety",
                "Prefer company-relevant items when user targets overlap",
                "Return exactly 'limit' items when possible"
        ));
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String inferLevelFromEmail(User user) {
        // Simple heuristic fallback; real logic could use solved counts
        String email = user != null ? user.getEmail() : null;
        if (email == null) return "intermediate";
        return email.contains("student") ? "beginner" : "intermediate";
    }

    private String mergeReasons(String base, String ai) {
        if (base == null || base.isBlank()) return ai;
        if (ai == null || ai.isBlank()) return base;
        return base + "; " + ai;
    }
}



