package com.trackdsa.controller;

import com.trackdsa.model.Problem;
import com.trackdsa.repository.ProblemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api/bulk-data")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class BulkDataController {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Import problems from JSON file
    @PostMapping("/import-json")
    public ResponseEntity<?> importFromJson(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            // Parse JSON array of problems
            Problem[] problems = objectMapper.readValue(file.getInputStream(), Problem[].class);
            
            List<Problem> savedProblems = new ArrayList<>();
            for (Problem problem : problems) {
                // Set default values if missing
                if (problem.getCreatedAt() == null) {
                    problem.setCreatedAt(new Date());
                }
                if (problem.isActive() == false) {
                    problem.setActive(true);
                }
                
                Problem saved = problemRepository.save(problem);
                savedProblems.add(saved);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Problems imported successfully");
            response.put("importedCount", savedProblems.size());
            response.put("totalProblems", problemRepository.count());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Import failed: " + e.getMessage()));
        }
    }

    // Add problems from JSON array in request body
    @PostMapping("/add-problems")
    public ResponseEntity<?> addProblems(@RequestBody List<Map<String, Object>> problemsData) {
        try {
            List<Problem> problems = new ArrayList<>();
            
            for (Map<String, Object> data : problemsData) {
                Problem problem = new Problem();
                problem.setName((String) data.get("name"));
                problem.setDescription((String) data.get("description"));
                problem.setTopic((String) data.get("topic"));
                problem.setDifficulty((String) data.get("difficulty"));
                problem.setUrl((String) data.get("url"));
                problem.setFrequencyScore((Integer) data.getOrDefault("frequencyScore", 5));
                problem.setAverageTime(((Number) data.getOrDefault("averageTime", 20)).doubleValue());
                problem.setActive(true);
                problem.setCreatedAt(new Date());
                
                // Handle companies array
                @SuppressWarnings("unchecked")
                List<String> companies = (List<String>) data.get("companies");
                problem.setCompanies(companies);
                
                // Handle tags array
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) data.get("tags");
                problem.setTags(tags);
                
                // Handle patterns array
                @SuppressWarnings("unchecked")
                List<String> patterns = (List<String>) data.get("patterns");
                problem.setPatterns(patterns);
                
                // Set complexity based on difficulty
                switch (problem.getDifficulty().toLowerCase()) {
                    case "easy":
                        problem.setTimeComplexity(1);
                        problem.setSpaceComplexity(1);
                        break;
                    case "medium":
                        problem.setTimeComplexity(2);
                        problem.setSpaceComplexity(2);
                        break;
                    case "hard":
                        problem.setTimeComplexity(3);
                        problem.setSpaceComplexity(3);
                        break;
                }
                
                problems.add(problem);
            }
            
            List<Problem> savedProblems = problemRepository.saveAll(problems);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Problems added successfully");
            response.put("addedCount", savedProblems.size());
            response.put("totalProblems", problemRepository.count());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to add problems: " + e.getMessage()));
        }
    }

    // Import from LeetCode API format (if you scrape LeetCode)
    @PostMapping("/import-leetcode-format")
    public ResponseEntity<?> importLeetCodeFormat(@RequestBody Map<String, Object> leetcodeData) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> problems = (List<Map<String, Object>>) leetcodeData.get("problems");
            
            List<Problem> convertedProblems = new ArrayList<>();
            
            for (Map<String, Object> problemData : problems) {
                Problem problem = new Problem();
                
                // Map LeetCode format to our format
                problem.setName((String) problemData.get("title"));
                problem.setDescription((String) problemData.get("content"));
                problem.setUrl("https://leetcode.com/problems/" + problemData.get("titleSlug"));
                
                // Map difficulty
                String difficulty = (String) problemData.get("difficulty");
                problem.setDifficulty(difficulty.substring(0, 1).toUpperCase() + difficulty.substring(1).toLowerCase());
                
                // Extract topic from tags
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) problemData.get("topicTags");
                if (tags != null && !tags.isEmpty()) {
                    problem.setTopic(mapLeetCodeTopicToOurs(tags.get(0)));
                    problem.setTags(tags);
                }
                
                // Set defaults
                problem.setFrequencyScore(5);
                problem.setAverageTime(20.0);
                problem.setActive(true);
                problem.setCreatedAt(new Date());
                
                convertedProblems.add(problem);
            }
            
            List<Problem> savedProblems = problemRepository.saveAll(convertedProblems);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "LeetCode problems imported successfully");
            response.put("importedCount", savedProblems.size());
            response.put("totalProblems", problemRepository.count());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "LeetCode import failed: " + e.getMessage()));
        }
    }

    // Get template for bulk import
    @GetMapping("/template")
    public ResponseEntity<?> getImportTemplate() {
        List<Map<String, Object>> template = Arrays.asList(
            Map.of(
                "name", "Problem Name",
                "description", "Problem description here",
                "topic", "Arrays",
                "difficulty", "Medium",
                "companies", Arrays.asList("Google", "Amazon"),
                "url", "https://leetcode.com/problems/problem-name/",
                "frequencyScore", 8,
                "tags", Arrays.asList("Array", "Two Pointers"),
                "patterns", Arrays.asList("Two Pointers", "Sliding Window"),
                "averageTime", 20.0
            )
        );
        
        return ResponseEntity.ok(Map.of(
            "template", template,
            "instructions", "Use this format to bulk import problems via POST /api/bulk-data/add-problems"
        ));
    }

    // Helper method to map LeetCode topics to our system
    private String mapLeetCodeTopicToOurs(String leetcodeTag) {
        Map<String, String> topicMapping = Map.of(
            "array", "Arrays",
            "string", "Strings",
            "linked-list", "Linked Lists",
            "tree", "Trees",
            "dynamic-programming", "Dynamic Programming",
            "graph", "Graphs",
            "hash-table", "Hash Tables",
            "stack", "Stacks",
            "heap", "Heaps",
            "queue", "Queues"
        );
        
        return topicMapping.getOrDefault(leetcodeTag.toLowerCase(), "Arrays");
    }
}
