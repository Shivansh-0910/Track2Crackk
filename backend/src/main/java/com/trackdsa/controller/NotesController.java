package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:5173")
public class NotesController {

    @GetMapping
    public ResponseEntity<?> getNotes() {
        // Mock data for now - replace with actual service calls
        List<Map<String, Object>> notes = Arrays.asList(
            Map.of(
                "id", "1",
                "title", "Two Sum Solution",
                "content", "Use HashMap to store complements. Time complexity: O(n), Space complexity: O(n)",
                "topic", "Arrays",
                "problemId", "1",
                "createdAt", "2024-01-15T10:30:00Z",
                "updatedAt", "2024-01-15T10:30:00Z"
            ),
            Map.of(
                "id", "2",
                "title", "Valid Parentheses Pattern",
                "content", "Use stack to match opening and closing brackets. Remember to check if stack is empty at the end.",
                "topic", "Stack",
                "problemId", "2", 
                "createdAt", "2024-01-14T15:45:00Z",
                "updatedAt", "2024-01-14T15:45:00Z"
            )
        );
        
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Map<String, Object> noteData) {
        // Mock response - replace with actual service calls
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Note created successfully",
            "note", Map.of(
                "id", "3",
                "title", noteData.get("title"),
                "content", noteData.get("content"),
                "topic", noteData.get("topic"),
                "problemId", noteData.get("problemId"),
                "createdAt", new Date().toString(),
                "updatedAt", new Date().toString()
            )
        );
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(
            @PathVariable String noteId,
            @RequestBody Map<String, Object> noteData) {
        
        // Mock response - replace with actual service calls
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Note updated successfully",
            "note", Map.of(
                "id", noteId,
                "title", noteData.get("title"),
                "content", noteData.get("content"),
                "topic", noteData.get("topic"),
                "problemId", noteData.get("problemId"),
                "updatedAt", new Date().toString()
            )
        );
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable String noteId) {
        // Mock response - replace with actual service calls
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Note deleted successfully"
        );
        
        return ResponseEntity.ok(response);
    }
} 