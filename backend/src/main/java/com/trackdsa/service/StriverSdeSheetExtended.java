package com.trackdsa.service;

import com.trackdsa.model.Problem;
import com.trackdsa.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Extended Striver SDE Sheet problems - Days 12-20+
 * This service adds more advanced topics from Striver's comprehensive list
 */
@Service
public class StriverSdeSheetExtended {

    @Autowired
    private ProblemRepository problemRepository;

    public void seedExtendedProblems() {
        if (problemRepository.count() < 100) { // Only seed if we don't have many problems yet
            List<Problem> extendedProblems = Arrays.asList(
                // Day 12: Heaps
                createProblem("Max Heap", "Heaps", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://practice.geeksforgeeks.org/problems/operations-on-binary-min-heap/1",
                    7, Arrays.asList("Heap", "Binary Heap"), Arrays.asList("Heap Operations"), 20),

                createProblem("Find Median from Data Stream", "Heaps", "Hard",
                    Arrays.asList("Amazon", "Google", "Microsoft"),
                    "https://leetcode.com/problems/find-median-from-data-stream/",
                    8, Arrays.asList("Two Pointers", "Design", "Sorting", "Heap", "Data Stream"), Arrays.asList("Two Heaps"), 30),

                createProblem("K Most Frequent Elements", "Heaps", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/top-k-frequent-elements/",
                    8, Arrays.asList("Array", "Hash Table", "Divide and Conquer", "Sorting", "Heap"), Arrays.asList("Heap", "Bucket Sort"), 20),

                // Day 13: Stack and Queue
                createProblem("Implement Stack using Arrays", "Stacks", "Easy",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://practice.geeksforgeeks.org/problems/implement-stack-using-array/1",
                    6, Arrays.asList("Stack", "Array", "Design"), Arrays.asList("Stack Implementation"), 15),

                createProblem("Implement Queue using Arrays", "Queues", "Easy",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://practice.geeksforgeeks.org/problems/implement-queue-using-array/1",
                    6, Arrays.asList("Queue", "Array", "Design"), Arrays.asList("Queue Implementation"), 15),

                createProblem("Implement Stack using Queue", "Stacks", "Easy",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/implement-stack-using-queues/",
                    6, Arrays.asList("Stack", "Design", "Queue"), Arrays.asList("Stack using Queue"), 18),

                createProblem("Implement Queue using Stack", "Queues", "Easy",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/implement-queue-using-stacks/",
                    7, Arrays.asList("Stack", "Design", "Queue"), Arrays.asList("Queue using Stack"), 18),

                createProblem("Check for Balanced Parentheses", "Stacks", "Easy",
                    Arrays.asList("Amazon", "Google", "Microsoft"),
                    "https://leetcode.com/problems/valid-parentheses/",
                    9, Arrays.asList("String", "Stack"), Arrays.asList("Stack"), 10),

                createProblem("Next Greater Element", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/next-greater-element-i/",
                    7, Arrays.asList("Array", "Hash Table", "Stack", "Monotonic Stack"), Arrays.asList("Monotonic Stack"), 15),

                createProblem("Sort a Stack", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://practice.geeksforgeeks.org/problems/sort-a-stack/1",
                    6, Arrays.asList("Stack", "Recursion"), Arrays.asList("Recursion", "Stack"), 20),

                // Day 14: Stack and Queue Part II
                createProblem("Next Smaller Element", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://www.interviewbit.com/problems/nearest-smaller-element/",
                    7, Arrays.asList("Array", "Stack"), Arrays.asList("Monotonic Stack"), 18),

                createProblem("LRU Cache Implementation", "Design", "Medium",
                    Arrays.asList("Amazon", "Google", "Microsoft"),
                    "https://leetcode.com/problems/lru-cache/",
                    9, Arrays.asList("Hash Table", "Linked List", "Design", "Doubly-Linked List"), Arrays.asList("Hash Map", "Doubly Linked List"), 25),

                createProblem("LFU Cache", "Design", "Hard",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/lfu-cache/",
                    6, Arrays.asList("Hash Table", "Linked List", "Design", "Doubly-Linked List"), Arrays.asList("Hash Map", "Doubly Linked List"), 35),

                createProblem("Largest Rectangle in Histogram", "Stacks", "Hard",
                    Arrays.asList("Amazon", "Google", "Microsoft"),
                    "https://leetcode.com/problems/largest-rectangle-in-histogram/",
                    8, Arrays.asList("Array", "Stack", "Monotonic Stack"), Arrays.asList("Monotonic Stack"), 30),

                createProblem("Sliding Window Maximum", "Queues", "Hard",
                    Arrays.asList("Amazon", "Google", "Microsoft"),
                    "https://leetcode.com/problems/sliding-window-maximum/",
                    8, Arrays.asList("Array", "Queue", "Sliding Window", "Heap", "Monotonic Queue"), Arrays.asList("Deque", "Sliding Window"), 25),

                createProblem("Min Stack", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/min-stack/",
                    7, Arrays.asList("Stack", "Design"), Arrays.asList("Stack", "Design"), 18),

                createProblem("Rotten Orange", "Graphs", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/rotting-oranges/",
                    7, Arrays.asList("Array", "Breadth-First Search", "Matrix"), Arrays.asList("BFS", "Multi-source BFS"), 20),

                createProblem("Stock Span Problem", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/online-stock-span/",
                    7, Arrays.asList("Stack", "Design", "Monotonic Stack"), Arrays.asList("Monotonic Stack"), 20),

                createProblem("Find the Maximum of minimums of every window size", "Stacks", "Hard",
                    Arrays.asList("Amazon", "Google"),
                    "https://practice.geeksforgeeks.org/problems/maximum-of-minimum-for-every-window-size3453/1",
                    6, Arrays.asList("Array", "Stack"), Arrays.asList("Monotonic Stack"), 35),

                createProblem("The Celebrity Problem", "Stacks", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://practice.geeksforgeeks.org/problems/the-celebrity-problem/1",
                    7, Arrays.asList("Array", "Two Pointers", "Stack"), Arrays.asList("Stack", "Two Pointers"), 20),

                // Day 15: String
                createProblem("Reverse Words in a String", "Strings", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/reverse-words-in-a-string/",
                    7, Arrays.asList("Two Pointers", "String"), Arrays.asList("Two Pointers", "String Manipulation"), 15),

                createProblem("Longest Palindrome in a string", "Strings", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/longest-palindromic-substring/",
                    8, Arrays.asList("String", "Dynamic Programming"), Arrays.asList("Expand Around Centers", "Manacher's Algorithm"), 25),

                createProblem("Roman Number to Integer", "Strings", "Easy",
                    Arrays.asList("Amazon", "Microsoft", "Google"),
                    "https://leetcode.com/problems/roman-to-integer/",
                    7, Arrays.asList("Hash Table", "Math", "String"), Arrays.asList("Hash Map", "String Processing"), 12),

                createProblem("Integer to Roman", "Strings", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/integer-to-roman/",
                    6, Arrays.asList("Hash Table", "Math", "String"), Arrays.asList("Greedy", "String Construction"), 18),

                createProblem("Implement ATOI/STRSTR", "Strings", "Medium",
                    Arrays.asList("Amazon", "Microsoft", "Google"),
                    "https://leetcode.com/problems/string-to-integer-atoi/",
                    6, Arrays.asList("String"), Arrays.asList("String Processing", "State Machine"), 20),

                createProblem("Longest Common Prefix", "Strings", "Easy",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/longest-common-prefix/",
                    6, Arrays.asList("String", "Trie"), Arrays.asList("Vertical Scanning", "Trie"), 12),

                createProblem("Rabin Karp", "Strings", "Hard",
                    Arrays.asList("Google", "Amazon"),
                    "https://leetcode.com/problems/repeated-string-match/",
                    6, Arrays.asList("String", "String Matching"), Arrays.asList("Rabin-Karp", "Rolling Hash"), 30),

                // Day 16: String Part II
                createProblem("Z-Function", "Strings", "Hard",
                    Arrays.asList("Google", "Amazon"),
                    "https://practice.geeksforgeeks.org/problems/search-pattern-z-algorithm/1",
                    5, Arrays.asList("String", "String Matching"), Arrays.asList("Z-Algorithm"), 35),

                createProblem("KMP algo / LPS(pi) Array", "Strings", "Hard",
                    Arrays.asList("Google", "Amazon"),
                    "https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/",
                    7, Arrays.asList("Two Pointers", "String", "String Matching"), Arrays.asList("KMP", "String Matching"), 30),

                createProblem("Minimum characters needed to make a string palindrome", "Strings", "Hard",
                    Arrays.asList("Amazon", "Google"),
                    "https://practice.geeksforgeeks.org/problems/minimum-characters-to-be-added-at-front-to-make-string-palindrome/1",
                    6, Arrays.asList("String", "String Matching"), Arrays.asList("KMP", "String Processing"), 25),

                createProblem("Check for Anagrams", "Strings", "Easy",
                    Arrays.asList("Amazon", "Google"),
                    "https://leetcode.com/problems/valid-anagram/",
                    7, Arrays.asList("Hash Table", "String", "Sorting"), Arrays.asList("Hash Map", "Sorting"), 10),

                createProblem("Count and Say", "Strings", "Medium",
                    Arrays.asList("Amazon", "Facebook"),
                    "https://leetcode.com/problems/count-and-say/",
                    5, Arrays.asList("String"), Arrays.asList("String Generation", "Recursion"), 18),

                createProblem("Compare Version Numbers", "Strings", "Medium",
                    Arrays.asList("Amazon", "Microsoft"),
                    "https://leetcode.com/problems/compare-version-numbers/",
                    6, Arrays.asList("Two Pointers", "String"), Arrays.asList("String Parsing", "Two Pointers"), 15)
            );
            
            problemRepository.saveAll(extendedProblems);
        }
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
        problem.setCreatedAt(new Date());
        
        // Set complexity values based on difficulty
        switch (difficulty) {
            case "Easy":
                problem.setTimeComplexity(1);
                problem.setSpaceComplexity(1);
                break;
            case "Medium":
                problem.setTimeComplexity(2);
                problem.setSpaceComplexity(2);
                break;
            case "Hard":
                problem.setTimeComplexity(3);
                problem.setSpaceComplexity(3);
                break;
        }
        
        return problem;
    }
}
