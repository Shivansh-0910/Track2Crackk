package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "http://localhost:5173")
public class TopicsController {

    @GetMapping
    public ResponseEntity<?> getTopics() {
        // Updated with actual problem counts from our comprehensive LeetCode collection
        List<Map<String, Object>> topics = Arrays.asList(
            Map.of(
                "_id", "arrays",
                "name", "Arrays",
                "icon", "📊",
                "description", "Array manipulation, searching, sorting, and two-pointer techniques",
                "totalProblems", 80,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 35, "medium", 40, "hard", 5)
            ),
            Map.of(
                "_id", "strings",
                "name", "Strings",
                "icon", "🔤",
                "description", "String manipulation, pattern matching, and character operations",
                "totalProblems", 60,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 35, "medium", 20, "hard", 5)
            ),
            Map.of(
                "_id", "linked-lists",
                "name", "Linked Lists",
                "icon", "🔗",
                "description", "Linked list operations, cycles, merging, and reversal",
                "totalProblems", 30,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 15, "medium", 12, "hard", 3)
            ),
            Map.of(
                "_id", "trees",
                "name", "Trees",
                "icon", "🌳",
                "description", "Binary trees, BST, traversals, and tree construction",
                "totalProblems", 50,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 20, "medium", 25, "hard", 5)
            ),
            Map.of(
                "_id", "dynamic-programming",
                "name", "Dynamic Programming",
                "icon", "🧮",
                "description", "Optimization problems, memoization, and bottom-up approaches",
                "totalProblems", 40,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 8, "medium", 25, "hard", 7)
            ),
            Map.of(
                "_id", "graphs",
                "name", "Graphs",
                "icon", "🗸",
                "description", "Graph traversal, shortest paths, and topological sorting",
                "totalProblems", 25,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 5, "medium", 15, "hard", 5)
            ),
            Map.of(
                "_id", "stack-queue",
                "name", "Stack & Queue",
                "icon", "📚",
                "description", "Stack and queue operations, parentheses matching, and design problems",
                "totalProblems", 20,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 8, "medium", 10, "hard", 2)
            ),
            Map.of(
                "_id", "math",
                "name", "Math & Bit Manipulation",
                "icon", "🔢",
                "description", "Mathematical problems, bit operations, and number theory",
                "totalProblems", 25,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 15, "medium", 8, "hard", 2)
            ),
            Map.of(
                "_id", "design",
                "name", "Design",
                "icon", "⚙️",
                "description", "System design problems and data structure implementation",
                "totalProblems", 15,
                "solvedProblems", 0,
                "accuracy", 0,
                "lastActivity", "2024-08-04",
                "difficulty", Map.of("easy", 3, "medium", 8, "hard", 4)
            )
        );

        Map<String, Object> response = Map.of("topics", topics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<?> getTopicDetails(@PathVariable String topicId) {
        Map<String, Object> topicDetails = getTopicProblemsFromMainDatabase(topicId);
        return ResponseEntity.ok(topicDetails);
    }

    private Map<String, Object> createProblem(String id, String name, String difficulty, String url, int estimatedTime) {
        return Map.of(
            "_id", id,
            "name", name,
            "difficulty", difficulty,
            "status", "Not Started", // Changed to match frontend expectations
            "url", url,
            "estimatedTime", estimatedTime,
            "companies", getCompaniesForProblem(name),
            "frequency", getFrequencyForProblem(id, difficulty)
        );
    }

    private List<String> getCompaniesForProblem(String problemName) {
        // Return realistic company names based on problem popularity
        if (problemName.contains("Two Sum") || problemName.contains("Valid Parentheses") || problemName.contains("Reverse")) {
            return Arrays.asList("Google", "Amazon", "Microsoft", "Facebook", "Apple");
        } else if (problemName.contains("Tree") || problemName.contains("Graph") || problemName.contains("Dynamic")) {
            return Arrays.asList("Google", "Microsoft", "Amazon", "Facebook");
        } else if (problemName.contains("Array") || problemName.contains("String")) {
            return Arrays.asList("Amazon", "Microsoft", "Google", "Apple");
        } else if (problemName.contains("Linked List") || problemName.contains("Stack") || problemName.contains("Queue")) {
            return Arrays.asList("Microsoft", "Google", "Amazon");
        } else {
            return Arrays.asList("Google", "Amazon", "Microsoft");
        }
    }

    private int getFrequencyForProblem(String problemId, String difficulty) {
        // Use problem ID hash to generate consistent frequency values
        int hash = Math.abs(problemId.hashCode());

        switch (difficulty) {
            case "Easy": return 85 + (hash % 15); // 85-99
            case "Medium": return 60 + (hash % 25); // 60-84
            case "Hard": return 30 + (hash % 30); // 30-59
            default: return 70;
        }
    }

    // Get the same problem database used by ProblemsController
    private List<Map<String, Object>> getAllAvailableProblems() {
        // This is the same method from ProblemsController - we need to share this data
        // For now, I'll create a simplified version that references the main problem database
        return Arrays.asList(
            // Arrays problems from the main database
            Map.of("_id", "lc-1", "name", "Two Sum", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/two-sum/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-26", "name", "Remove Duplicates from Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-array/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-27", "name", "Remove Element", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-element/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-35", "name", "Search Insert Position", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-insert-position/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-66", "name", "Plus One", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/plus-one/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-88", "name", "Merge Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/merge-sorted-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-121", "name", "Best Time to Buy and Sell Stock", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-136", "name", "Single Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/single-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-169", "name", "Majority Element", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/majority-element/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-283", "name", "Move Zeroes", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/move-zeroes/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-448", "name", "Find All Numbers Disappeared in an Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-485", "name", "Max Consecutive Ones", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/max-consecutive-ones/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-561", "name", "Array Partition I", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/array-partition-i/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-566", "name", "Reshape the Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/reshape-the-matrix/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-628", "name", "Maximum Product of Three Numbers", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-product-of-three-numbers/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-643", "name", "Maximum Average Subarray I", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-average-subarray-i/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-661", "name", "Image Smoother", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/image-smoother/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-674", "name", "Longest Continuous Increasing Subsequence", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/longest-continuous-increasing-subsequence/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-697", "name", "Degree of an Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/degree-of-an-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-724", "name", "Find Pivot Index", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-pivot-index/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-747", "name", "Largest Number At Least Twice of Others", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/largest-number-at-least-twice-of-others/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-766", "name", "Toeplitz Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/toeplitz-matrix/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-832", "name", "Flipping an Image", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/flipping-an-image/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-867", "name", "Transpose Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/transpose-matrix/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-888", "name", "Fair Candy Swap", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/fair-candy-swap/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-896", "name", "Monotonic Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/monotonic-array/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-905", "name", "Sort Array By Parity", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-array-by-parity/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-922", "name", "Sort Array By Parity II", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-array-by-parity-ii/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-941", "name", "Valid Mountain Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/valid-mountain-array/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-977", "name", "Squares of a Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/squares-of-a-sorted-array/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-989", "name", "Add to Array-Form of Integer", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/add-to-array-form-of-integer/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-999", "name", "Available Captures for Rook", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/available-captures-for-rook/", "estimatedTime", 20, "status", "Not Started"),

            // Medium Arrays problems
            Map.of("_id", "lc-11", "name", "Container With Most Water", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/container-with-most-water/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-15", "name", "3Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-16", "name", "3Sum Closest", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum-closest/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-18", "name", "4Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/4sum/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-31", "name", "Next Permutation", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/next-permutation/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-33", "name", "Search in Rotated Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-in-rotated-sorted-array/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-34", "name", "Find First and Last Position of Element in Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-36", "name", "Valid Sudoku", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/valid-sudoku/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-39", "name", "Combination Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/combination-sum/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-40", "name", "Combination Sum II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/combination-sum-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-48", "name", "Rotate Image", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/rotate-image/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-53", "name", "Maximum Subarray", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-subarray/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-54", "name", "Spiral Matrix", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/spiral-matrix/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-55", "name", "Jump Game", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/jump-game/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-56", "name", "Merge Intervals", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/merge-intervals/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-59", "name", "Spiral Matrix II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/spiral-matrix-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-73", "name", "Set Matrix Zeroes", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/set-matrix-zeroes/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-74", "name", "Search a 2D Matrix", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-a-2d-matrix/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-75", "name", "Sort Colors", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-colors/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-78", "name", "Subsets", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/subsets/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-79", "name", "Word Search", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/word-search/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-80", "name", "Remove Duplicates from Sorted Array II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-81", "name", "Search in Rotated Sorted Array II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-in-rotated-sorted-array-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-90", "name", "Subsets II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/subsets-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-118", "name", "Pascal's Triangle", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/pascals-triangle/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-119", "name", "Pascal's Triangle II", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/pascals-triangle-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-122", "name", "Best Time to Buy and Sell Stock II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-128", "name", "Longest Consecutive Sequence", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/longest-consecutive-sequence/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-134", "name", "Gas Station", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/gas-station/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-152", "name", "Maximum Product Subarray", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-product-subarray/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-153", "name", "Find Minimum in Rotated Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-162", "name", "Find Peak Element", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-peak-element/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-167", "name", "Two Sum II - Input Array Is Sorted", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-189", "name", "Rotate Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/rotate-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-209", "name", "Minimum Size Subarray Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/minimum-size-subarray-sum/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-215", "name", "Kth Largest Element in an Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/kth-largest-element-in-an-array/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-217", "name", "Contains Duplicate", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/contains-duplicate/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-238", "name", "Product of Array Except Self", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/product-of-array-except-self/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-240", "name", "Search a 2D Matrix II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-a-2d-matrix-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-243", "name", "Shortest Word Distance", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/shortest-word-distance/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-245", "name", "Shortest Word Distance III", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/shortest-word-distance-iii/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-252", "name", "Meeting Rooms", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/meeting-rooms/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-253", "name", "Meeting Rooms II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/meeting-rooms-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-259", "name", "3Sum Smaller", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum-smaller/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-268", "name", "Missing Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/missing-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-274", "name", "H-Index", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/h-index/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-275", "name", "H-Index II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/h-index-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-406", "name", "Queue Reconstruction by Height", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/queue-reconstruction-by-height/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-414", "name", "Third Maximum Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/third-maximum-number/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-419", "name", "Battleships in a Board", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/battleships-in-a-board/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-435", "name", "Non-overlapping Intervals", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/non-overlapping-intervals/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-436", "name", "Find Right Interval", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-right-interval/", "estimatedTime", 30, "status", "Not Started"),

            // Hard Arrays problems
            Map.of("_id", "lc-4", "name", "Median of Two Sorted Arrays", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/median-of-two-sorted-arrays/", "estimatedTime", 45, "status", "Not Started"),
            Map.of("_id", "lc-164", "name", "Maximum Gap", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-gap/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-407", "name", "Trapping Rain Water II", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/trapping-rain-water-ii/", "estimatedTime", 45, "status", "Not Started"),

            // Strings problems
            Map.of("_id", "lc-13", "name", "Roman to Integer", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/roman-to-integer/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-14", "name", "Longest Common Prefix", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-common-prefix/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-28", "name", "Find the Index of the First Occurrence in a String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-58", "name", "Length of Last Word", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/length-of-last-word/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-67", "name", "Add Binary", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/add-binary/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-125", "name", "Valid Palindrome", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-palindrome/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-242", "name", "Valid Anagram", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-anagram/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-344", "name", "Reverse String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-string/", "estimatedTime", 10, "status", "Not Started"),
            Map.of("_id", "lc-383", "name", "Ransom Note", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/ransom-note/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-387", "name", "First Unique Character in a String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/first-unique-character-in-a-string/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-151", "name", "Reverse Words in a String", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-words-in-a-string/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-165", "name", "Compare Version Numbers", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/compare-version-numbers/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-187", "name", "Repeated DNA Sequences", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/repeated-dna-sequences/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-205", "name", "Isomorphic Strings", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/isomorphic-strings/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-246", "name", "Strobogrammatic Number", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-247", "name", "Strobogrammatic Number II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-248", "name", "Strobogrammatic Number III", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number-iii/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-249", "name", "Group Shifted Strings", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/group-shifted-strings/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-266", "name", "Palindrome Permutation", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/palindrome-permutation/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-267", "name", "Palindrome Permutation II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/palindrome-permutation-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-271", "name", "Encode and Decode Strings", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/encode-and-decode-strings/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-273", "name", "Integer to English Words", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/integer-to-english-words/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-159", "name", "Longest Substring with At Most Two Distinct Characters", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-161", "name", "One Edit Distance", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/one-edit-distance/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-186", "name", "Reverse Words in a String II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-words-in-a-string-ii/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-408", "name", "Valid Word Abbreviation", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-word-abbreviation/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-409", "name", "Longest Palindrome", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-palindrome/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-418", "name", "Sentence Screen Fitting", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/sentence-screen-fitting/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-420", "name", "Strong Password Checker", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strong-password-checker/", "estimatedTime", 45, "status", "Not Started"),
            Map.of("_id", "lc-422", "name", "Valid Word Square", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-word-square/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-423", "name", "Reconstruct Original Digits from English", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reconstruct-original-digits-from-english/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-424", "name", "Longest Repeating Character Replacement", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-repeating-character-replacement/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-438", "name", "Find All Anagrams in a String", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/find-all-anagrams-in-a-string/", "estimatedTime", 25, "status", "Not Started"),

            // Linked Lists problems
            Map.of("_id", "lc-21", "name", "Merge Two Sorted Lists", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/merge-two-sorted-lists/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-83", "name", "Remove Duplicates from Sorted List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-list/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-141", "name", "Linked List Cycle", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/linked-list-cycle/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-160", "name", "Intersection of Two Linked Lists", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/intersection-of-two-linked-lists/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-203", "name", "Remove Linked List Elements", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-linked-list-elements/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-206", "name", "Reverse Linked List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-linked-list/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-234", "name", "Palindrome Linked List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/palindrome-linked-list/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-2", "name", "Add Two Numbers", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/add-two-numbers/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-19", "name", "Remove Nth Node From End of List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-nth-node-from-end-of-list/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-24", "name", "Swap Nodes in Pairs", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/swap-nodes-in-pairs/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-61", "name", "Rotate List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/rotate-list/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-82", "name", "Remove Duplicates from Sorted List II", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-86", "name", "Partition List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/partition-list/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-92", "name", "Reverse Linked List II", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-linked-list-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-109", "name", "Convert Sorted List to Binary Search Tree", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-147", "name", "Insertion Sort List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/insertion-sort-list/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-430", "name", "Flatten a Multilevel Doubly Linked List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-25", "name", "Reverse Nodes in k-Group", "difficulty", "Hard", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-nodes-in-k-group/", "estimatedTime", 35, "status", "Not Started"),

            // Trees problems
            Map.of("_id", "lc-94", "name", "Binary Tree Inorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-inorder-traversal/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-100", "name", "Same Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/same-tree/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-101", "name", "Symmetric Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/symmetric-tree/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-104", "name", "Maximum Depth of Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/maximum-depth-of-binary-tree/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-110", "name", "Balanced Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/balanced-binary-tree/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-111", "name", "Minimum Depth of Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/minimum-depth-of-binary-tree/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-112", "name", "Path Sum", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-144", "name", "Binary Tree Preorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-preorder-traversal/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-145", "name", "Binary Tree Postorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-postorder-traversal/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-226", "name", "Invert Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/invert-binary-tree/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-108", "name", "Convert Sorted Array to Binary Search Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-250", "name", "Count Univalue Subtrees", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/count-univalue-subtrees/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-255", "name", "Verify Preorder Sequence in Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-257", "name", "Binary Tree Paths", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-paths/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-270", "name", "Closest Binary Search Tree Value", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/closest-binary-search-tree-value/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-272", "name", "Closest Binary Search Tree Value II", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/closest-binary-search-tree-value-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-95", "name", "Unique Binary Search Trees II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/unique-binary-search-trees-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-96", "name", "Unique Binary Search Trees", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/unique-binary-search-trees/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-99", "name", "Recover Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/recover-binary-search-tree/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-103", "name", "Binary Tree Zigzag Level Order Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-107", "name", "Binary Tree Level Order Traversal II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-level-order-traversal-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-113", "name", "Path Sum II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-129", "name", "Sum Root to Leaf Numbers", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/sum-root-to-leaf-numbers/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-156", "name", "Binary Tree Upside Down", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-upside-down/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-404", "name", "Sum of Left Leaves", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/sum-of-left-leaves/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-426", "name", "Convert Binary Search Tree to Sorted Doubly Linked List", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-427", "name", "Construct Quad Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/construct-quad-tree/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-428", "name", "Serialize and Deserialize N-ary Tree", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/serialize-and-deserialize-n-ary-tree/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-429", "name", "N-ary Tree Level Order Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/n-ary-tree-level-order-traversal/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-431", "name", "Encode N-ary Tree to Binary Tree", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-437", "name", "Path Sum III", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum-iii/", "estimatedTime", 25, "status", "Not Started"),

            // Dynamic Programming problems
            Map.of("_id", "lc-70", "name", "Climbing Stairs", "difficulty", "Easy", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/climbing-stairs/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-62", "name", "Unique Paths", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/unique-paths/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-63", "name", "Unique Paths II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/unique-paths-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-64", "name", "Minimum Path Sum", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/minimum-path-sum/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-120", "name", "Triangle", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/triangle/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-139", "name", "Word Break", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/word-break/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-198", "name", "House Robber", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-213", "name", "House Robber II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-221", "name", "Maximal Square", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/maximal-square/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-241", "name", "Different Ways to Add Parentheses", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/different-ways-to-add-parentheses/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-256", "name", "Paint House", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/paint-house/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-264", "name", "Ugly Number II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/ugly-number-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-279", "name", "Perfect Squares", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/perfect-squares/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-300", "name", "Longest Increasing Subsequence", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/longest-increasing-subsequence/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-309", "name", "Best Time to Buy and Sell Stock with Cooldown", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-322", "name", "Coin Change", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/coin-change/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-337", "name", "House Robber III", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber-iii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-343", "name", "Integer Break", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/integer-break/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-368", "name", "Largest Divisible Subset", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/largest-divisible-subset/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-375", "name", "Guess Number Higher or Lower II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/guess-number-higher-or-lower-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-377", "name", "Combination Sum IV", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/combination-sum-iv/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-392", "name", "Is Subsequence", "difficulty", "Easy", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/is-subsequence/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-403", "name", "Frog Jump", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/frog-jump/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-410", "name", "Split Array Largest Sum", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/split-array-largest-sum/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-413", "name", "Arithmetic Slices", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/arithmetic-slices/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-416", "name", "Partition Equal Subset Sum", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/partition-equal-subset-sum/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-115", "name", "Distinct Subsequences", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/distinct-subsequences/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-174", "name", "Dungeon Game", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/dungeon-game/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-188", "name", "Best Time to Buy and Sell Stock IV", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-265", "name", "Paint House II", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/paint-house-ii/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-354", "name", "Russian Doll Envelopes", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/russian-doll-envelopes/", "estimatedTime", 40, "status", "Not Started"),

            // Graphs problems
            Map.of("_id", "lc-200", "name", "Number of Islands", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/number-of-islands/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-130", "name", "Surrounded Regions", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/surrounded-regions/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-133", "name", "Clone Graph", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/clone-graph/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-207", "name", "Course Schedule", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-210", "name", "Course Schedule II", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-261", "name", "Graph Valid Tree", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/graph-valid-tree/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-323", "name", "Number of Connected Components in an Undirected Graph", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-332", "name", "Reconstruct Itinerary", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/reconstruct-itinerary/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-399", "name", "Evaluate Division", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/evaluate-division/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-417", "name", "Pacific Atlantic Water Flow", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/pacific-atlantic-water-flow/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-433", "name", "Minimum Genetic Mutation", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/minimum-genetic-mutation/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-269", "name", "Alien Dictionary", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/alien-dictionary/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-329", "name", "Longest Increasing Path in a Matrix", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/longest-increasing-path-in-a-matrix/", "estimatedTime", 40, "status", "Not Started"),

            // Stack & Queue problems
            Map.of("_id", "lc-20", "name", "Valid Parentheses", "difficulty", "Easy", "topic", "Stack",
                   "url", "https://leetcode.com/problems/valid-parentheses/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-155", "name", "Min Stack", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/min-stack/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-71", "name", "Simplify Path", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/simplify-path/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-150", "name", "Evaluate Reverse Polish Notation", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-225", "name", "Implement Stack using Queues", "difficulty", "Easy", "topic", "Stack",
                   "url", "https://leetcode.com/problems/implement-stack-using-queues/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-227", "name", "Basic Calculator II", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-232", "name", "Implement Queue using Stacks", "difficulty", "Easy", "topic", "Queue",
                   "url", "https://leetcode.com/problems/implement-queue-using-stacks/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-341", "name", "Flatten Nested List Iterator", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/flatten-nested-list-iterator/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-394", "name", "Decode String", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/decode-string/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-402", "name", "Remove K Digits", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/remove-k-digits/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-439", "name", "Ternary Expression Parser", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/ternary-expression-parser/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-84", "name", "Largest Rectangle in Histogram", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/largest-rectangle-in-histogram/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-85", "name", "Maximal Rectangle", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/maximal-rectangle/", "estimatedTime", 45, "status", "Not Started"),
            Map.of("_id", "lc-224", "name", "Basic Calculator", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-239", "name", "Sliding Window Maximum", "difficulty", "Hard", "topic", "Queue",
                   "url", "https://leetcode.com/problems/sliding-window-maximum/", "estimatedTime", 35, "status", "Not Started"),

            // Math & Bit Manipulation problems
            Map.of("_id", "lc-7", "name", "Reverse Integer", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/reverse-integer/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-9", "name", "Palindrome Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/palindrome-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-69", "name", "Sqrt(x)", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/sqrtx/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-137", "name", "Single Number II", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/single-number-ii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-166", "name", "Fraction to Recurring Decimal", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/fraction-to-recurring-decimal/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-168", "name", "Excel Sheet Column Title", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/excel-sheet-column-title/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-171", "name", "Excel Sheet Column Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/excel-sheet-column-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-172", "name", "Factorial Trailing Zeroes", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/factorial-trailing-zeroes/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-190", "name", "Reverse Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/reverse-bits/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-191", "name", "Number of 1 Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/number-of-1-bits/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-201", "name", "Bitwise AND of Numbers Range", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/bitwise-and-of-numbers-range/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-202", "name", "Happy Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/happy-number/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-204", "name", "Count Primes", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/count-primes/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-223", "name", "Rectangle Area", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/rectangle-area/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-231", "name", "Power of Two", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/power-of-two/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-258", "name", "Add Digits", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/add-digits/", "estimatedTime", 10, "status", "Not Started"),
            Map.of("_id", "lc-260", "name", "Single Number III", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/single-number-iii/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-263", "name", "Ugly Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/ugly-number/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-292", "name", "Nim Game", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/nim-game/", "estimatedTime", 10, "status", "Not Started"),
            Map.of("_id", "lc-318", "name", "Maximum Product of Word Lengths", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/maximum-product-of-word-lengths/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-319", "name", "Bulb Switcher", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/bulb-switcher/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-326", "name", "Power of Three", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/power-of-three/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-338", "name", "Counting Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/counting-bits/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-342", "name", "Power of Four", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/power-of-four/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-365", "name", "Water and Jug Problem", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/water-and-jug-problem/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-367", "name", "Valid Perfect Square", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/valid-perfect-square/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-371", "name", "Sum of Two Integers", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/sum-of-two-integers/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-389", "name", "Find the Difference", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/find-the-difference/", "estimatedTime", 15, "status", "Not Started"),
            Map.of("_id", "lc-393", "name", "UTF-8 Validation", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/utf-8-validation/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-400", "name", "Nth Digit", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/nth-digit/", "estimatedTime", 30, "status", "Not Started"),
            Map.of("_id", "lc-401", "name", "Binary Watch", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/binary-watch/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-405", "name", "Convert a Number to Hexadecimal", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/convert-a-number-to-hexadecimal/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-412", "name", "Fizz Buzz", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/fizz-buzz/", "estimatedTime", 10, "status", "Not Started"),
            Map.of("_id", "lc-421", "name", "Maximum XOR of Two Numbers in an Array", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-233", "name", "Number of Digit One", "difficulty", "Hard", "topic", "Math",
                   "url", "https://leetcode.com/problems/number-of-digit-one/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-335", "name", "Self Crossing", "difficulty", "Hard", "topic", "Math",
                   "url", "https://leetcode.com/problems/self-crossing/", "estimatedTime", 45, "status", "Not Started"),

            // Design problems
            Map.of("_id", "lc-146", "name", "LRU Cache", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/lru-cache/", "estimatedTime", 40, "status", "Not Started"),
            Map.of("_id", "lc-157", "name", "Read N Characters Given Read4", "difficulty", "Easy", "topic", "Design",
                   "url", "https://leetcode.com/problems/read-n-characters-given-read4/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-170", "name", "Two Sum III - Data structure design", "difficulty", "Easy", "topic", "Design",
                   "url", "https://leetcode.com/problems/two-sum-iii-data-structure-design/", "estimatedTime", 20, "status", "Not Started"),
            Map.of("_id", "lc-208", "name", "Implement Trie (Prefix Tree)", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/implement-trie-prefix-tree/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-211", "name", "Design Add and Search Words Data Structure", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/design-add-and-search-words-data-structure/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-244", "name", "Shortest Word Distance II", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/shortest-word-distance-ii/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-251", "name", "Flatten 2D Vector", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/flatten-2d-vector/", "estimatedTime", 25, "status", "Not Started"),
            Map.of("_id", "lc-158", "name", "Read N Characters Given Read4 II - Call multiple times", "difficulty", "Hard", "topic", "Design",
                   "url", "https://leetcode.com/problems/read-n-characters-given-read4-ii-call-multiple-times/", "estimatedTime", 35, "status", "Not Started"),
            Map.of("_id", "lc-432", "name", "All O`one Data Structure", "difficulty", "Hard", "topic", "Design",
                   "url", "https://leetcode.com/problems/all-oone-data-structure/", "estimatedTime", 45, "status", "Not Started")
        );
    }

    private Map<String, Object> getTopicProblemsFromMainDatabase(String topicId) {
        List<Map<String, Object>> allProblems = getAllAvailableProblems();

        // Filter problems by topic
        List<Map<String, Object>> topicProblems = allProblems.stream()
            .filter(problem -> {
                String topic = (String) problem.get("topic");
                if (topic == null) return false;

                // Handle special cases for topic matching
                switch (topicId.toLowerCase()) {
                    case "stack-queue":
                        return topic.equals("Stack") || topic.equals("Queue");
                    case "math":
                        return topic.equals("Math") || topic.equals("Bit Manipulation");
                    default:
                        return topic.toLowerCase().equals(getTopicNameForId(topicId).toLowerCase());
                }
            })
            .collect(java.util.stream.Collectors.toList());

        return Map.of(
            "id", topicId,
            "name", getTopicNameForId(topicId),
            "description", getTopicDescription(topicId),
            "difficulty", getTopicDifficulty(topicId),
            "problems", topicProblems
        );
    }

    private String getTopicNameForId(String topicId) {
        switch (topicId.toLowerCase()) {
            case "arrays": return "Arrays";
            case "strings": return "Strings";
            case "linked-lists": return "Linked Lists";
            case "trees": return "Trees";
            case "dynamic-programming": return "Dynamic Programming";
            case "graphs": return "Graphs";
            case "stack-queue": return "Stack & Queue";
            case "math": return "Math & Bit Manipulation";
            case "design": return "Design";
            default: return "Unknown Topic";
        }
    }

    private String getTopicDescription(String topicId) {
        switch (topicId.toLowerCase()) {
            case "arrays": return "Array manipulation, searching, sorting, and two-pointer techniques";
            case "strings": return "String manipulation, pattern matching, and character operations";
            case "linked-lists": return "Linked list operations, cycles, merging, and reversal";
            case "trees": return "Binary trees, BST, traversals, and tree construction";
            case "dynamic-programming": return "Optimization problems, memoization, and bottom-up approaches";
            case "graphs": return "Graph traversal, shortest paths, and topological sorting";
            case "stack-queue": return "Stack and queue operations, parentheses matching, and design problems";
            case "math": return "Mathematical problems, bit operations, and number theory";
            case "design": return "System design problems and data structure implementation";
            default: return "Topic description not available";
        }
    }

    private String getTopicDifficulty(String topicId) {
        switch (topicId.toLowerCase()) {
            case "arrays": return "Easy to Hard";
            case "strings": return "Easy to Medium";
            case "linked-lists": return "Easy to Medium";
            case "trees": return "Easy to Hard";
            case "dynamic-programming": return "Medium to Hard";
            case "graphs": return "Medium to Hard";
            case "stack-queue": return "Easy to Medium";
            case "math": return "Easy to Medium";
            case "design": return "Medium to Hard";
            default: return "Unknown";
        }
    }

    private Map<String, Object> getTopicProblems(String topicId) {
        switch (topicId.toLowerCase()) {
            case "arrays":
                return Map.of(
                    "id", topicId,
                    "name", "Arrays",
                    "description", "Array manipulation, searching, sorting, and two-pointer techniques",
                    "difficulty", "Easy to Hard",
                    "problems", Arrays.asList(
                        createProblem("lc-1", "Two Sum", "Easy", "https://leetcode.com/problems/two-sum/", 15),
                        createProblem("lc-26", "Remove Duplicates from Sorted Array", "Easy", "https://leetcode.com/problems/remove-duplicates-from-sorted-array/", 20),
                        createProblem("lc-27", "Remove Element", "Easy", "https://leetcode.com/problems/remove-element/", 15),
                        createProblem("lc-35", "Search Insert Position", "Easy", "https://leetcode.com/problems/search-insert-position/", 20),
                        createProblem("lc-66", "Plus One", "Easy", "https://leetcode.com/problems/plus-one/", 15),
                        createProblem("lc-88", "Merge Sorted Array", "Easy", "https://leetcode.com/problems/merge-sorted-array/", 25),
                        createProblem("lc-121", "Best Time to Buy and Sell Stock", "Easy", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/", 20),
                        createProblem("lc-136", "Single Number", "Easy", "https://leetcode.com/problems/single-number/", 15),
                        createProblem("lc-169", "Majority Element", "Easy", "https://leetcode.com/problems/majority-element/", 20),
                        createProblem("lc-217", "Contains Duplicate", "Easy", "https://leetcode.com/problems/contains-duplicate/", 15),
                        Map.of("id", "lc-118", "title", "Pascal's Triangle", "difficulty", "Easy", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/pascals-triangle/", "estimatedTime", 25),
                        Map.of("id", "lc-119", "title", "Pascal's Triangle II", "difficulty", "Easy", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/pascals-triangle-ii/", "estimatedTime", 25),
                        Map.of("id", "lc-15", "title", "3Sum", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/3sum/", "estimatedTime", 35),
                        Map.of("id", "lc-16", "title", "3Sum Closest", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/3sum-closest/", "estimatedTime", 30),
                        Map.of("id", "lc-18", "title", "4Sum", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/4sum/", "estimatedTime", 40),
                        Map.of("id", "lc-33", "title", "Search in Rotated Sorted Array", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/search-in-rotated-sorted-array/", "estimatedTime", 30),
                        Map.of("id", "lc-34", "title", "Find First and Last Position of Element in Sorted Array", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/", "estimatedTime", 25),
                        Map.of("id", "lc-39", "title", "Combination Sum", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/combination-sum/", "estimatedTime", 35),
                        Map.of("id", "lc-40", "title", "Combination Sum II", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/combination-sum-ii/", "estimatedTime", 35),
                        Map.of("id", "lc-48", "title", "Rotate Image", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/rotate-image/", "estimatedTime", 25),
                        Map.of("id", "lc-53", "title", "Maximum Subarray", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/maximum-subarray/", "estimatedTime", 20),
                        Map.of("id", "lc-56", "title", "Merge Intervals", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/merge-intervals/", "estimatedTime", 30),
                        Map.of("id", "lc-75", "title", "Sort Colors", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/sort-colors/", "estimatedTime", 20),
                        Map.of("id", "lc-78", "title", "Subsets", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/subsets/", "estimatedTime", 30),
                        Map.of("id", "lc-79", "title", "Word Search", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/word-search/", "estimatedTime", 35),
                        Map.of("id", "lc-90", "title", "Subsets II", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/subsets-ii/", "estimatedTime", 35),
                        Map.of("id", "lc-122", "title", "Best Time to Buy and Sell Stock II", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/", "estimatedTime", 20),
                        Map.of("id", "lc-128", "title", "Longest Consecutive Sequence", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/longest-consecutive-sequence/", "estimatedTime", 30),
                        Map.of("id", "lc-134", "title", "Gas Station", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/gas-station/", "estimatedTime", 30),
                        Map.of("id", "lc-152", "title", "Maximum Product Subarray", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/maximum-product-subarray/", "estimatedTime", 30),
                        Map.of("id", "lc-153", "title", "Find Minimum in Rotated Sorted Array", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/", "estimatedTime", 25),
                        Map.of("id", "lc-162", "title", "Find Peak Element", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/find-peak-element/", "estimatedTime", 25),
                        Map.of("id", "lc-167", "title", "Two Sum II - Input Array Is Sorted", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/", "estimatedTime", 20),
                        Map.of("id", "lc-189", "title", "Rotate Array", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/rotate-array/", "estimatedTime", 25),
                        Map.of("id", "lc-209", "title", "Minimum Size Subarray Sum", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/minimum-size-subarray-sum/", "estimatedTime", 25),
                        Map.of("id", "lc-215", "title", "Kth Largest Element in an Array", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/kth-largest-element-in-an-array/", "estimatedTime", 25),
                        Map.of("id", "lc-238", "title", "Product of Array Except Self", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/product-of-array-except-self/", "estimatedTime", 25)
                    )
                );

            case "strings":
                return Map.of(
                    "id", topicId,
                    "name", "Strings",
                    "description", "String manipulation, pattern matching, and character operations",
                    "difficulty", "Easy to Medium",
                    "problems", Arrays.asList(
                        createProblem("lc-13", "Roman to Integer", "Easy", "https://leetcode.com/problems/roman-to-integer/", 25),
                        createProblem("lc-14", "Longest Common Prefix", "Easy", "https://leetcode.com/problems/longest-common-prefix/", 20),
                        createProblem("lc-28", "Find the Index of the First Occurrence in a String", "Easy", "https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/", 25),
                        createProblem("lc-58", "Length of Last Word", "Easy", "https://leetcode.com/problems/length-of-last-word/", 15),
                        createProblem("lc-67", "Add Binary", "Easy", "https://leetcode.com/problems/add-binary/", 25),
                        createProblem("lc-125", "Valid Palindrome", "Easy", "https://leetcode.com/problems/valid-palindrome/", 20),
                        createProblem("lc-242", "Valid Anagram", "Easy", "https://leetcode.com/problems/valid-anagram/", 15),
                        createProblem("lc-344", "Reverse String", "Easy", "https://leetcode.com/problems/reverse-string/", 10),
                        createProblem("lc-383", "Ransom Note", "Easy", "https://leetcode.com/problems/ransom-note/", 15),
                        createProblem("lc-387", "First Unique Character in a String", "Easy", "https://leetcode.com/problems/first-unique-character-in-a-string/", 20),
                        createProblem("lc-131", "Palindrome Partitioning", "Medium", "https://leetcode.com/problems/palindrome-partitioning/", 35),
                        createProblem("lc-179", "Largest Number", "Medium", "https://leetcode.com/problems/largest-number/", 30)
                    )
                );

            case "linked-lists":
                return Map.of(
                    "id", topicId,
                    "name", "Linked Lists",
                    "description", "Linked list operations, cycles, merging, and reversal",
                    "difficulty", "Easy to Medium",
                    "problems", Arrays.asList(
                        createProblem("lc-21", "Merge Two Sorted Lists", "Easy", "https://leetcode.com/problems/merge-two-sorted-lists/", 25),
                        createProblem("lc-83", "Remove Duplicates from Sorted List", "Easy", "https://leetcode.com/problems/remove-duplicates-from-sorted-list/", 20),
                        createProblem("lc-141", "Linked List Cycle", "Easy", "https://leetcode.com/problems/linked-list-cycle/", 20),
                        createProblem("lc-160", "Intersection of Two Linked Lists", "Easy", "https://leetcode.com/problems/intersection-of-two-linked-lists/", 30),
                        createProblem("lc-203", "Remove Linked List Elements", "Easy", "https://leetcode.com/problems/remove-linked-list-elements/", 20),
                        createProblem("lc-206", "Reverse Linked List", "Easy", "https://leetcode.com/problems/reverse-linked-list/", 25),
                        createProblem("lc-234", "Palindrome Linked List", "Easy", "https://leetcode.com/problems/palindrome-linked-list/", 30),
                        createProblem("lc-138", "Copy List with Random Pointer", "Medium", "https://leetcode.com/problems/copy-list-with-random-pointer/", 35),
                        createProblem("lc-142", "Linked List Cycle II", "Medium", "https://leetcode.com/problems/linked-list-cycle-ii/", 30),
                        createProblem("lc-143", "Reorder List", "Medium", "https://leetcode.com/problems/reorder-list/", 35),
                        createProblem("lc-148", "Sort List", "Medium", "https://leetcode.com/problems/sort-list/", 35),
                        createProblem("lc-237", "Delete Node in a Linked List", "Medium", "https://leetcode.com/problems/delete-node-in-a-linked-list/", 15)
                    )
                );

            case "trees":
                return Map.of(
                    "id", topicId,
                    "name", "Trees",
                    "description", "Binary trees, BST, traversals, and tree construction",
                    "difficulty", "Easy to Hard",
                    "problems", Arrays.asList(
                        createProblem("lc-94", "Binary Tree Inorder Traversal", "Easy", "https://leetcode.com/problems/binary-tree-inorder-traversal/", 25),
                        createProblem("lc-100", "Same Tree", "Easy", "https://leetcode.com/problems/same-tree/", 20),
                        createProblem("lc-101", "Symmetric Tree", "Easy", "https://leetcode.com/problems/symmetric-tree/", 25),
                        createProblem("lc-104", "Maximum Depth of Binary Tree", "Easy", "https://leetcode.com/problems/maximum-depth-of-binary-tree/", 15),
                        createProblem("lc-110", "Balanced Binary Tree", "Easy", "https://leetcode.com/problems/balanced-binary-tree/", 25),
                        createProblem("lc-111", "Minimum Depth of Binary Tree", "Easy", "https://leetcode.com/problems/minimum-depth-of-binary-tree/", 20),
                        createProblem("lc-112", "Path Sum", "Easy", "https://leetcode.com/problems/path-sum/", 20),
                        createProblem("lc-144", "Binary Tree Preorder Traversal", "Easy", "https://leetcode.com/problems/binary-tree-preorder-traversal/", 20),
                        createProblem("lc-145", "Binary Tree Postorder Traversal", "Easy", "https://leetcode.com/problems/binary-tree-postorder-traversal/", 25),
                        createProblem("lc-226", "Invert Binary Tree", "Easy", "https://leetcode.com/problems/invert-binary-tree/", 15),
                        Map.of("id", "lc-105", "title", "Construct Binary Tree from Preorder and Inorder Traversal", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/", "estimatedTime", 40),
                        Map.of("id", "lc-114", "title", "Flatten Binary Tree to Linked List", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/flatten-binary-tree-to-linked-list/", "estimatedTime", 30),
                        Map.of("id", "lc-173", "title", "Binary Search Tree Iterator", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/binary-search-tree-iterator/", "estimatedTime", 30),
                        Map.of("id", "lc-199", "title", "Binary Tree Right Side View", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/binary-tree-right-side-view/", "estimatedTime", 25),
                        Map.of("id", "lc-222", "title", "Count Complete Tree Nodes", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/count-complete-tree-nodes/", "estimatedTime", 30),
                        Map.of("id", "lc-230", "title", "Kth Smallest Element in a BST", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/kth-smallest-element-in-a-bst/", "estimatedTime", 25),
                        Map.of("id", "lc-235", "title", "Lowest Common Ancestor of a Binary Search Tree", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/", "estimatedTime", 25),
                        Map.of("id", "lc-236", "title", "Lowest Common Ancestor of a Binary Tree", "difficulty", "Medium", "status", "NOT_STARTED",
                               "url", "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/", "estimatedTime", 30)
                    )
                );

            case "stack-queue":
                return Map.of(
                    "id", topicId,
                    "name", "Stack & Queue",
                    "description", "Stack and queue operations, parentheses matching, and design problems",
                    "difficulty", "Easy to Medium",
                    "problems", Arrays.asList(
                        createProblem("lc-20", "Valid Parentheses", "Easy", "https://leetcode.com/problems/valid-parentheses/", 20),
                        createProblem("lc-155", "Min Stack", "Medium", "https://leetcode.com/problems/min-stack/", 30),
                        createProblem("lc-225", "Implement Stack using Queues", "Easy", "https://leetcode.com/problems/implement-stack-using-queues/", 25),
                        createProblem("lc-232", "Implement Queue using Stacks", "Easy", "https://leetcode.com/problems/implement-queue-using-stacks/", 25),
                        createProblem("lc-150", "Evaluate Reverse Polish Notation", "Medium", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", 25),
                        createProblem("lc-227", "Basic Calculator II", "Medium", "https://leetcode.com/problems/basic-calculator-ii/", 35)
                    )
                );

            case "dynamic-programming":
                return Map.of(
                    "id", topicId,
                    "name", "Dynamic Programming",
                    "description", "Optimization problems, memoization, and bottom-up approaches",
                    "difficulty", "Medium to Hard",
                    "problems", Arrays.asList(
                        createProblem("lc-70", "Climbing Stairs", "Easy", "https://leetcode.com/problems/climbing-stairs/", 20),
                        createProblem("lc-62", "Unique Paths", "Medium", "https://leetcode.com/problems/unique-paths/", 25),
                        createProblem("lc-63", "Unique Paths II", "Medium", "https://leetcode.com/problems/unique-paths-ii/", 30),
                        createProblem("lc-64", "Minimum Path Sum", "Medium", "https://leetcode.com/problems/minimum-path-sum/", 25),
                        createProblem("lc-139", "Word Break", "Medium", "https://leetcode.com/problems/word-break/", 30),
                        createProblem("lc-198", "House Robber", "Medium", "https://leetcode.com/problems/house-robber/", 25),
                        createProblem("lc-213", "House Robber II", "Medium", "https://leetcode.com/problems/house-robber-ii/", 30),
                        createProblem("lc-221", "Maximal Square", "Medium", "https://leetcode.com/problems/maximal-square/", 35),
                        createProblem("lc-241", "Different Ways to Add Parentheses", "Medium", "https://leetcode.com/problems/different-ways-to-add-parentheses/", 35),
                        createProblem("lc-123", "Best Time to Buy and Sell Stock III", "Hard", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/", 45),
                        createProblem("lc-140", "Word Break II", "Hard", "https://leetcode.com/problems/word-break-ii/", 45)
                    )
                );

            case "graphs":
                return Map.of(
                    "id", topicId,
                    "name", "Graphs",
                    "description", "Graph traversal, shortest paths, and topological sorting",
                    "difficulty", "Medium to Hard",
                    "problems", Arrays.asList(
                        createProblem("lc-200", "Number of Islands", "Medium", "https://leetcode.com/problems/number-of-islands/", 30),
                        createProblem("lc-130", "Surrounded Regions", "Medium", "https://leetcode.com/problems/surrounded-regions/", 35),
                        createProblem("lc-133", "Clone Graph", "Medium", "https://leetcode.com/problems/clone-graph/", 30),
                        createProblem("lc-207", "Course Schedule", "Medium", "https://leetcode.com/problems/course-schedule/", 35),
                        createProblem("lc-210", "Course Schedule II", "Medium", "https://leetcode.com/problems/course-schedule-ii/", 35),
                        createProblem("lc-127", "Word Ladder", "Hard", "https://leetcode.com/problems/word-ladder/", 45)
                    )
                );

            case "math":
                return Map.of(
                    "id", topicId,
                    "name", "Math & Bit Manipulation",
                    "description", "Mathematical problems, bit operations, and number theory",
                    "difficulty", "Easy to Medium",
                    "problems", Arrays.asList(
                        createProblem("lc-7", "Reverse Integer", "Medium", "https://leetcode.com/problems/reverse-integer/", 20),
                        createProblem("lc-9", "Palindrome Number", "Easy", "https://leetcode.com/problems/palindrome-number/", 15),
                        createProblem("lc-69", "Sqrt(x)", "Easy", "https://leetcode.com/problems/sqrtx/", 20),
                        createProblem("lc-190", "Reverse Bits", "Easy", "https://leetcode.com/problems/reverse-bits/", 20),
                        createProblem("lc-191", "Number of 1 Bits", "Easy", "https://leetcode.com/problems/number-of-1-bits/", 15),
                        createProblem("lc-201", "Bitwise AND of Numbers Range", "Medium", "https://leetcode.com/problems/bitwise-and-of-numbers-range/", 25),
                        createProblem("lc-202", "Happy Number", "Easy", "https://leetcode.com/problems/happy-number/", 20),
                        createProblem("lc-223", "Rectangle Area", "Medium", "https://leetcode.com/problems/rectangle-area/", 25),
                        createProblem("lc-231", "Power of Two", "Easy", "https://leetcode.com/problems/power-of-two/", 15)
                    )
                );

            case "design":
                return Map.of(
                    "id", topicId,
                    "name", "Design",
                    "description", "System design problems and data structure implementation",
                    "difficulty", "Medium to Hard",
                    "problems", Arrays.asList(
                        createProblem("lc-146", "LRU Cache", "Medium", "https://leetcode.com/problems/lru-cache/", 40),
                        createProblem("lc-208", "Implement Trie (Prefix Tree)", "Medium", "https://leetcode.com/problems/implement-trie-prefix-tree/", 35),
                        createProblem("lc-211", "Design Add and Search Words Data Structure", "Medium", "https://leetcode.com/problems/design-add-and-search-words-data-structure/", 35),
                        createProblem("lc-224", "Basic Calculator", "Hard", "https://leetcode.com/problems/basic-calculator/", 45)
                    )
                );

            default:
                return Map.of(
                    "id", topicId,
                    "name", "Topic Not Found",
                    "description", "The requested topic was not found",
                    "difficulty", "Unknown",
                    "problems", Arrays.asList()
                );
        }
    }
}