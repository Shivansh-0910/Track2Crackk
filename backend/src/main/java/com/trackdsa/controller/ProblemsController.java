package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.trackdsa.model.User;
import com.trackdsa.model.UserProblem;
import com.trackdsa.repository.UserRepository;
import com.trackdsa.repository.UserProblemRepository;
import com.trackdsa.security.JwtUtil;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class ProblemsController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProblemRepository userProblemRepository;

    @GetMapping("/daily-recommendations")
    public ResponseEntity<?> getDailyRecommendations() {
        // Comprehensive list of LeetCode problems with actual links
        List<Map<String, Object>> allProblems = Arrays.asList(
            // Easy Problems - Arrays
            Map.of("_id", "lc-1", "name", "Two Sum", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/two-sum/", "estimatedTime", 15),
            Map.of("_id", "lc-26", "name", "Remove Duplicates from Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-array/", "estimatedTime", 20),
            Map.of("_id", "lc-27", "name", "Remove Element", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-element/", "estimatedTime", 15),
            Map.of("_id", "lc-35", "name", "Search Insert Position", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-insert-position/", "estimatedTime", 20),
            Map.of("_id", "lc-66", "name", "Plus One", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/plus-one/", "estimatedTime", 15),
            Map.of("_id", "lc-88", "name", "Merge Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/merge-sorted-array/", "estimatedTime", 25),
            Map.of("_id", "lc-121", "name", "Best Time to Buy and Sell Stock", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/", "estimatedTime", 20),
            Map.of("_id", "lc-136", "name", "Single Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/single-number/", "estimatedTime", 15),
            Map.of("_id", "lc-169", "name", "Majority Element", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/majority-element/", "estimatedTime", 20),
            Map.of("_id", "lc-217", "name", "Contains Duplicate", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/contains-duplicate/", "estimatedTime", 15),

            // Easy Problems - Strings
            Map.of("_id", "lc-13", "name", "Roman to Integer", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/roman-to-integer/", "estimatedTime", 25),
            Map.of("_id", "lc-14", "name", "Longest Common Prefix", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-common-prefix/", "estimatedTime", 20),
            Map.of("_id", "lc-28", "name", "Find the Index of the First Occurrence in a String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/", "estimatedTime", 25),
            Map.of("_id", "lc-58", "name", "Length of Last Word", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/length-of-last-word/", "estimatedTime", 15),
            Map.of("_id", "lc-67", "name", "Add Binary", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/add-binary/", "estimatedTime", 25),
            Map.of("_id", "lc-125", "name", "Valid Palindrome", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-palindrome/", "estimatedTime", 20),
            Map.of("_id", "lc-242", "name", "Valid Anagram", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-anagram/", "estimatedTime", 15),
            Map.of("_id", "lc-344", "name", "Reverse String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-string/", "estimatedTime", 10),
            Map.of("_id", "lc-383", "name", "Ransom Note", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/ransom-note/", "estimatedTime", 15),
            Map.of("_id", "lc-387", "name", "First Unique Character in a String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/first-unique-character-in-a-string/", "estimatedTime", 20),

            // Easy Problems - Linked Lists
            Map.of("_id", "lc-21", "name", "Merge Two Sorted Lists", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/merge-two-sorted-lists/", "estimatedTime", 25),
            Map.of("_id", "lc-83", "name", "Remove Duplicates from Sorted List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-list/", "estimatedTime", 20),
            Map.of("_id", "lc-141", "name", "Linked List Cycle", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/linked-list-cycle/", "estimatedTime", 20),
            Map.of("_id", "lc-160", "name", "Intersection of Two Linked Lists", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/intersection-of-two-linked-lists/", "estimatedTime", 30),
            Map.of("_id", "lc-203", "name", "Remove Linked List Elements", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-linked-list-elements/", "estimatedTime", 20),
            Map.of("_id", "lc-206", "name", "Reverse Linked List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-linked-list/", "estimatedTime", 25),
            Map.of("_id", "lc-234", "name", "Palindrome Linked List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/palindrome-linked-list/", "estimatedTime", 30),

            // Easy Problems - Trees
            Map.of("_id", "lc-94", "name", "Binary Tree Inorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-inorder-traversal/", "estimatedTime", 25),
            Map.of("_id", "lc-100", "name", "Same Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/same-tree/", "estimatedTime", 20),
            Map.of("_id", "lc-101", "name", "Symmetric Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/symmetric-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-104", "name", "Maximum Depth of Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/maximum-depth-of-binary-tree/", "estimatedTime", 15),
            Map.of("_id", "lc-110", "name", "Balanced Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/balanced-binary-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-111", "name", "Minimum Depth of Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/minimum-depth-of-binary-tree/", "estimatedTime", 20),
            Map.of("_id", "lc-112", "name", "Path Sum", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum/", "estimatedTime", 20),
            Map.of("_id", "lc-144", "name", "Binary Tree Preorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-preorder-traversal/", "estimatedTime", 20),
            Map.of("_id", "lc-145", "name", "Binary Tree Postorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-postorder-traversal/", "estimatedTime", 25),
            Map.of("_id", "lc-226", "name", "Invert Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/invert-binary-tree/", "estimatedTime", 15),

            // Easy Problems - Stack & Queue
            Map.of("_id", "lc-20", "name", "Valid Parentheses", "difficulty", "Easy", "topic", "Stack",
                   "url", "https://leetcode.com/problems/valid-parentheses/", "estimatedTime", 20),
            Map.of("_id", "lc-155", "name", "Min Stack", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/min-stack/", "estimatedTime", 30),
            Map.of("_id", "lc-225", "name", "Implement Stack using Queues", "difficulty", "Easy", "topic", "Stack",
                   "url", "https://leetcode.com/problems/implement-stack-using-queues/", "estimatedTime", 25),
            Map.of("_id", "lc-232", "name", "Implement Queue using Stacks", "difficulty", "Easy", "topic", "Queue",
                   "url", "https://leetcode.com/problems/implement-queue-using-stacks/", "estimatedTime", 25),

            // Easy Problems - Math & Bit Manipulation
            Map.of("_id", "lc-7", "name", "Reverse Integer", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/reverse-integer/", "estimatedTime", 20),
            Map.of("_id", "lc-9", "name", "Palindrome Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/palindrome-number/", "estimatedTime", 15),
            Map.of("_id", "lc-69", "name", "Sqrt(x)", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/sqrtx/", "estimatedTime", 20),
            Map.of("_id", "lc-70", "name", "Climbing Stairs", "difficulty", "Easy", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/climbing-stairs/", "estimatedTime", 20),
            Map.of("_id", "lc-118", "name", "Pascal's Triangle", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/pascals-triangle/", "estimatedTime", 25),
            Map.of("_id", "lc-119", "name", "Pascal's Triangle II", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/pascals-triangle-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-190", "name", "Reverse Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/reverse-bits/", "estimatedTime", 20),
            Map.of("_id", "lc-191", "name", "Number of 1 Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/number-of-1-bits/", "estimatedTime", 15)
        );

        // Return a random selection of 5-8 problems for daily recommendations
        Collections.shuffle(allProblems);
        List<Map<String, Object>> dailyProblems = allProblems.subList(0, Math.min(8, allProblems.size()));

        Map<String, Object> response = Map.of("problems", dailyProblems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProblems() {
        List<Map<String, Object>> allProblems = getAllLeetCodeProblems();
        Map<String, Object> response = Map.of("problems", allProblems);
        return ResponseEntity.ok(response);
    }

    private List<Map<String, Object>> getAllLeetCodeProblems() {
        return Arrays.asList(
            // Medium Problems - Arrays
            Map.of("_id", "lc-15", "name", "3Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum/", "estimatedTime", 35),
            Map.of("_id", "lc-16", "name", "3Sum Closest", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum-closest/", "estimatedTime", 30),
            Map.of("_id", "lc-18", "name", "4Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/4sum/", "estimatedTime", 40),
            Map.of("_id", "lc-33", "name", "Search in Rotated Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-in-rotated-sorted-array/", "estimatedTime", 30),
            Map.of("_id", "lc-34", "name", "Find First and Last Position of Element in Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/", "estimatedTime", 25),
            Map.of("_id", "lc-39", "name", "Combination Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/combination-sum/", "estimatedTime", 35),
            Map.of("_id", "lc-40", "name", "Combination Sum II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/combination-sum-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-48", "name", "Rotate Image", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/rotate-image/", "estimatedTime", 25),
            Map.of("_id", "lc-53", "name", "Maximum Subarray", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-subarray/", "estimatedTime", 20),
            Map.of("_id", "lc-56", "name", "Merge Intervals", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/merge-intervals/", "estimatedTime", 30),
            Map.of("_id", "lc-62", "name", "Unique Paths", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/unique-paths/", "estimatedTime", 25),
            Map.of("_id", "lc-63", "name", "Unique Paths II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/unique-paths-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-64", "name", "Minimum Path Sum", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/minimum-path-sum/", "estimatedTime", 25),
            Map.of("_id", "lc-75", "name", "Sort Colors", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-colors/", "estimatedTime", 20),
            Map.of("_id", "lc-78", "name", "Subsets", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/subsets/", "estimatedTime", 30),
            Map.of("_id", "lc-79", "name", "Word Search", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/word-search/", "estimatedTime", 35),
            Map.of("_id", "lc-90", "name", "Subsets II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/subsets-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-105", "name", "Construct Binary Tree from Preorder and Inorder Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/", "estimatedTime", 40),
            Map.of("_id", "lc-106", "name", "Construct Binary Tree from Inorder and Postorder Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/", "estimatedTime", 40),
            Map.of("_id", "lc-114", "name", "Flatten Binary Tree to Linked List", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/flatten-binary-tree-to-linked-list/", "estimatedTime", 30),
            Map.of("_id", "lc-116", "name", "Populating Next Right Pointers in Each Node", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/populating-next-right-pointers-in-each-node/", "estimatedTime", 30),
            Map.of("_id", "lc-117", "name", "Populating Next Right Pointers in Each Node II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-122", "name", "Best Time to Buy and Sell Stock II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-123", "name", "Best Time to Buy and Sell Stock III", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/", "estimatedTime", 45),
            Map.of("_id", "lc-124", "name", "Binary Tree Maximum Path Sum", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-maximum-path-sum/", "estimatedTime", 40),
            Map.of("_id", "lc-127", "name", "Word Ladder", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/word-ladder/", "estimatedTime", 45),
            Map.of("_id", "lc-128", "name", "Longest Consecutive Sequence", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/longest-consecutive-sequence/", "estimatedTime", 30),
            Map.of("_id", "lc-130", "name", "Surrounded Regions", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/surrounded-regions/", "estimatedTime", 35),
            Map.of("_id", "lc-131", "name", "Palindrome Partitioning", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/palindrome-partitioning/", "estimatedTime", 35),
            Map.of("_id", "lc-133", "name", "Clone Graph", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/clone-graph/", "estimatedTime", 30),
            Map.of("_id", "lc-134", "name", "Gas Station", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/gas-station/", "estimatedTime", 30),
            Map.of("_id", "lc-138", "name", "Copy List with Random Pointer", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/copy-list-with-random-pointer/", "estimatedTime", 35),
            Map.of("_id", "lc-139", "name", "Word Break", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/word-break/", "estimatedTime", 30),
            Map.of("_id", "lc-140", "name", "Word Break II", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/word-break-ii/", "estimatedTime", 45),
            Map.of("_id", "lc-142", "name", "Linked List Cycle II", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/linked-list-cycle-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-143", "name", "Reorder List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reorder-list/", "estimatedTime", 35),
            Map.of("_id", "lc-146", "name", "LRU Cache", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/lru-cache/", "estimatedTime", 40),
            Map.of("_id", "lc-148", "name", "Sort List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/sort-list/", "estimatedTime", 35),
            Map.of("_id", "lc-150", "name", "Evaluate Reverse Polish Notation", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", "estimatedTime", 25),
            Map.of("_id", "lc-152", "name", "Maximum Product Subarray", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-product-subarray/", "estimatedTime", 30),
            Map.of("_id", "lc-153", "name", "Find Minimum in Rotated Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/", "estimatedTime", 25),
            Map.of("_id", "lc-154", "name", "Find Minimum in Rotated Sorted Array II", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-162", "name", "Find Peak Element", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-peak-element/", "estimatedTime", 25),
            Map.of("_id", "lc-167", "name", "Two Sum II - Input Array Is Sorted", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/", "estimatedTime", 20),
            Map.of("_id", "lc-173", "name", "Binary Search Tree Iterator", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-search-tree-iterator/", "estimatedTime", 30),
            Map.of("_id", "lc-179", "name", "Largest Number", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/largest-number/", "estimatedTime", 30),
            Map.of("_id", "lc-189", "name", "Rotate Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/rotate-array/", "estimatedTime", 25),
            Map.of("_id", "lc-198", "name", "House Robber", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber/", "estimatedTime", 25),
            Map.of("_id", "lc-199", "name", "Binary Tree Right Side View", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-right-side-view/", "estimatedTime", 25),
            Map.of("_id", "lc-200", "name", "Number of Islands", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/number-of-islands/", "estimatedTime", 30),
            Map.of("_id", "lc-201", "name", "Bitwise AND of Numbers Range", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/bitwise-and-of-numbers-range/", "estimatedTime", 25),
            Map.of("_id", "lc-202", "name", "Happy Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/happy-number/", "estimatedTime", 20),
            Map.of("_id", "lc-207", "name", "Course Schedule", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule/", "estimatedTime", 35),
            Map.of("_id", "lc-208", "name", "Implement Trie (Prefix Tree)", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/implement-trie-prefix-tree/", "estimatedTime", 35),
            Map.of("_id", "lc-209", "name", "Minimum Size Subarray Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/minimum-size-subarray-sum/", "estimatedTime", 25),
            Map.of("_id", "lc-210", "name", "Course Schedule II", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-211", "name", "Design Add and Search Words Data Structure", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/design-add-and-search-words-data-structure/", "estimatedTime", 35),
            Map.of("_id", "lc-212", "name", "Word Search II", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/word-search-ii/", "estimatedTime", 50),
            Map.of("_id", "lc-213", "name", "House Robber II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-215", "name", "Kth Largest Element in an Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/kth-largest-element-in-an-array/", "estimatedTime", 25),
            Map.of("_id", "lc-216", "name", "Combination Sum III", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/combination-sum-iii/", "estimatedTime", 30),
            Map.of("_id", "lc-218", "name", "The Skyline Problem", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/the-skyline-problem/", "estimatedTime", 60),
            Map.of("_id", "lc-219", "name", "Contains Duplicate II", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/contains-duplicate-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-220", "name", "Contains Duplicate III", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/contains-duplicate-iii/", "estimatedTime", 40),
            Map.of("_id", "lc-221", "name", "Maximal Square", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/maximal-square/", "estimatedTime", 35),
            Map.of("_id", "lc-222", "name", "Count Complete Tree Nodes", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/count-complete-tree-nodes/", "estimatedTime", 30),
            Map.of("_id", "lc-223", "name", "Rectangle Area", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/rectangle-area/", "estimatedTime", 25),
            Map.of("_id", "lc-224", "name", "Basic Calculator", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator/", "estimatedTime", 45),
            Map.of("_id", "lc-227", "name", "Basic Calculator II", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-228", "name", "Summary Ranges", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/summary-ranges/", "estimatedTime", 20),
            Map.of("_id", "lc-229", "name", "Majority Element II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/majority-element-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-230", "name", "Kth Smallest Element in a BST", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/kth-smallest-element-in-a-bst/", "estimatedTime", 25),
            Map.of("_id", "lc-231", "name", "Power of Two", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/power-of-two/", "estimatedTime", 15),
            Map.of("_id", "lc-235", "name", "Lowest Common Ancestor of a Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-236", "name", "Lowest Common Ancestor of a Binary Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/", "estimatedTime", 30),
            Map.of("_id", "lc-237", "name", "Delete Node in a Linked List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/delete-node-in-a-linked-list/", "estimatedTime", 15),
            Map.of("_id", "lc-238", "name", "Product of Array Except Self", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/product-of-array-except-self/", "estimatedTime", 25),
            Map.of("_id", "lc-239", "name", "Sliding Window Maximum", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sliding-window-maximum/", "estimatedTime", 40),
            Map.of("_id", "lc-240", "name", "Search a 2D Matrix II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-a-2d-matrix-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-241", "name", "Different Ways to Add Parentheses", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/different-ways-to-add-parentheses/", "estimatedTime", 35),

            // Additional Medium Problems - Arrays
            Map.of("_id", "lc-11", "name", "Container With Most Water", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/container-with-most-water/", "estimatedTime", 25),
            Map.of("_id", "lc-31", "name", "Next Permutation", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/next-permutation/", "estimatedTime", 30),
            Map.of("_id", "lc-36", "name", "Valid Sudoku", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/valid-sudoku/", "estimatedTime", 25),
            Map.of("_id", "lc-54", "name", "Spiral Matrix", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/spiral-matrix/", "estimatedTime", 25),
            Map.of("_id", "lc-55", "name", "Jump Game", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/jump-game/", "estimatedTime", 20),
            Map.of("_id", "lc-59", "name", "Spiral Matrix II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/spiral-matrix-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-73", "name", "Set Matrix Zeroes", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/set-matrix-zeroes/", "estimatedTime", 25),
            Map.of("_id", "lc-74", "name", "Search a 2D Matrix", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-a-2d-matrix/", "estimatedTime", 20),
            Map.of("_id", "lc-80", "name", "Remove Duplicates from Sorted Array II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-81", "name", "Search in Rotated Sorted Array II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-in-rotated-sorted-array-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-120", "name", "Triangle", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/triangle/", "estimatedTime", 25),
            Map.of("_id", "lc-137", "name", "Single Number II", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/single-number-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-151", "name", "Reverse Words in a String", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-words-in-a-string/", "estimatedTime", 20),
            Map.of("_id", "lc-165", "name", "Compare Version Numbers", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/compare-version-numbers/", "estimatedTime", 25),
            Map.of("_id", "lc-166", "name", "Fraction to Recurring Decimal", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/fraction-to-recurring-decimal/", "estimatedTime", 35),
            Map.of("_id", "lc-172", "name", "Factorial Trailing Zeroes", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/factorial-trailing-zeroes/", "estimatedTime", 20),
            Map.of("_id", "lc-187", "name", "Repeated DNA Sequences", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/repeated-dna-sequences/", "estimatedTime", 25),
            Map.of("_id", "lc-204", "name", "Count Primes", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/count-primes/", "estimatedTime", 25),
            Map.of("_id", "lc-205", "name", "Isomorphic Strings", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/isomorphic-strings/", "estimatedTime", 20),
            Map.of("_id", "lc-217", "name", "Contains Duplicate", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/contains-duplicate/", "estimatedTime", 15),
            Map.of("_id", "lc-243", "name", "Shortest Word Distance", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/shortest-word-distance/", "estimatedTime", 15),
            Map.of("_id", "lc-244", "name", "Shortest Word Distance II", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/shortest-word-distance-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-245", "name", "Shortest Word Distance III", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/shortest-word-distance-iii/", "estimatedTime", 20),
            Map.of("_id", "lc-246", "name", "Strobogrammatic Number", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number/", "estimatedTime", 15),
            Map.of("_id", "lc-247", "name", "Strobogrammatic Number II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-248", "name", "Strobogrammatic Number III", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strobogrammatic-number-iii/", "estimatedTime", 40),
            Map.of("_id", "lc-249", "name", "Group Shifted Strings", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/group-shifted-strings/", "estimatedTime", 25),
            Map.of("_id", "lc-250", "name", "Count Univalue Subtrees", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/count-univalue-subtrees/", "estimatedTime", 25),
            Map.of("_id", "lc-251", "name", "Flatten 2D Vector", "difficulty", "Medium", "topic", "Design",
                   "url", "https://leetcode.com/problems/flatten-2d-vector/", "estimatedTime", 25),
            Map.of("_id", "lc-252", "name", "Meeting Rooms", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/meeting-rooms/", "estimatedTime", 15),
            Map.of("_id", "lc-253", "name", "Meeting Rooms II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/meeting-rooms-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-254", "name", "Factor Combinations", "difficulty", "Medium", "topic", "Backtracking",
                   "url", "https://leetcode.com/problems/factor-combinations/", "estimatedTime", 30),
            Map.of("_id", "lc-255", "name", "Verify Preorder Sequence in Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-256", "name", "Paint House", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/paint-house/", "estimatedTime", 25),
            Map.of("_id", "lc-257", "name", "Binary Tree Paths", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-paths/", "estimatedTime", 20),
            Map.of("_id", "lc-258", "name", "Add Digits", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/add-digits/", "estimatedTime", 10),
            Map.of("_id", "lc-259", "name", "3Sum Smaller", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum-smaller/", "estimatedTime", 25),
            Map.of("_id", "lc-260", "name", "Single Number III", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/single-number-iii/", "estimatedTime", 30),
            Map.of("_id", "lc-261", "name", "Graph Valid Tree", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/graph-valid-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-262", "name", "Trips and Users", "difficulty", "Hard", "topic", "Database",
                   "url", "https://leetcode.com/problems/trips-and-users/", "estimatedTime", 35),
            Map.of("_id", "lc-263", "name", "Ugly Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/ugly-number/", "estimatedTime", 15),
            Map.of("_id", "lc-264", "name", "Ugly Number II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/ugly-number-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-265", "name", "Paint House II", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/paint-house-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-266", "name", "Palindrome Permutation", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/palindrome-permutation/", "estimatedTime", 15),
            Map.of("_id", "lc-267", "name", "Palindrome Permutation II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/palindrome-permutation-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-268", "name", "Missing Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/missing-number/", "estimatedTime", 15),
            Map.of("_id", "lc-269", "name", "Alien Dictionary", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/alien-dictionary/", "estimatedTime", 40),
            Map.of("_id", "lc-270", "name", "Closest Binary Search Tree Value", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/closest-binary-search-tree-value/", "estimatedTime", 15),
            Map.of("_id", "lc-271", "name", "Encode and Decode Strings", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/encode-and-decode-strings/", "estimatedTime", 25),
            Map.of("_id", "lc-272", "name", "Closest Binary Search Tree Value II", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/closest-binary-search-tree-value-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-273", "name", "Integer to English Words", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/integer-to-english-words/", "estimatedTime", 40),
            Map.of("_id", "lc-274", "name", "H-Index", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/h-index/", "estimatedTime", 25),
            Map.of("_id", "lc-275", "name", "H-Index II", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/h-index-ii/", "estimatedTime", 25)
        );
    }

    @PutMapping("/{problemId}/status")
    public ResponseEntity<?> updateProblemStatus(
            @PathVariable String problemId,
            @RequestBody Map<String, String> payload,
            @RequestHeader("Authorization") String authHeader) {
        
        System.out.println("Received status update request for problem: " + problemId + " with status: " + payload.get("status"));
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        String status = payload.get("status");
        String email = jwtUtil.getEmailFromToken(token);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        String userId = userOpt.get().getId();
        
        // Find existing record or create new one
        Optional<UserProblem> existingRecord = userProblemRepository.findByUserIdAndProblemId(userId, problemId);
        UserProblem userProblem;
        
        if (existingRecord.isPresent()) {
            userProblem = existingRecord.get();
            userProblem.setStatus(status);
            userProblem.setUpdatedAt(new Date());
        } else {
            userProblem = new UserProblem(userId, problemId, status);
        }
        
        userProblemRepository.save(userProblem);
        
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Problem status updated successfully",
            "problemId", problemId,
            "status", status
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/daily-recommendations")
    public ResponseEntity<?> generateDailyRecommendations(@RequestBody Map<String, Object> config) {
        System.out.println("Generating daily recommendations with config: " + config);

        // Get configuration parameters
        int problemCount = (Integer) config.getOrDefault("problemCount", 3);
        List<String> selectedTopics = (List<String>) config.getOrDefault("topics", Arrays.asList("Arrays", "Strings", "Trees"));
        List<String> selectedDifficulties = (List<String>) config.getOrDefault("difficulties", Arrays.asList("Easy", "Medium"));
        // Handle both timeLimit and timeAvailable for frontend compatibility
        final int timeLimit = config.containsKey("timeAvailable") ?
            (Integer) config.get("timeAvailable") :
            (Integer) config.getOrDefault("timeLimit", 60);

        // Get all available problems
        List<Map<String, Object>> allProblems = getAllAvailableProblems();

        // Filter problems based on configuration
        List<Map<String, Object>> filteredProblems = allProblems.stream()
            .filter(problem -> selectedTopics.contains(problem.get("topic")))
            .filter(problem -> selectedDifficulties.contains(problem.get("difficulty")))
            .filter(problem -> (Integer) problem.get("estimatedTime") <= timeLimit)
            .collect(Collectors.toList());

        // Shuffle and select random problems
        Collections.shuffle(filteredProblems);
        List<Map<String, Object>> selectedProblems = filteredProblems.stream()
            .limit(problemCount)
            .collect(Collectors.toList());

        // If not enough problems found, add some defaults BUT STILL RESPECT TIME LIMIT
        if (selectedProblems.size() < problemCount) {
            final List<Map<String, Object>> currentSelected = selectedProblems; // Create final reference
            List<Map<String, Object>> defaultProblems = allProblems.stream()
                .filter(problem -> selectedDifficulties.contains(problem.get("difficulty")))
                .filter(problem -> (Integer) problem.get("estimatedTime") <= timeLimit) // RESPECT TIME LIMIT
                .filter(problem -> !currentSelected.contains(problem)) // Avoid duplicates
                .limit(problemCount - selectedProblems.size())
                .collect(Collectors.toList());
            selectedProblems.addAll(defaultProblems);
        }

        // Calculate total estimated time for selected problems
        int totalEstimatedTime = selectedProblems.stream()
            .mapToInt(problem -> (Integer) problem.get("estimatedTime"))
            .sum();

        // If total time exceeds limit, try to optimize the selection
        if (totalEstimatedTime > timeLimit && selectedProblems.size() > 1) {
            selectedProblems = optimizeProblemsForTimeLimit(selectedProblems, timeLimit);
        }

        // Calculate final total estimated time
        int finalTotalTime = selectedProblems.stream()
            .mapToInt(problem -> (Integer) problem.get("estimatedTime"))
            .sum();

        Map<String, Object> response = Map.of(
            "problems", selectedProblems,
            "config", config,
            "totalAvailable", filteredProblems.size(),
            "totalEstimatedTime", finalTotalTime,
            "timeLimit", timeLimit
        );

        return ResponseEntity.ok(response);
    }

    // Helper method to optimize problem selection for time limit
    private List<Map<String, Object>> optimizeProblemsForTimeLimit(List<Map<String, Object>> problems, int timeLimit) {
        // Sort problems by estimated time (ascending)
        problems.sort((p1, p2) -> Integer.compare(
            (Integer) p1.get("estimatedTime"),
            (Integer) p2.get("estimatedTime")
        ));

        List<Map<String, Object>> optimizedProblems = new ArrayList<>();
        int currentTime = 0;

        // Greedily select problems that fit within time limit
        for (Map<String, Object> problem : problems) {
            int problemTime = (Integer) problem.get("estimatedTime");
            if (currentTime + problemTime <= timeLimit) {
                optimizedProblems.add(problem);
                currentTime += problemTime;
            }
        }

        // If no problems fit, return the shortest one
        if (optimizedProblems.isEmpty() && !problems.isEmpty()) {
            optimizedProblems.add(problems.get(0)); // Shortest problem
        }

        return optimizedProblems;
    }

    private List<Map<String, Object>> getAllAvailableProblems() {
        return Arrays.asList(
            // Easy Problems - Arrays
            Map.of("_id", "lc-1", "name", "Two Sum", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/two-sum/", "estimatedTime", 15),
            Map.of("_id", "lc-26", "name", "Remove Duplicates from Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-array/", "estimatedTime", 20),
            Map.of("_id", "lc-121", "name", "Best Time to Buy and Sell Stock", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/", "estimatedTime", 20),

            // Easy Problems - Strings
            Map.of("_id", "lc-13", "name", "Roman to Integer", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/roman-to-integer/", "estimatedTime", 25),
            Map.of("_id", "lc-14", "name", "Longest Common Prefix", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-common-prefix/", "estimatedTime", 20),
            Map.of("_id", "lc-125", "name", "Valid Palindrome", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-palindrome/", "estimatedTime", 20),

            // Easy Problems - Linked Lists
            Map.of("_id", "lc-21", "name", "Merge Two Sorted Lists", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/merge-two-sorted-lists/", "estimatedTime", 25),
            Map.of("_id", "lc-206", "name", "Reverse Linked List", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-linked-list/", "estimatedTime", 25),
            Map.of("_id", "lc-141", "name", "Linked List Cycle", "difficulty", "Easy", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/linked-list-cycle/", "estimatedTime", 20),

            // Easy Problems - Trees
            Map.of("_id", "lc-94", "name", "Binary Tree Inorder Traversal", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-inorder-traversal/", "estimatedTime", 25),
            Map.of("_id", "lc-104", "name", "Maximum Depth of Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/maximum-depth-of-binary-tree/", "estimatedTime", 15),
            Map.of("_id", "lc-226", "name", "Invert Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/invert-binary-tree/", "estimatedTime", 15),

            // Easy Problems - Stack & Queue
            Map.of("_id", "lc-20", "name", "Valid Parentheses", "difficulty", "Easy", "topic", "Stack & Queue",
                   "url", "https://leetcode.com/problems/valid-parentheses/", "estimatedTime", 20),
            Map.of("_id", "lc-232", "name", "Implement Queue using Stacks", "difficulty", "Easy", "topic", "Stack & Queue",
                   "url", "https://leetcode.com/problems/implement-queue-using-stacks/", "estimatedTime", 25),

            // Medium Problems - Arrays
            Map.of("_id", "lc-15", "name", "3Sum", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/3sum/", "estimatedTime", 35),
            Map.of("_id", "lc-33", "name", "Search in Rotated Sorted Array", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/search-in-rotated-sorted-array/", "estimatedTime", 30),
            Map.of("_id", "lc-56", "name", "Merge Intervals", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/merge-intervals/", "estimatedTime", 30),

            // Medium Problems - Strings
            Map.of("_id", "lc-3", "name", "Longest Substring Without Repeating Characters", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-substring-without-repeating-characters/", "estimatedTime", 30),
            Map.of("_id", "lc-5", "name", "Longest Palindromic Substring", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-palindromic-substring/", "estimatedTime", 35),

            // Medium Problems - Trees
            Map.of("_id", "lc-102", "name", "Binary Tree Level Order Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-level-order-traversal/", "estimatedTime", 30),
            Map.of("_id", "lc-98", "name", "Validate Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/validate-binary-search-tree/", "estimatedTime", 30),

            // Medium Problems - Dynamic Programming
            Map.of("_id", "lc-70", "name", "Climbing Stairs", "difficulty", "Easy", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/climbing-stairs/", "estimatedTime", 20),
            Map.of("_id", "lc-198", "name", "House Robber", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber/", "estimatedTime", 25),
            Map.of("_id", "lc-62", "name", "Unique Paths", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/unique-paths/", "estimatedTime", 25),

            // Medium Problems - Graphs
            Map.of("_id", "lc-200", "name", "Number of Islands", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/number-of-islands/", "estimatedTime", 30),
            Map.of("_id", "lc-207", "name", "Course Schedule", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule/", "estimatedTime", 35),

            // Hard Problems
            Map.of("_id", "lc-4", "name", "Median of Two Sorted Arrays", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/median-of-two-sorted-arrays/", "estimatedTime", 45),
            Map.of("_id", "lc-23", "name", "Merge k Sorted Lists", "difficulty", "Hard", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/merge-k-sorted-lists/", "estimatedTime", 40),
            Map.of("_id", "lc-124", "name", "Binary Tree Maximum Path Sum", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-maximum-path-sum/", "estimatedTime", 45),

            // Additional Easy Problems - Arrays
            Map.of("_id", "lc-283", "name", "Move Zeroes", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/move-zeroes/", "estimatedTime", 15),
            Map.of("_id", "lc-448", "name", "Find All Numbers Disappeared in an Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/", "estimatedTime", 20),
            Map.of("_id", "lc-485", "name", "Max Consecutive Ones", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/max-consecutive-ones/", "estimatedTime", 15),
            Map.of("_id", "lc-561", "name", "Array Partition I", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/array-partition-i/", "estimatedTime", 15),
            Map.of("_id", "lc-566", "name", "Reshape the Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/reshape-the-matrix/", "estimatedTime", 20),
            Map.of("_id", "lc-628", "name", "Maximum Product of Three Numbers", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-product-of-three-numbers/", "estimatedTime", 20),
            Map.of("_id", "lc-643", "name", "Maximum Average Subarray I", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-average-subarray-i/", "estimatedTime", 15),
            Map.of("_id", "lc-661", "name", "Image Smoother", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/image-smoother/", "estimatedTime", 20),
            Map.of("_id", "lc-674", "name", "Longest Continuous Increasing Subsequence", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/longest-continuous-increasing-subsequence/", "estimatedTime", 15),
            Map.of("_id", "lc-697", "name", "Degree of an Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/degree-of-an-array/", "estimatedTime", 25),
            Map.of("_id", "lc-724", "name", "Find Pivot Index", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-pivot-index/", "estimatedTime", 15),
            Map.of("_id", "lc-747", "name", "Largest Number At Least Twice of Others", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/largest-number-at-least-twice-of-others/", "estimatedTime", 15),
            Map.of("_id", "lc-766", "name", "Toeplitz Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/toeplitz-matrix/", "estimatedTime", 15),
            Map.of("_id", "lc-832", "name", "Flipping an Image", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/flipping-an-image/", "estimatedTime", 15),
            Map.of("_id", "lc-867", "name", "Transpose Matrix", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/transpose-matrix/", "estimatedTime", 15),
            Map.of("_id", "lc-888", "name", "Fair Candy Swap", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/fair-candy-swap/", "estimatedTime", 20),
            Map.of("_id", "lc-896", "name", "Monotonic Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/monotonic-array/", "estimatedTime", 15),
            Map.of("_id", "lc-905", "name", "Sort Array By Parity", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-array-by-parity/", "estimatedTime", 15),
            Map.of("_id", "lc-922", "name", "Sort Array By Parity II", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sort-array-by-parity-ii/", "estimatedTime", 15),
            Map.of("_id", "lc-941", "name", "Valid Mountain Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/valid-mountain-array/", "estimatedTime", 20),
            Map.of("_id", "lc-977", "name", "Squares of a Sorted Array", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/squares-of-a-sorted-array/", "estimatedTime", 15),
            Map.of("_id", "lc-985", "name", "Sum of Even Numbers After Queries", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/sum-of-even-numbers-after-queries/", "estimatedTime", 25),
            Map.of("_id", "lc-989", "name", "Add to Array-Form of Integer", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/add-to-array-form-of-integer/", "estimatedTime", 20),
            Map.of("_id", "lc-999", "name", "Available Captures for Rook", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/available-captures-for-rook/", "estimatedTime", 20),

            // Additional Easy Problems - Strings
            Map.of("_id", "lc-415", "name", "Add Strings", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/add-strings/", "estimatedTime", 20),
            Map.of("_id", "lc-434", "name", "Number of Segments in a String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/number-of-segments-in-a-string/", "estimatedTime", 15),
            Map.of("_id", "lc-459", "name", "Repeated Substring Pattern", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/repeated-substring-pattern/", "estimatedTime", 25),
            Map.of("_id", "lc-520", "name", "Detect Capital", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/detect-capital/", "estimatedTime", 15),
            Map.of("_id", "lc-521", "name", "Longest Uncommon Subsequence I", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-uncommon-subsequence-i/", "estimatedTime", 10),
            Map.of("_id", "lc-541", "name", "Reverse String II", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-string-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-551", "name", "Student Attendance Record I", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/student-attendance-record-i/", "estimatedTime", 15),
            Map.of("_id", "lc-557", "name", "Reverse Words in a String III", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-words-in-a-string-iii/", "estimatedTime", 15),
            Map.of("_id", "lc-606", "name", "Construct String from Binary Tree", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/construct-string-from-binary-tree/", "estimatedTime", 25),
            Map.of("_id", "lc-657", "name", "Robot Return to Origin", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/robot-return-to-origin/", "estimatedTime", 15),
            Map.of("_id", "lc-680", "name", "Valid Palindrome II", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-palindrome-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-686", "name", "Repeated String Match", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/repeated-string-match/", "estimatedTime", 25),
            Map.of("_id", "lc-709", "name", "To Lower Case", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/to-lower-case/", "estimatedTime", 10),
            Map.of("_id", "lc-771", "name", "Jewels and Stones", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/jewels-and-stones/", "estimatedTime", 10),
            Map.of("_id", "lc-788", "name", "Rotated Digits", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/rotated-digits/", "estimatedTime", 20),
            Map.of("_id", "lc-796", "name", "Rotate String", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/rotate-string/", "estimatedTime", 15),
            Map.of("_id", "lc-804", "name", "Unique Morse Code Words", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/unique-morse-code-words/", "estimatedTime", 15),
            Map.of("_id", "lc-819", "name", "Most Common Word", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/most-common-word/", "estimatedTime", 20),
            Map.of("_id", "lc-824", "name", "Goat Latin", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/goat-latin/", "estimatedTime", 20),
            Map.of("_id", "lc-859", "name", "Buddy Strings", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/buddy-strings/", "estimatedTime", 20),
            Map.of("_id", "lc-884", "name", "Uncommon Words from Two Sentences", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/uncommon-words-from-two-sentences/", "estimatedTime", 15),
            Map.of("_id", "lc-893", "name", "Groups of Special-Equivalent Strings", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/groups-of-special-equivalent-strings/", "estimatedTime", 25),
            Map.of("_id", "lc-917", "name", "Reverse Only Letters", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-only-letters/", "estimatedTime", 15),
            Map.of("_id", "lc-925", "name", "Long Pressed Name", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/long-pressed-name/", "estimatedTime", 20),
            Map.of("_id", "lc-929", "name", "Unique Email Addresses", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/unique-email-addresses/", "estimatedTime", 20),
            Map.of("_id", "lc-944", "name", "Delete Columns to Make Sorted", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/delete-columns-to-make-sorted/", "estimatedTime", 15),
            Map.of("_id", "lc-953", "name", "Verifying an Alien Dictionary", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/verifying-an-alien-dictionary/", "estimatedTime", 20),
            Map.of("_id", "lc-965", "name", "Univalued Binary Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/univalued-binary-tree/", "estimatedTime", 15),
            Map.of("_id", "lc-1002", "name", "Find Common Characters", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/find-common-characters/", "estimatedTime", 20),

            // Additional Medium Problems - Linked Lists
            Map.of("_id", "lc-2", "name", "Add Two Numbers", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/add-two-numbers/", "estimatedTime", 25),
            Map.of("_id", "lc-19", "name", "Remove Nth Node From End of List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-nth-node-from-end-of-list/", "estimatedTime", 20),
            Map.of("_id", "lc-24", "name", "Swap Nodes in Pairs", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/swap-nodes-in-pairs/", "estimatedTime", 20),
            Map.of("_id", "lc-25", "name", "Reverse Nodes in k-Group", "difficulty", "Hard", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-nodes-in-k-group/", "estimatedTime", 35),
            Map.of("_id", "lc-61", "name", "Rotate List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/rotate-list/", "estimatedTime", 25),
            Map.of("_id", "lc-82", "name", "Remove Duplicates from Sorted List II", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-86", "name", "Partition List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/partition-list/", "estimatedTime", 25),
            Map.of("_id", "lc-92", "name", "Reverse Linked List II", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/reverse-linked-list-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-109", "name", "Convert Sorted List to Binary Search Tree", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/", "estimatedTime", 30),
            Map.of("_id", "lc-147", "name", "Insertion Sort List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/insertion-sort-list/", "estimatedTime", 30),

            // Additional Medium/Hard Problems - Trees
            Map.of("_id", "lc-95", "name", "Unique Binary Search Trees II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/unique-binary-search-trees-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-96", "name", "Unique Binary Search Trees", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/unique-binary-search-trees/", "estimatedTime", 25),
            Map.of("_id", "lc-99", "name", "Recover Binary Search Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/recover-binary-search-tree/", "estimatedTime", 35),
            Map.of("_id", "lc-103", "name", "Binary Tree Zigzag Level Order Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/", "estimatedTime", 30),
            Map.of("_id", "lc-107", "name", "Binary Tree Level Order Traversal II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-level-order-traversal-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-108", "name", "Convert Sorted Array to Binary Search Tree", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/", "estimatedTime", 20),
            Map.of("_id", "lc-113", "name", "Path Sum II", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum-ii/", "estimatedTime", 25),
            Map.of("_id", "lc-115", "name", "Distinct Subsequences", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/distinct-subsequences/", "estimatedTime", 40),
            Map.of("_id", "lc-129", "name", "Sum Root to Leaf Numbers", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/sum-root-to-leaf-numbers/", "estimatedTime", 25),
            Map.of("_id", "lc-156", "name", "Binary Tree Upside Down", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/binary-tree-upside-down/", "estimatedTime", 25),
            Map.of("_id", "lc-157", "name", "Read N Characters Given Read4", "difficulty", "Easy", "topic", "Design",
                   "url", "https://leetcode.com/problems/read-n-characters-given-read4/", "estimatedTime", 20),
            Map.of("_id", "lc-158", "name", "Read N Characters Given Read4 II - Call multiple times", "difficulty", "Hard", "topic", "Design",
                   "url", "https://leetcode.com/problems/read-n-characters-given-read4-ii-call-multiple-times/", "estimatedTime", 35),
            Map.of("_id", "lc-159", "name", "Longest Substring with At Most Two Distinct Characters", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/", "estimatedTime", 25),
            Map.of("_id", "lc-161", "name", "One Edit Distance", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/one-edit-distance/", "estimatedTime", 25),
            Map.of("_id", "lc-163", "name", "Missing Ranges", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/missing-ranges/", "estimatedTime", 20),
            Map.of("_id", "lc-164", "name", "Maximum Gap", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/maximum-gap/", "estimatedTime", 35),
            Map.of("_id", "lc-168", "name", "Excel Sheet Column Title", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/excel-sheet-column-title/", "estimatedTime", 20),
            Map.of("_id", "lc-170", "name", "Two Sum III - Data structure design", "difficulty", "Easy", "topic", "Design",
                   "url", "https://leetcode.com/problems/two-sum-iii-data-structure-design/", "estimatedTime", 20),
            Map.of("_id", "lc-171", "name", "Excel Sheet Column Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/excel-sheet-column-number/", "estimatedTime", 15),
            Map.of("_id", "lc-174", "name", "Dungeon Game", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/dungeon-game/", "estimatedTime", 40),
            Map.of("_id", "lc-175", "name", "Combine Two Tables", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/combine-two-tables/", "estimatedTime", 15),
            Map.of("_id", "lc-176", "name", "Second Highest Salary", "difficulty", "Medium", "topic", "Database",
                   "url", "https://leetcode.com/problems/second-highest-salary/", "estimatedTime", 20),
            Map.of("_id", "lc-177", "name", "Nth Highest Salary", "difficulty", "Medium", "topic", "Database",
                   "url", "https://leetcode.com/problems/nth-highest-salary/", "estimatedTime", 25),
            Map.of("_id", "lc-178", "name", "Rank Scores", "difficulty", "Medium", "topic", "Database",
                   "url", "https://leetcode.com/problems/rank-scores/", "estimatedTime", 25),
            Map.of("_id", "lc-180", "name", "Consecutive Numbers", "difficulty", "Medium", "topic", "Database",
                   "url", "https://leetcode.com/problems/consecutive-numbers/", "estimatedTime", 25),
            Map.of("_id", "lc-181", "name", "Employees Earning More Than Their Managers", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/employees-earning-more-than-their-managers/", "estimatedTime", 15),
            Map.of("_id", "lc-182", "name", "Duplicate Emails", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/duplicate-emails/", "estimatedTime", 15),
            Map.of("_id", "lc-183", "name", "Customers Who Never Order", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/customers-who-never-order/", "estimatedTime", 15),
            Map.of("_id", "lc-184", "name", "Department Highest Salary", "difficulty", "Medium", "topic", "Database",
                   "url", "https://leetcode.com/problems/department-highest-salary/", "estimatedTime", 25),
            Map.of("_id", "lc-185", "name", "Department Top Three Salaries", "difficulty", "Hard", "topic", "Database",
                   "url", "https://leetcode.com/problems/department-top-three-salaries/", "estimatedTime", 35),
            Map.of("_id", "lc-186", "name", "Reverse Words in a String II", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reverse-words-in-a-string-ii/", "estimatedTime", 20),
            Map.of("_id", "lc-188", "name", "Best Time to Buy and Sell Stock IV", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/", "estimatedTime", 40),
            Map.of("_id", "lc-191", "name", "Number of 1 Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/number-of-1-bits/", "estimatedTime", 15),
            Map.of("_id", "lc-192", "name", "Word Frequency", "difficulty", "Medium", "topic", "Shell",
                   "url", "https://leetcode.com/problems/word-frequency/", "estimatedTime", 20),
            Map.of("_id", "lc-193", "name", "Valid Phone Numbers", "difficulty", "Easy", "topic", "Shell",
                   "url", "https://leetcode.com/problems/valid-phone-numbers/", "estimatedTime", 15),
            Map.of("_id", "lc-194", "name", "Transpose File", "difficulty", "Medium", "topic", "Shell",
                   "url", "https://leetcode.com/problems/transpose-file/", "estimatedTime", 25),
            Map.of("_id", "lc-195", "name", "Tenth Line", "difficulty", "Easy", "topic", "Shell",
                   "url", "https://leetcode.com/problems/tenth-line/", "estimatedTime", 15),
            Map.of("_id", "lc-196", "name", "Delete Duplicate Emails", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/delete-duplicate-emails/", "estimatedTime", 20),
            Map.of("_id", "lc-197", "name", "Rising Temperature", "difficulty", "Easy", "topic", "Database",
                   "url", "https://leetcode.com/problems/rising-temperature/", "estimatedTime", 15),

            // Additional Dynamic Programming Problems
            Map.of("_id", "lc-198", "name", "House Robber", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber/", "estimatedTime", 25),
            Map.of("_id", "lc-213", "name", "House Robber II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-221", "name", "Maximal Square", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/maximal-square/", "estimatedTime", 30),
            Map.of("_id", "lc-279", "name", "Perfect Squares", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/perfect-squares/", "estimatedTime", 30),
            Map.of("_id", "lc-300", "name", "Longest Increasing Subsequence", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/longest-increasing-subsequence/", "estimatedTime", 30),
            Map.of("_id", "lc-309", "name", "Best Time to Buy and Sell Stock with Cooldown", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/", "estimatedTime", 35),
            Map.of("_id", "lc-322", "name", "Coin Change", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/coin-change/", "estimatedTime", 30),
            Map.of("_id", "lc-337", "name", "House Robber III", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/house-robber-iii/", "estimatedTime", 30),
            Map.of("_id", "lc-343", "name", "Integer Break", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/integer-break/", "estimatedTime", 25),
            Map.of("_id", "lc-354", "name", "Russian Doll Envelopes", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/russian-doll-envelopes/", "estimatedTime", 40),
            Map.of("_id", "lc-368", "name", "Largest Divisible Subset", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/largest-divisible-subset/", "estimatedTime", 30),
            Map.of("_id", "lc-375", "name", "Guess Number Higher or Lower II", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/guess-number-higher-or-lower-ii/", "estimatedTime", 35),
            Map.of("_id", "lc-377", "name", "Combination Sum IV", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/combination-sum-iv/", "estimatedTime", 25),
            Map.of("_id", "lc-392", "name", "Is Subsequence", "difficulty", "Easy", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/is-subsequence/", "estimatedTime", 15),
            Map.of("_id", "lc-416", "name", "Partition Equal Subset Sum", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/partition-equal-subset-sum/", "estimatedTime", 30),

            // Additional Graph Problems
            Map.of("_id", "lc-133", "name", "Clone Graph", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/clone-graph/", "estimatedTime", 25),
            Map.of("_id", "lc-207", "name", "Course Schedule", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule/", "estimatedTime", 30),
            Map.of("_id", "lc-210", "name", "Course Schedule II", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/course-schedule-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-323", "name", "Number of Connected Components in an Undirected Graph", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/", "estimatedTime", 25),
            Map.of("_id", "lc-329", "name", "Longest Increasing Path in a Matrix", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/longest-increasing-path-in-a-matrix/", "estimatedTime", 40),
            Map.of("_id", "lc-332", "name", "Reconstruct Itinerary", "difficulty", "Hard", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/reconstruct-itinerary/", "estimatedTime", 40),
            Map.of("_id", "lc-399", "name", "Evaluate Division", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/evaluate-division/", "estimatedTime", 35),

            // Additional Stack & Queue Problems
            Map.of("_id", "lc-71", "name", "Simplify Path", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/simplify-path/", "estimatedTime", 25),
            Map.of("_id", "lc-84", "name", "Largest Rectangle in Histogram", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/largest-rectangle-in-histogram/", "estimatedTime", 40),
            Map.of("_id", "lc-85", "name", "Maximal Rectangle", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/maximal-rectangle/", "estimatedTime", 45),
            Map.of("_id", "lc-150", "name", "Evaluate Reverse Polish Notation", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", "estimatedTime", 20),
            Map.of("_id", "lc-224", "name", "Basic Calculator", "difficulty", "Hard", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator/", "estimatedTime", 40),
            Map.of("_id", "lc-225", "name", "Implement Stack using Queues", "difficulty", "Easy", "topic", "Stack",
                   "url", "https://leetcode.com/problems/implement-stack-using-queues/", "estimatedTime", 20),
            Map.of("_id", "lc-227", "name", "Basic Calculator II", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/basic-calculator-ii/", "estimatedTime", 30),
            Map.of("_id", "lc-232", "name", "Implement Queue using Stacks", "difficulty", "Easy", "topic", "Queue",
                   "url", "https://leetcode.com/problems/implement-queue-using-stacks/", "estimatedTime", 20),
            Map.of("_id", "lc-239", "name", "Sliding Window Maximum", "difficulty", "Hard", "topic", "Queue",
                   "url", "https://leetcode.com/problems/sliding-window-maximum/", "estimatedTime", 35),
            Map.of("_id", "lc-341", "name", "Flatten Nested List Iterator", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/flatten-nested-list-iterator/", "estimatedTime", 30),
            Map.of("_id", "lc-394", "name", "Decode String", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/decode-string/", "estimatedTime", 25),

            // Additional Bit Manipulation Problems
            Map.of("_id", "lc-190", "name", "Reverse Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/reverse-bits/", "estimatedTime", 20),
            Map.of("_id", "lc-201", "name", "Bitwise AND of Numbers Range", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/bitwise-and-of-numbers-range/", "estimatedTime", 25),
            Map.of("_id", "lc-231", "name", "Power of Two", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/power-of-two/", "estimatedTime", 15),
            Map.of("_id", "lc-318", "name", "Maximum Product of Word Lengths", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/maximum-product-of-word-lengths/", "estimatedTime", 25),
            Map.of("_id", "lc-338", "name", "Counting Bits", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/counting-bits/", "estimatedTime", 20),
            Map.of("_id", "lc-342", "name", "Power of Four", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/power-of-four/", "estimatedTime", 15),
            Map.of("_id", "lc-371", "name", "Sum of Two Integers", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/sum-of-two-integers/", "estimatedTime", 25),
            Map.of("_id", "lc-389", "name", "Find the Difference", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/find-the-difference/", "estimatedTime", 15),
            Map.of("_id", "lc-393", "name", "UTF-8 Validation", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/utf-8-validation/", "estimatedTime", 30),
            Map.of("_id", "lc-401", "name", "Binary Watch", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/binary-watch/", "estimatedTime", 20),

            // Additional Math Problems
            Map.of("_id", "lc-202", "name", "Happy Number", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/happy-number/", "estimatedTime", 20),
            Map.of("_id", "lc-223", "name", "Rectangle Area", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/rectangle-area/", "estimatedTime", 25),
            Map.of("_id", "lc-233", "name", "Number of Digit One", "difficulty", "Hard", "topic", "Math",
                   "url", "https://leetcode.com/problems/number-of-digit-one/", "estimatedTime", 40),
            Map.of("_id", "lc-258", "name", "Add Digits", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/add-digits/", "estimatedTime", 10),
            Map.of("_id", "lc-292", "name", "Nim Game", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/nim-game/", "estimatedTime", 10),
            Map.of("_id", "lc-319", "name", "Bulb Switcher", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/bulb-switcher/", "estimatedTime", 20),
            Map.of("_id", "lc-326", "name", "Power of Three", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/power-of-three/", "estimatedTime", 15),
            Map.of("_id", "lc-335", "name", "Self Crossing", "difficulty", "Hard", "topic", "Math",
                   "url", "https://leetcode.com/problems/self-crossing/", "estimatedTime", 45),
            Map.of("_id", "lc-365", "name", "Water and Jug Problem", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/water-and-jug-problem/", "estimatedTime", 30),
            Map.of("_id", "lc-367", "name", "Valid Perfect Square", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/valid-perfect-square/", "estimatedTime", 15),

            // Final batch to reach 400 problems
            Map.of("_id", "lc-400", "name", "Nth Digit", "difficulty", "Medium", "topic", "Math",
                   "url", "https://leetcode.com/problems/nth-digit/", "estimatedTime", 30),
            Map.of("_id", "lc-402", "name", "Remove K Digits", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/remove-k-digits/", "estimatedTime", 30),
            Map.of("_id", "lc-403", "name", "Frog Jump", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/frog-jump/", "estimatedTime", 40),
            Map.of("_id", "lc-404", "name", "Sum of Left Leaves", "difficulty", "Easy", "topic", "Trees",
                   "url", "https://leetcode.com/problems/sum-of-left-leaves/", "estimatedTime", 20),
            Map.of("_id", "lc-405", "name", "Convert a Number to Hexadecimal", "difficulty", "Easy", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/convert-a-number-to-hexadecimal/", "estimatedTime", 20),
            Map.of("_id", "lc-406", "name", "Queue Reconstruction by Height", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/queue-reconstruction-by-height/", "estimatedTime", 30),
            Map.of("_id", "lc-407", "name", "Trapping Rain Water II", "difficulty", "Hard", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/trapping-rain-water-ii/", "estimatedTime", 45),
            Map.of("_id", "lc-408", "name", "Valid Word Abbreviation", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-word-abbreviation/", "estimatedTime", 20),
            Map.of("_id", "lc-409", "name", "Longest Palindrome", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-palindrome/", "estimatedTime", 15),
            Map.of("_id", "lc-410", "name", "Split Array Largest Sum", "difficulty", "Hard", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/split-array-largest-sum/", "estimatedTime", 40),
            Map.of("_id", "lc-412", "name", "Fizz Buzz", "difficulty", "Easy", "topic", "Math",
                   "url", "https://leetcode.com/problems/fizz-buzz/", "estimatedTime", 10),
            Map.of("_id", "lc-413", "name", "Arithmetic Slices", "difficulty", "Medium", "topic", "Dynamic Programming",
                   "url", "https://leetcode.com/problems/arithmetic-slices/", "estimatedTime", 25),
            Map.of("_id", "lc-414", "name", "Third Maximum Number", "difficulty", "Easy", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/third-maximum-number/", "estimatedTime", 20),
            Map.of("_id", "lc-417", "name", "Pacific Atlantic Water Flow", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/pacific-atlantic-water-flow/", "estimatedTime", 35),
            Map.of("_id", "lc-418", "name", "Sentence Screen Fitting", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/sentence-screen-fitting/", "estimatedTime", 30),
            Map.of("_id", "lc-419", "name", "Battleships in a Board", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/battleships-in-a-board/", "estimatedTime", 25),
            Map.of("_id", "lc-420", "name", "Strong Password Checker", "difficulty", "Hard", "topic", "Strings",
                   "url", "https://leetcode.com/problems/strong-password-checker/", "estimatedTime", 45),
            Map.of("_id", "lc-421", "name", "Maximum XOR of Two Numbers in an Array", "difficulty", "Medium", "topic", "Bit Manipulation",
                   "url", "https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/", "estimatedTime", 35),
            Map.of("_id", "lc-422", "name", "Valid Word Square", "difficulty", "Easy", "topic", "Strings",
                   "url", "https://leetcode.com/problems/valid-word-square/", "estimatedTime", 15),
            Map.of("_id", "lc-423", "name", "Reconstruct Original Digits from English", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/reconstruct-original-digits-from-english/", "estimatedTime", 30),
            Map.of("_id", "lc-424", "name", "Longest Repeating Character Replacement", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/longest-repeating-character-replacement/", "estimatedTime", 30),
            Map.of("_id", "lc-425", "name", "Word Squares", "difficulty", "Hard", "topic", "Backtracking",
                   "url", "https://leetcode.com/problems/word-squares/", "estimatedTime", 40),
            Map.of("_id", "lc-426", "name", "Convert Binary Search Tree to Sorted Doubly Linked List", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/", "estimatedTime", 30),
            Map.of("_id", "lc-427", "name", "Construct Quad Tree", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/construct-quad-tree/", "estimatedTime", 30),
            Map.of("_id", "lc-428", "name", "Serialize and Deserialize N-ary Tree", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/serialize-and-deserialize-n-ary-tree/", "estimatedTime", 40),
            Map.of("_id", "lc-429", "name", "N-ary Tree Level Order Traversal", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/n-ary-tree-level-order-traversal/", "estimatedTime", 25),
            Map.of("_id", "lc-430", "name", "Flatten a Multilevel Doubly Linked List", "difficulty", "Medium", "topic", "Linked Lists",
                   "url", "https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/", "estimatedTime", 30),
            Map.of("_id", "lc-431", "name", "Encode N-ary Tree to Binary Tree", "difficulty", "Hard", "topic", "Trees",
                   "url", "https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree/", "estimatedTime", 40),
            Map.of("_id", "lc-432", "name", "All O`one Data Structure", "difficulty", "Hard", "topic", "Design",
                   "url", "https://leetcode.com/problems/all-oone-data-structure/", "estimatedTime", 45),
            Map.of("_id", "lc-433", "name", "Minimum Genetic Mutation", "difficulty", "Medium", "topic", "Graphs",
                   "url", "https://leetcode.com/problems/minimum-genetic-mutation/", "estimatedTime", 30),
            Map.of("_id", "lc-435", "name", "Non-overlapping Intervals", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/non-overlapping-intervals/", "estimatedTime", 25),
            Map.of("_id", "lc-436", "name", "Find Right Interval", "difficulty", "Medium", "topic", "Arrays",
                   "url", "https://leetcode.com/problems/find-right-interval/", "estimatedTime", 30),
            Map.of("_id", "lc-437", "name", "Path Sum III", "difficulty", "Medium", "topic", "Trees",
                   "url", "https://leetcode.com/problems/path-sum-iii/", "estimatedTime", 25),
            Map.of("_id", "lc-438", "name", "Find All Anagrams in a String", "difficulty", "Medium", "topic", "Strings",
                   "url", "https://leetcode.com/problems/find-all-anagrams-in-a-string/", "estimatedTime", 25),
            Map.of("_id", "lc-439", "name", "Ternary Expression Parser", "difficulty", "Medium", "topic", "Stack",
                   "url", "https://leetcode.com/problems/ternary-expression-parser/", "estimatedTime", 25)
        );
    }
}
