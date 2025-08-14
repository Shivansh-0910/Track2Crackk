package com.trackdsa.service;

import com.trackdsa.model.Problem;
import com.trackdsa.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class DataSeederService implements CommandLineRunner {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private StriverSdeSheetExtended striverExtended;

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if database is empty
        if (problemRepository.count() == 0) {
            System.out.println("🌱 Seeding problem data...");
            seedProblems();
            System.out.println("✅ Problem data seeded successfully!");
        } else {
            System.out.println("📊 Problem data already exists, skipping seeding.");
        }
    }

    private void seedProblems() {
        List<Problem> problems = Arrays.asList(
            // Array Problems
            createProblem("Two Sum", "Arrays", "Easy", 
                Arrays.asList("Google", "Amazon", "Microsoft"), 
                "https://leetcode.com/problems/two-sum/",
                9, Arrays.asList("Hash Table", "Array"), Arrays.asList("Two Pointers", "Hash Map"), 15),

            createProblem("3Sum", "Arrays", "Medium", 
                Arrays.asList("Google", "Amazon", "Facebook"), 
                "https://leetcode.com/problems/3sum/",
                8, Arrays.asList("Array", "Two Pointers"), Arrays.asList("Two Pointers", "Sorting"), 25),

            createProblem("Container With Most Water", "Arrays", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/container-with-most-water/",
                7, Arrays.asList("Array", "Two Pointers"), Arrays.asList("Two Pointers", "Greedy"), 20),

            createProblem("Best Time to Buy and Sell Stock", "Arrays", "Easy", 
                Arrays.asList("Amazon", "Microsoft", "Google"), 
                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/",
                9, Arrays.asList("Array", "Dynamic Programming"), Arrays.asList("Kadane's Algorithm"), 12),

            createProblem("Maximum Subarray", "Arrays", "Medium", 
                Arrays.asList("Amazon", "Google", "Microsoft"), 
                "https://leetcode.com/problems/maximum-subarray/",
                8, Arrays.asList("Array", "Dynamic Programming"), Arrays.asList("Kadane's Algorithm", "Divide and Conquer"), 18),

            // String Problems
            createProblem("Valid Anagram", "Strings", "Easy", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/valid-anagram/",
                7, Arrays.asList("Hash Table", "String"), Arrays.asList("Sorting", "Hash Map"), 10),

            createProblem("Group Anagrams", "Strings", "Medium", 
                Arrays.asList("Amazon", "Uber"), 
                "https://leetcode.com/problems/group-anagrams/",
                8, Arrays.asList("Array", "Hash Table", "String"), Arrays.asList("Hash Map", "Sorting"), 22),

            createProblem("Longest Substring Without Repeating Characters", "Strings", "Medium", 
                Arrays.asList("Amazon", "Google", "Microsoft"), 
                "https://leetcode.com/problems/longest-substring-without-repeating-characters/",
                9, Arrays.asList("Hash Table", "String", "Sliding Window"), Arrays.asList("Sliding Window", "Two Pointers"), 20),

            createProblem("Valid Palindrome", "Strings", "Easy", 
                Arrays.asList("Microsoft", "Amazon"), 
                "https://leetcode.com/problems/valid-palindrome/",
                6, Arrays.asList("Two Pointers", "String"), Arrays.asList("Two Pointers"), 8),

            createProblem("Palindromic Substrings", "Strings", "Medium", 
                Arrays.asList("Amazon", "Microsoft"), 
                "https://leetcode.com/problems/palindromic-substrings/",
                7, Arrays.asList("String", "Dynamic Programming"), Arrays.asList("Expand Around Centers", "DP"), 25),

            // Linked List Problems
            createProblem("Reverse Linked List", "Linked Lists", "Easy", 
                Arrays.asList("Amazon", "Microsoft", "Google"), 
                "https://leetcode.com/problems/reverse-linked-list/",
                9, Arrays.asList("Linked List", "Recursion"), Arrays.asList("Iterative", "Recursive"), 15),

            createProblem("Merge Two Sorted Lists", "Linked Lists", "Easy", 
                Arrays.asList("Amazon", "Google", "Apple"), 
                "https://leetcode.com/problems/merge-two-sorted-lists/",
                8, Arrays.asList("Linked List", "Recursion"), Arrays.asList("Two Pointers", "Merge"), 12),

            createProblem("Linked List Cycle", "Linked Lists", "Easy", 
                Arrays.asList("Amazon", "Microsoft"), 
                "https://leetcode.com/problems/linked-list-cycle/",
                8, Arrays.asList("Hash Table", "Linked List", "Two Pointers"), Arrays.asList("Floyd's Cycle Detection"), 10),

            createProblem("Remove Nth Node From End of List", "Linked Lists", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/remove-nth-node-from-end-of-list/",
                7, Arrays.asList("Linked List", "Two Pointers"), Arrays.asList("Two Pointers"), 18),

            // Tree Problems
            createProblem("Maximum Depth of Binary Tree", "Trees", "Easy", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/maximum-depth-of-binary-tree/",
                8, Arrays.asList("Tree", "Depth-First Search", "Breadth-First Search"), Arrays.asList("DFS", "BFS", "Recursion"), 10),

            createProblem("Same Tree", "Trees", "Easy", 
                Arrays.asList("Amazon"), 
                "https://leetcode.com/problems/same-tree/",
                6, Arrays.asList("Tree", "Depth-First Search"), Arrays.asList("DFS", "Recursion"), 8),

            createProblem("Invert Binary Tree", "Trees", "Easy", 
                Arrays.asList("Google", "Amazon"), 
                "https://leetcode.com/problems/invert-binary-tree/",
                7, Arrays.asList("Tree", "Depth-First Search", "Breadth-First Search"), Arrays.asList("DFS", "BFS", "Recursion"), 12),

            createProblem("Binary Tree Level Order Traversal", "Trees", "Medium", 
                Arrays.asList("Amazon", "Microsoft", "Google"), 
                "https://leetcode.com/problems/binary-tree-level-order-traversal/",
                8, Arrays.asList("Tree", "Breadth-First Search"), Arrays.asList("BFS", "Queue"), 20),

            createProblem("Validate Binary Search Tree", "Trees", "Medium", 
                Arrays.asList("Amazon", "Microsoft", "Google"), 
                "https://leetcode.com/problems/validate-binary-search-tree/",
                8, Arrays.asList("Tree", "Depth-First Search", "Binary Search Tree"), Arrays.asList("DFS", "Inorder Traversal"), 22),

            // Dynamic Programming Problems
            createProblem("Climbing Stairs", "Dynamic Programming", "Easy", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/climbing-stairs/",
                8, Arrays.asList("Math", "Dynamic Programming", "Memoization"), Arrays.asList("Fibonacci", "DP"), 10),

            createProblem("House Robber", "Dynamic Programming", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/house-robber/",
                7, Arrays.asList("Array", "Dynamic Programming"), Arrays.asList("DP", "State Machine"), 15),

            createProblem("Coin Change", "Dynamic Programming", "Medium", 
                Arrays.asList("Amazon", "Google", "Uber"), 
                "https://leetcode.com/problems/coin-change/",
                8, Arrays.asList("Array", "Dynamic Programming", "Breadth-First Search"), Arrays.asList("DP", "BFS"), 25),

            createProblem("Longest Increasing Subsequence", "Dynamic Programming", "Medium", 
                Arrays.asList("Amazon", "Microsoft"), 
                "https://leetcode.com/problems/longest-increasing-subsequence/",
                7, Arrays.asList("Array", "Binary Search", "Dynamic Programming"), Arrays.asList("DP", "Binary Search"), 30),

            // Graph Problems
            createProblem("Number of Islands", "Graphs", "Medium", 
                Arrays.asList("Amazon", "Google", "Microsoft"), 
                "https://leetcode.com/problems/number-of-islands/",
                9, Arrays.asList("Array", "Depth-First Search", "Breadth-First Search", "Union Find"), Arrays.asList("DFS", "BFS", "Union Find"), 20),

            createProblem("Clone Graph", "Graphs", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/clone-graph/",
                7, Arrays.asList("Hash Table", "Depth-First Search", "Breadth-First Search", "Graph"), Arrays.asList("DFS", "BFS", "Hash Map"), 25),

            createProblem("Course Schedule", "Graphs", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/course-schedule/",
                8, Arrays.asList("Depth-First Search", "Breadth-First Search", "Graph", "Topological Sort"), Arrays.asList("Topological Sort", "DFS", "BFS"), 22),

            // Hash Table Problems
            createProblem("Contains Duplicate", "Hash Tables", "Easy", 
                Arrays.asList("Amazon"), 
                "https://leetcode.com/problems/contains-duplicate/",
                6, Arrays.asList("Array", "Hash Table", "Sorting"), Arrays.asList("Hash Set", "Sorting"), 8),

            createProblem("Valid Sudoku", "Hash Tables", "Medium", 
                Arrays.asList("Amazon", "Apple"), 
                "https://leetcode.com/problems/valid-sudoku/",
                6, Arrays.asList("Array", "Hash Table"), Arrays.asList("Hash Set", "Matrix"), 20),

            // Stack Problems
            createProblem("Valid Parentheses", "Stacks", "Easy", 
                Arrays.asList("Amazon", "Google", "Microsoft"), 
                "https://leetcode.com/problems/valid-parentheses/",
                9, Arrays.asList("String", "Stack"), Arrays.asList("Stack"), 10),

            createProblem("Min Stack", "Stacks", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/min-stack/",
                7, Arrays.asList("Stack", "Design"), Arrays.asList("Stack", "Design"), 18),

            // Heap Problems
            createProblem("Kth Largest Element in an Array", "Heaps", "Medium", 
                Arrays.asList("Amazon", "Facebook"), 
                "https://leetcode.com/problems/kth-largest-element-in-an-array/",
                8, Arrays.asList("Array", "Divide and Conquer", "Sorting", "Heap"), Arrays.asList("Quick Select", "Heap"), 15),

            createProblem("Top K Frequent Elements", "Heaps", "Medium", 
                Arrays.asList("Amazon", "Google"), 
                "https://leetcode.com/problems/top-k-frequent-elements/",
                8, Arrays.asList("Array", "Hash Table", "Divide and Conquer", "Sorting", "Heap"), Arrays.asList("Heap", "Bucket Sort"), 20),

            // More Array Problems
            createProblem("Product of Array Except Self", "Arrays", "Medium",
                Arrays.asList("Amazon", "Microsoft", "Google"),
                "https://leetcode.com/problems/product-of-array-except-self/",
                8, Arrays.asList("Array", "Prefix Sum"), Arrays.asList("Prefix Sum", "Two Pass"), 18),

            createProblem("Find Minimum in Rotated Sorted Array", "Arrays", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/",
                7, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search", "Divide and Conquer"), 15),

            createProblem("Search in Rotated Sorted Array", "Arrays", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/search-in-rotated-sorted-array/",
                8, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search"), 20),

            // More String Problems
            createProblem("Longest Palindromic Substring", "Strings", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/longest-palindromic-substring/",
                8, Arrays.asList("String", "Dynamic Programming"), Arrays.asList("Expand Around Centers", "Manacher's Algorithm"), 25),

            createProblem("String to Integer (atoi)", "Strings", "Medium",
                Arrays.asList("Amazon", "Microsoft", "Google"),
                "https://leetcode.com/problems/string-to-integer-atoi/",
                6, Arrays.asList("String", "Math"), Arrays.asList("String Processing"), 20),

            createProblem("Implement strStr()", "Strings", "Easy",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/implement-strstr/",
                7, Arrays.asList("Two Pointers", "String"), Arrays.asList("KMP", "Two Pointers"), 12),

            // More Tree Problems
            createProblem("Lowest Common Ancestor of a Binary Tree", "Trees", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/",
                8, Arrays.asList("Tree", "Depth-First Search"), Arrays.asList("DFS", "Recursion"), 20),

            createProblem("Binary Tree Right Side View", "Trees", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/binary-tree-right-side-view/",
                7, Arrays.asList("Tree", "Depth-First Search", "Breadth-First Search"), Arrays.asList("BFS", "DFS"), 15),

            createProblem("Serialize and Deserialize Binary Tree", "Trees", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/serialize-and-deserialize-binary-tree/",
                8, Arrays.asList("String", "Tree", "Depth-First Search", "Breadth-First Search"), Arrays.asList("DFS", "BFS", "Preorder"), 30),

            // More Dynamic Programming Problems
            createProblem("Unique Paths", "Dynamic Programming", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/unique-paths/",
                7, Arrays.asList("Math", "Dynamic Programming", "Combinatorics"), Arrays.asList("DP", "Math"), 15),

            createProblem("Edit Distance", "Dynamic Programming", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/edit-distance/",
                8, Arrays.asList("String", "Dynamic Programming"), Arrays.asList("DP", "String Matching"), 35),

            createProblem("Word Break", "Dynamic Programming", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/word-break/",
                8, Arrays.asList("Array", "Hash Table", "String", "Dynamic Programming"), Arrays.asList("DP", "Trie"), 20),

            // More Graph Problems
            createProblem("Word Ladder", "Graphs", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/word-ladder/",
                7, Arrays.asList("Hash Table", "String", "Breadth-First Search"), Arrays.asList("BFS", "Graph Traversal"), 30),

            createProblem("Pacific Atlantic Water Flow", "Graphs", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/pacific-atlantic-water-flow/",
                6, Arrays.asList("Array", "Depth-First Search", "Breadth-First Search", "Matrix"), Arrays.asList("DFS", "BFS"), 25),

            // More Advanced Problems
            createProblem("Trapping Rain Water", "Arrays", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/trapping-rain-water/",
                9, Arrays.asList("Array", "Two Pointers", "Dynamic Programming", "Stack"), Arrays.asList("Two Pointers", "Stack"), 25),

            createProblem("Median of Two Sorted Arrays", "Binary Search", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/median-of-two-sorted-arrays/",
                9, Arrays.asList("Array", "Binary Search", "Divide and Conquer"), Arrays.asList("Binary Search"), 30),

            createProblem("Regular Expression Matching", "Dynamic Programming", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/regular-expression-matching/",
                8, Arrays.asList("String", "Dynamic Programming", "Recursion"), Arrays.asList("DP", "Recursion"), 35),

            // System Design Related
            createProblem("LRU Cache", "Design", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/lru-cache/",
                9, Arrays.asList("Hash Table", "Linked List", "Design", "Doubly-Linked List"), Arrays.asList("Hash Map", "Doubly Linked List"), 25),

            createProblem("Design Add and Search Words Data Structure", "Design", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/design-add-and-search-words-data-structure/",
                7, Arrays.asList("String", "Depth-First Search", "Design", "Trie"), Arrays.asList("Trie", "DFS"), 20),

            // =============== STRIVER'S SDE SHEET - DAY 1-3 ===============
            
            // Day 1: Arrays Part I
            createProblem("Set Matrix Zeroes", "Arrays", "Medium",
                Arrays.asList("Amazon", "Microsoft", "Google"),
                "https://leetcode.com/problems/set-matrix-zeroes/",
                8, Arrays.asList("Array", "Hash Table", "Matrix"), Arrays.asList("Matrix Manipulation", "In-place"), 20),

            createProblem("Pascal's Triangle", "Arrays", "Easy",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/pascals-triangle/",
                6, Arrays.asList("Array", "Dynamic Programming"), Arrays.asList("DP", "Math"), 15),

            createProblem("Next Permutation", "Arrays", "Medium",
                Arrays.asList("Google", "Amazon"),
                "https://leetcode.com/problems/next-permutation/",
                8, Arrays.asList("Array", "Two Pointers"), Arrays.asList("Two Pointers", "Math"), 25),

            createProblem("Sort Colors", "Arrays", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/sort-colors/",
                7, Arrays.asList("Array", "Two Pointers", "Sorting"), Arrays.asList("Dutch National Flag", "Three Pointers"), 12),

            // Day 2: Arrays Part II
            createProblem("Find Duplicate Number", "Arrays", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/find-the-duplicate-number/",
                8, Arrays.asList("Array", "Two Pointers", "Binary Search"), Arrays.asList("Floyd's Cycle Detection", "Binary Search"), 20),

            createProblem("Repeat and Missing Number", "Arrays", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://www.interviewbit.com/problems/repeat-and-missing-number-array/",
                7, Arrays.asList("Array", "Math"), Arrays.asList("Math", "XOR"), 18),

            createProblem("Inversion Count", "Arrays", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/inversion-of-array-1587115620/1",
                6, Arrays.asList("Array", "Binary Indexed Tree", "Segment Tree", "Merge Sort"), Arrays.asList("Merge Sort", "Divide and Conquer"), 30),

            // Day 3: Arrays Part III
            createProblem("Search in 2D Matrix", "Arrays", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/search-a-2d-matrix/",
                8, Arrays.asList("Array", "Binary Search", "Matrix"), Arrays.asList("Binary Search", "Matrix"), 15),

            createProblem("Majority Element (>n/2 times)", "Arrays", "Easy",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/majority-element/",
                8, Arrays.asList("Array", "Hash Table", "Divide and Conquer", "Sorting", "Counting"), Arrays.asList("Boyer-Moore Voting", "Hash Map"), 12),

            createProblem("Majority Element II (>n/3 times)", "Arrays", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/majority-element-ii/",
                7, Arrays.asList("Array", "Hash Table", "Counting"), Arrays.asList("Boyer-Moore Voting"), 20),

            createProblem("Reverse Pairs", "Arrays", "Hard",
                Arrays.asList("Google", "Amazon"),
                "https://leetcode.com/problems/reverse-pairs/",
                6, Arrays.asList("Array", "Binary Search", "Divide and Conquer", "Binary Indexed Tree", "Segment Tree", "Merge Sort"), Arrays.asList("Merge Sort", "Divide and Conquer"), 35)
        );

        problemRepository.saveAll(problems);
        
        // Add more Striver SDE Sheet problems
        seedStriverSdeSheet();
        
        // Add extended Striver SDE Sheet problems
        striverExtended.seedExtendedProblems();
    }
    
    private void seedStriverSdeSheet() {
        List<Problem> striverProblems = Arrays.asList(
            // Day 4: Hashing
            createProblem("Longest Consecutive Sequence", "Hash Tables", "Medium",
                Arrays.asList("Amazon", "Google", "Facebook"),
                "https://leetcode.com/problems/longest-consecutive-sequence/",
                8, Arrays.asList("Array", "Hash Table", "Union Find"), Arrays.asList("Hash Set", "Union Find"), 20),

            createProblem("Largest Subarray with 0 Sum", "Hash Tables", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://practice.geeksforgeeks.org/problems/largest-subarray-with-0-sum/1",
                7, Arrays.asList("Array", "Hash Table"), Arrays.asList("Prefix Sum", "Hash Map"), 18),

            createProblem("Count Subarrays with Given XOR", "Hash Tables", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://www.interviewbit.com/problems/subarray-with-given-xor/",
                6, Arrays.asList("Array", "Hash Table", "Bit Manipulation"), Arrays.asList("Prefix XOR", "Hash Map"), 22),

            // Day 5: Linked List
            createProblem("Find Middle of Linked List", "Linked Lists", "Easy",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/middle-of-the-linked-list/",
                7, Arrays.asList("Linked List", "Two Pointers"), Arrays.asList("Slow Fast Pointers"), 10),

            createProblem("Delete Node in Linked List", "Linked Lists", "Medium",
                Arrays.asList("Amazon", "Apple"),
                "https://leetcode.com/problems/delete-node-in-a-linked-list/",
                6, Arrays.asList("Linked List"), Arrays.asList("Node Manipulation"), 8),

            // Day 6: Linked List Part II
            createProblem("Intersection of Two Linked Lists", "Linked Lists", "Easy",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/intersection-of-two-linked-lists/",
                7, Arrays.asList("Hash Table", "Linked List", "Two Pointers"), Arrays.asList("Two Pointers", "Hash Set"), 15),

            createProblem("Reverse Nodes in k-Group", "Linked Lists", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/reverse-nodes-in-k-group/",
                8, Arrays.asList("Linked List", "Recursion"), Arrays.asList("Recursion", "Iterative"), 30),

            createProblem("Palindrome Linked List", "Linked Lists", "Easy",
                Arrays.asList("Amazon", "Facebook"),
                "https://leetcode.com/problems/palindrome-linked-list/",
                7, Arrays.asList("Linked List", "Two Pointers", "Stack", "Recursion"), Arrays.asList("Two Pointers", "Stack"), 18),

            createProblem("Starting Point of Loop in Linked List", "Linked Lists", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/linked-list-cycle-ii/",
                8, Arrays.asList("Hash Table", "Linked List", "Two Pointers"), Arrays.asList("Floyd's Cycle Detection"), 20),

            createProblem("Flattening a Linked List", "Linked Lists", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/flattening-a-linked-list/1",
                6, Arrays.asList("Linked List", "Heap"), Arrays.asList("Merge", "Heap"), 25),

            createProblem("Rotate List", "Linked Lists", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/rotate-list/",
                7, Arrays.asList("Linked List", "Two Pointers"), Arrays.asList("Two Pointers", "Circular"), 18),

            // Day 7: 2-Pointer
            createProblem("Remove Duplicates from Sorted Array", "Arrays", "Easy",
                Arrays.asList("Amazon", "Microsoft"),
                "https://leetcode.com/problems/remove-duplicates-from-sorted-array/",
                6, Arrays.asList("Array", "Two Pointers"), Arrays.asList("Two Pointers"), 10),

            createProblem("Max Consecutive Ones", "Arrays", "Easy",
                Arrays.asList("Google", "Amazon"),
                "https://leetcode.com/problems/max-consecutive-ones/",
                5, Arrays.asList("Array"), Arrays.asList("One Pass"), 8),

            createProblem("Trapping Rain Water", "Arrays", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/trapping-rain-water/",
                9, Arrays.asList("Array", "Two Pointers", "Dynamic Programming", "Stack"), Arrays.asList("Two Pointers", "Stack"), 25),

            // Day 8: Greedy Algorithm
            createProblem("N meetings in one room", "Greedy", "Easy",
                Arrays.asList("Amazon", "Microsoft"),
                "https://practice.geeksforgeeks.org/problems/n-meetings-in-one-room-1587115620/1",
                7, Arrays.asList("Greedy", "Sorting"), Arrays.asList("Activity Selection"), 15),

            createProblem("Minimum Platforms", "Greedy", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/minimum-platforms-1587115620/1",
                8, Arrays.asList("Array", "Greedy", "Sorting"), Arrays.asList("Greedy", "Two Pointers"), 20),

            createProblem("Job Sequencing Problem", "Greedy", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://practice.geeksforgeeks.org/problems/job-sequencing-problem-1587115620/1",
                7, Arrays.asList("Array", "Greedy", "Sorting"), Arrays.asList("Greedy", "Union Find"), 25),

            createProblem("Fractional Knapsack", "Greedy", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/fractional-knapsack-1587115620/1",
                6, Arrays.asList("Array", "Greedy", "Sorting"), Arrays.asList("Greedy"), 18),

            // Day 9: Recursion
            createProblem("Subset Sums", "Recursion", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/subset-sums2234/1",
                7, Arrays.asList("Array", "Bit Manipulation", "Backtracking", "Recursion"), Arrays.asList("Recursion", "Backtracking"), 20),

            createProblem("Subsets II", "Backtracking", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/subsets-ii/",
                7, Arrays.asList("Array", "Backtracking", "Bit Manipulation"), Arrays.asList("Backtracking"), 22),

            createProblem("Combination Sum II", "Backtracking", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/combination-sum-ii/",
                7, Arrays.asList("Array", "Backtracking"), Arrays.asList("Backtracking"), 25),

            createProblem("Palindrome Partitioning", "Backtracking", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/palindrome-partitioning/",
                8, Arrays.asList("String", "Dynamic Programming", "Backtracking"), Arrays.asList("Backtracking", "DP"), 30),

            createProblem("K-th Permutation Sequence", "Math", "Hard",
                Arrays.asList("Google", "Amazon"),
                "https://leetcode.com/problems/permutation-sequence/",
                6, Arrays.asList("Math", "Recursion"), Arrays.asList("Math", "Factorial"), 25),

            // Day 10: Recursion and Backtracking
            createProblem("N-Queens", "Backtracking", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://leetcode.com/problems/n-queens/",
                7, Arrays.asList("Array", "Backtracking"), Arrays.asList("Backtracking"), 35),

            createProblem("Sudoku Solver", "Backtracking", "Hard",
                Arrays.asList("Amazon", "Google", "Uber"),
                "https://leetcode.com/problems/sudoku-solver/",
                6, Arrays.asList("Array", "Hash Table", "Backtracking", "Matrix"), Arrays.asList("Backtracking"), 40),

            createProblem("M-Coloring Problem", "Backtracking", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://practice.geeksforgeeks.org/problems/m-coloring-problem-1587115620/1",
                6, Arrays.asList("Backtracking", "Graph"), Arrays.asList("Backtracking", "Graph Coloring"), 30),

            createProblem("Rat in a Maze", "Backtracking", "Medium",
                Arrays.asList("Amazon", "Microsoft"),
                "https://practice.geeksforgeeks.org/problems/rat-in-a-maze-problem/1",
                7, Arrays.asList("Array", "Backtracking", "Matrix"), Arrays.asList("Backtracking", "DFS"), 25),

            // Day 11: Binary Search
            createProblem("Find First and Last Position", "Binary Search", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/",
                8, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search"), 18),

            createProblem("Search in Rotated Sorted Array", "Binary Search", "Medium",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/search-in-rotated-sorted-array/",
                9, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search"), 20),

            createProblem("Median of Two Sorted Arrays", "Binary Search", "Hard",
                Arrays.asList("Amazon", "Google", "Microsoft"),
                "https://leetcode.com/problems/median-of-two-sorted-arrays/",
                9, Arrays.asList("Array", "Binary Search", "Divide and Conquer"), Arrays.asList("Binary Search"), 30),

            createProblem("Kth Element of Two Sorted Arrays", "Binary Search", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://practice.geeksforgeeks.org/problems/k-th-element-of-two-sorted-array1317/1",
                7, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search"), 25),

            createProblem("Allocate Books", "Binary Search", "Medium",
                Arrays.asList("Amazon", "Google"),
                "https://www.interviewbit.com/problems/allocate-books/",
                7, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search on Answer"), 25),

            createProblem("Aggressive Cows", "Binary Search", "Hard",
                Arrays.asList("Amazon", "Google"),
                "https://www.spoj.com/problems/AGGRCOW/",
                6, Arrays.asList("Array", "Binary Search"), Arrays.asList("Binary Search on Answer"), 30)
        );
        
        problemRepository.saveAll(striverProblems);
    }

    private Problem createProblem(String name, String topic, String difficulty, 
                                List<String> companies, String url, int frequencyScore,
                                List<String> tags, List<String> patterns, double avgTime) {
        Problem problem = new Problem();
        problem.setName(name);
        problem.setTopic(topic);
        problem.setDifficulty(difficulty);
        problem.setCompanies(companies);
        problem.setUrl(url);
        problem.setFrequencyScore(frequencyScore);
        problem.setTags(tags);
        problem.setPatterns(patterns);
        problem.setAverageTime(avgTime);
        problem.setActive(true);
        
        // Set complexity values (simplified)
        switch (difficulty) {
            case "Easy":
                problem.setTimeComplexity(1); // O(n)
                problem.setSpaceComplexity(1); // O(1)
                break;
            case "Medium":
                problem.setTimeComplexity(2); // O(n log n)
                problem.setSpaceComplexity(2); // O(n)
                break;
            case "Hard":
                problem.setTimeComplexity(3); // O(n^2)
                problem.setSpaceComplexity(3); // O(n^2)
                break;
        }
        
        return problem;
    }
}
