import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Progress } from './ui/progress';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';
import { Calendar, Clock, Target, Trophy, BookOpen, CheckCircle, ArrowRight, Play, Pause, RotateCcw, Map, AlertCircle } from 'lucide-react';
import { roadmapAPI } from '../api/roadmap';
import { useToast } from '../hooks/useToast';

const RoadmapGenerator = () => {
  const [timelines, setTimelines] = useState([]);
  const [selectedTimeline, setSelectedTimeline] = useState(null);
  const [roadmap, setRoadmap] = useState(null);
  const [progress, setProgress] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeWeek, setActiveWeek] = useState(1);
  const [completedWeeks, setCompletedWeeks] = useState(new Set());
  const { toast } = useToast();

  // Mock data for fallback
  const mockTimelines = [
    {
      key: '30_days',
      days: 30,
      description: '30 Days - Quick Prep',
      weeks: 4,
      estimatedHoursPerWeek: 15,
      difficulty: 'Intensive'
    },
    {
      key: '60_days',
      days: 60,
      description: '60 Days - Standard Prep',
      weeks: 8,
      estimatedHoursPerWeek: 12,
      difficulty: 'Moderate'
    },
    {
      key: '90_days',
      days: 90,
      description: '90 Days - Comprehensive Prep',
      weeks: 13,
      estimatedHoursPerWeek: 10,
      difficulty: 'Balanced'
    },
    {
      key: '180_days',
      days: 180,
      description: '180 Days - Deep Dive',
      weeks: 26,
      estimatedHoursPerWeek: 8,
      difficulty: 'Relaxed'
    }
  ];

  const mockProgress = {
    currentWeek: 2,
    totalWeeks: 8,
    problemsCompleted: 25,
    targetProblems: 120,
    completionPercentage: 20.8,
    onTrack: true,
    nextMilestone: 'Complete Week 3: Binary Search & Sorting'
  };

  const mockRecommendations = [
    {
      timeline: '60_days',
      reason: 'Based on your current progress, 60 days would be optimal',
      confidence: 85,
      estimatedSuccessRate: 78
    },
    {
      timeline: '90_days',
      reason: 'For a more comfortable pace with better retention',
      confidence: 92,
      estimatedSuccessRate: 85
    }
  ];

  const generateMockRoadmap = (timelineKey) => {
    const timeline = mockTimelines.find(t => t.key === timelineKey);
    if (!timeline) return null;

    const weeks = [];
    const weekCount = timeline.weeks;

    // Define comprehensive problem sets for different topics
    const problemSets = {
             arrays: [
         { name: 'Two Sum', difficulty: 'Easy', leetcodeId: 1 },
         { name: 'Best Time to Buy and Sell Stock', difficulty: 'Easy', leetcodeId: 121 },
         { name: 'Contains Duplicate', difficulty: 'Easy', leetcodeId: 217 },
         { name: 'Product of Array Except Self', difficulty: 'Medium', leetcodeId: 238 },
         { name: 'Maximum Subarray', difficulty: 'Medium', leetcodeId: 53 },
         { name: '3Sum', difficulty: 'Medium', leetcodeId: 15 },
         { name: 'Container With Most Water', difficulty: 'Medium', leetcodeId: 11 },
         { name: 'Trapping Rain Water', difficulty: 'Hard', leetcodeId: 42 },
         { name: 'Move Zeroes', difficulty: 'Easy', leetcodeId: 283 },
         { name: 'Maximum Product Subarray', difficulty: 'Medium', leetcodeId: 152 },
         { name: 'Find the Duplicate Number', difficulty: 'Medium', leetcodeId: 287 },
         { name: 'Missing Number', difficulty: 'Easy', leetcodeId: 268 },
         { name: 'First Missing Positive', difficulty: 'Hard', leetcodeId: 41 },
         { name: 'Subarray Sum Equals K', difficulty: 'Medium', leetcodeId: 560 },
         { name: 'Next Permutation', difficulty: 'Medium', leetcodeId: 31 },
         { name: 'Spiral Matrix', difficulty: 'Medium', leetcodeId: 54 },
         { name: 'Rotate Image', difficulty: 'Medium', leetcodeId: 48 },
         { name: 'Set Matrix Zeroes', difficulty: 'Medium', leetcodeId: 73 },
         { name: 'Game of Life', difficulty: 'Medium', leetcodeId: 289 }
       ],
             strings: [
         { name: 'Valid Parentheses', difficulty: 'Easy', leetcodeId: 20 },
         { name: 'Longest Common Prefix', difficulty: 'Easy', leetcodeId: 14 },
         { name: 'Valid Anagram', difficulty: 'Easy', leetcodeId: 242 },
         { name: 'Longest Substring Without Repeating Characters', difficulty: 'Medium', leetcodeId: 3 },
         { name: 'Longest Palindromic Substring', difficulty: 'Medium', leetcodeId: 5 },
         { name: 'Group Anagrams', difficulty: 'Medium', leetcodeId: 49 },
         { name: 'Minimum Window Substring', difficulty: 'Hard', leetcodeId: 76 },
         { name: 'Regular Expression Matching', difficulty: 'Hard', leetcodeId: 10 },
         { name: 'Implement strStr()', difficulty: 'Easy', leetcodeId: 28 },
         { name: 'String to Integer (atoi)', difficulty: 'Medium', leetcodeId: 8 },
         { name: 'Integer to Roman', difficulty: 'Medium', leetcodeId: 12 },
         { name: 'Roman to Integer', difficulty: 'Easy', leetcodeId: 13 },
         { name: 'Longest Valid Parentheses', difficulty: 'Hard', leetcodeId: 32 },
         { name: 'Simplify Path', difficulty: 'Medium', leetcodeId: 71 },
         { name: 'Basic Calculator', difficulty: 'Hard', leetcodeId: 224 },
         { name: 'Decode String', difficulty: 'Medium', leetcodeId: 394 },
         { name: 'Valid Parentheses', difficulty: 'Easy', leetcodeId: 20 },
         { name: 'Generate Parentheses', difficulty: 'Medium', leetcodeId: 22 },
         { name: 'Word Break', difficulty: 'Medium', leetcodeId: 139 }
       ],
             linkedLists: [
         { name: 'Reverse Linked List', difficulty: 'Easy', leetcodeId: 206 },
         { name: 'Linked List Cycle', difficulty: 'Easy', leetcodeId: 141 },
         { name: 'Add Two Numbers', difficulty: 'Medium', leetcodeId: 2 },
         { name: 'Remove Nth Node From End of List', difficulty: 'Medium', leetcodeId: 19 },
         { name: 'Merge Two Sorted Lists', difficulty: 'Easy', leetcodeId: 21 },
         { name: 'Merge k Sorted Lists', difficulty: 'Hard', leetcodeId: 23 },
         { name: 'Copy List with Random Pointer', difficulty: 'Medium', leetcodeId: 138 },
         { name: 'Reorder List', difficulty: 'Medium', leetcodeId: 143 },
         { name: 'Palindrome Linked List', difficulty: 'Easy', leetcodeId: 234 },
         { name: 'Intersection of Two Linked Lists', difficulty: 'Easy', leetcodeId: 160 },
         { name: 'Sort List', difficulty: 'Medium', leetcodeId: 148 },
         { name: 'Flatten Binary Tree to Linked List', difficulty: 'Medium', leetcodeId: 114 },
         { name: 'Convert Sorted List to Binary Search Tree', difficulty: 'Medium', leetcodeId: 109 },
         { name: 'Rotate List', difficulty: 'Medium', leetcodeId: 61 },
         { name: 'Partition List', difficulty: 'Medium', leetcodeId: 86 },
         { name: 'Reverse Nodes in k-Group', difficulty: 'Hard', leetcodeId: 25 },
         { name: 'Swap Nodes in Pairs', difficulty: 'Medium', leetcodeId: 24 },
         { name: 'Odd Even Linked List', difficulty: 'Medium', leetcodeId: 328 },
         { name: 'Remove Duplicates from Sorted List', difficulty: 'Easy', leetcodeId: 83 }
       ],
             trees: [
         { name: 'Maximum Depth of Binary Tree', difficulty: 'Easy', leetcodeId: 104 },
         { name: 'Validate Binary Search Tree', difficulty: 'Medium', leetcodeId: 98 },
         { name: 'Binary Tree Level Order Traversal', difficulty: 'Medium', leetcodeId: 102 },
         { name: 'Invert Binary Tree', difficulty: 'Easy', leetcodeId: 226 },
         { name: 'Kth Smallest Element in a BST', difficulty: 'Medium', leetcodeId: 230 },
         { name: 'Lowest Common Ancestor of a Binary Search Tree', difficulty: 'Easy', leetcodeId: 235 },
         { name: 'Serialize and Deserialize Binary Tree', difficulty: 'Hard', leetcodeId: 297 },
         { name: 'Binary Tree Maximum Path Sum', difficulty: 'Hard', leetcodeId: 124 },
         { name: 'Construct Binary Tree from Preorder and Inorder Traversal', difficulty: 'Medium', leetcodeId: 105 },
         { name: 'Construct Binary Tree from Inorder and Postorder Traversal', difficulty: 'Medium', leetcodeId: 106 },
         { name: 'Binary Tree Zigzag Level Order Traversal', difficulty: 'Medium', leetcodeId: 103 },
         { name: 'Symmetric Tree', difficulty: 'Easy', leetcodeId: 101 },
         { name: 'Same Tree', difficulty: 'Easy', leetcodeId: 100 },
         { name: 'Path Sum', difficulty: 'Easy', leetcodeId: 112 },
         { name: 'Path Sum II', difficulty: 'Medium', leetcodeId: 113 },
         { name: 'Flatten Binary Tree to Linked List', difficulty: 'Medium', leetcodeId: 114 },
         { name: 'Populating Next Right Pointers in Each Node', difficulty: 'Medium', leetcodeId: 116 },
         { name: 'Sum Root to Leaf Numbers', difficulty: 'Medium', leetcodeId: 129 },
         { name: 'Binary Tree Right Side View', difficulty: 'Medium', leetcodeId: 199 }
       ],
       graphs: [
         { name: 'Number of Islands', difficulty: 'Medium', leetcodeId: 200 },
         { name: 'Clone Graph', difficulty: 'Medium', leetcodeId: 133 },
         { name: 'Course Schedule', difficulty: 'Medium', leetcodeId: 207 },
         { name: 'Pacific Atlantic Water Flow', difficulty: 'Medium', leetcodeId: 417 },
         { name: 'Redundant Connection', difficulty: 'Medium', leetcodeId: 684 },
         { name: 'Graph Valid Tree', difficulty: 'Medium', leetcodeId: 261 },
         { name: 'Alien Dictionary', difficulty: 'Hard', leetcodeId: 269 },
         { name: 'Word Ladder', difficulty: 'Hard', leetcodeId: 127 },
         { name: 'Course Schedule II', difficulty: 'Medium', leetcodeId: 210 },
         { name: 'Reconstruct Itinerary', difficulty: 'Hard', leetcodeId: 332 },
         { name: 'Min Cost to Connect All Points', difficulty: 'Medium', leetcodeId: 1584 },
         { name: 'Network Delay Time', difficulty: 'Medium', leetcodeId: 743 },
         { name: 'Find the City With the Smallest Number of Neighbors', difficulty: 'Medium', leetcodeId: 1334 },
         { name: 'All Paths From Source to Target', difficulty: 'Medium', leetcodeId: 797 },
         { name: 'Flood Fill', difficulty: 'Easy', leetcodeId: 733 },
         { name: 'Island Perimeter', difficulty: 'Easy', leetcodeId: 463 },
         { name: 'Max Area of Island', difficulty: 'Medium', leetcodeId: 695 },
         { name: 'Surrounded Regions', difficulty: 'Medium', leetcodeId: 130 },
         { name: 'Walls and Gates', difficulty: 'Medium', leetcodeId: 286 }
       ],
       dynamicProgramming: [
         { name: 'Climbing Stairs', difficulty: 'Easy', leetcodeId: 70 },
         { name: 'House Robber', difficulty: 'Medium', leetcodeId: 198 },
         { name: 'Longest Increasing Subsequence', difficulty: 'Medium', leetcodeId: 300 },
         { name: 'Coin Change', difficulty: 'Medium', leetcodeId: 322 },
         { name: 'Unique Paths', difficulty: 'Medium', leetcodeId: 62 },
         { name: 'Word Break', difficulty: 'Medium', leetcodeId: 139 },
         { name: 'Longest Common Subsequence', difficulty: 'Medium', leetcodeId: 1143 },
         { name: 'Edit Distance', difficulty: 'Hard', leetcodeId: 72 },
         { name: 'House Robber II', difficulty: 'Medium', leetcodeId: 213 },
         { name: 'Best Time to Buy and Sell Stock', difficulty: 'Easy', leetcodeId: 121 },
         { name: 'Best Time to Buy and Sell Stock II', difficulty: 'Medium', leetcodeId: 122 },
         { name: 'Best Time to Buy and Sell Stock III', difficulty: 'Hard', leetcodeId: 123 },
         { name: 'Best Time to Buy and Sell Stock IV', difficulty: 'Hard', leetcodeId: 188 },
         { name: 'Maximum Subarray', difficulty: 'Medium', leetcodeId: 53 },
         { name: 'Maximum Product Subarray', difficulty: 'Medium', leetcodeId: 152 },
         { name: 'Decode Ways', difficulty: 'Medium', leetcodeId: 91 },
         { name: 'Unique Binary Search Trees', difficulty: 'Medium', leetcodeId: 96 },
         { name: 'Perfect Squares', difficulty: 'Medium', leetcodeId: 279 },
         { name: 'Partition Equal Subset Sum', difficulty: 'Medium', leetcodeId: 416 }
       ],
       heaps: [
         { name: 'Kth Largest Element in an Array', difficulty: 'Medium', leetcodeId: 215 },
         { name: 'Top K Frequent Elements', difficulty: 'Medium', leetcodeId: 347 },
         { name: 'Merge k Sorted Lists', difficulty: 'Hard', leetcodeId: 23 },
         { name: 'Find Median from Data Stream', difficulty: 'Hard', leetcodeId: 295 },
         { name: 'Sliding Window Maximum', difficulty: 'Hard', leetcodeId: 239 },
         { name: 'Task Scheduler', difficulty: 'Medium', leetcodeId: 621 },
         { name: 'Reorganize String', difficulty: 'Medium', leetcodeId: 767 },
         { name: 'Furthest Building You Can Reach', difficulty: 'Medium', leetcodeId: 1642 },
         { name: 'Top K Frequent Words', difficulty: 'Medium', leetcodeId: 692 },
         { name: 'K Closest Points to Origin', difficulty: 'Medium', leetcodeId: 973 },
         { name: 'Sort Characters By Frequency', difficulty: 'Medium', leetcodeId: 451 },
         { name: 'Split Array into Consecutive Subsequences', difficulty: 'Medium', leetcodeId: 659 },
         { name: 'Design Twitter', difficulty: 'Medium', leetcodeId: 355 },
         { name: 'Find K Pairs with Smallest Sums', difficulty: 'Medium', leetcodeId: 373 },
         { name: 'Ugly Number II', difficulty: 'Medium', leetcodeId: 264 },
         { name: 'Super Ugly Number', difficulty: 'Medium', leetcodeId: 313 },
         { name: 'Find K-th Smallest Pair Distance', difficulty: 'Hard', leetcodeId: 719 },
         { name: 'Minimum Cost to Connect Sticks', difficulty: 'Medium', leetcodeId: 1167 },
         { name: 'Last Stone Weight', difficulty: 'Easy', leetcodeId: 1046 }
       ],
       binarySearch: [
         { name: 'Binary Search', difficulty: 'Easy', leetcodeId: 704 },
         { name: 'Search in Rotated Sorted Array', difficulty: 'Medium', leetcodeId: 33 },
         { name: 'Find First and Last Position of Element in Sorted Array', difficulty: 'Medium', leetcodeId: 34 },
         { name: 'Search a 2D Matrix', difficulty: 'Medium', leetcodeId: 74 },
         { name: 'Median of Two Sorted Arrays', difficulty: 'Hard', leetcodeId: 4 },
         { name: 'Split Array Largest Sum', difficulty: 'Hard', leetcodeId: 410 },
         { name: 'Capacity To Ship Packages Within D Days', difficulty: 'Medium', leetcodeId: 1011 },
         { name: 'Kth Smallest Element in a Sorted Matrix', difficulty: 'Medium', leetcodeId: 378 },
         { name: 'Search in Rotated Sorted Array II', difficulty: 'Medium', leetcodeId: 81 },
         { name: 'Find Minimum in Rotated Sorted Array', difficulty: 'Medium', leetcodeId: 153 },
         { name: 'Find Minimum in Rotated Sorted Array II', difficulty: 'Hard', leetcodeId: 154 },
         { name: 'Search a 2D Matrix II', difficulty: 'Medium', leetcodeId: 240 },
         { name: 'H-Index II', difficulty: 'Medium', leetcodeId: 275 },
         { name: 'Find the Duplicate Number', difficulty: 'Medium', leetcodeId: 287 },
         { name: 'Find Peak Element', difficulty: 'Medium', leetcodeId: 162 },
         { name: 'Valid Perfect Square', difficulty: 'Easy', leetcodeId: 367 },
         { name: 'Sqrt(x)', difficulty: 'Easy', leetcodeId: 69 },
         { name: 'Pow(x, n)', difficulty: 'Medium', leetcodeId: 50 },
         { name: 'Guess Number Higher or Lower', difficulty: 'Easy', leetcodeId: 374 }
       ]
    };

    // Define week structures based on timeline
    const weekStructures = {
      '30_days': [
        { focus: 'Foundation & Basics', topics: ['Arrays', 'Strings', 'Hash Tables'], problemSet: 'arrays' },
        { focus: 'Core Algorithms', topics: ['Two Pointers', 'Sliding Window', 'Binary Search'], problemSet: 'binarySearch' },
        { focus: 'Data Structures', topics: ['Linked Lists', 'Stacks', 'Queues'], problemSet: 'linkedLists' },
        { focus: 'Interview Readiness', topics: ['Trees', 'Graphs', 'Dynamic Programming'], problemSet: 'trees' }
      ],
      '60_days': [
        { focus: 'Arrays & Strings Foundation', topics: ['Arrays', 'Strings', 'Hash Tables'], problemSet: 'arrays' },
        { focus: 'Advanced Arrays', topics: ['Two Pointers', 'Sliding Window', 'Prefix Sum'], problemSet: 'arrays' },
        { focus: 'Binary Search & Sorting', topics: ['Binary Search', 'Sorting', 'Two Pointers'], problemSet: 'binarySearch' },
        { focus: 'Linked Lists & Stacks', topics: ['Linked Lists', 'Stacks', 'Queues'], problemSet: 'linkedLists' },
        { focus: 'Trees & Binary Search Trees', topics: ['Trees', 'Binary Search Trees', 'Tree Traversal'], problemSet: 'trees' },
        { focus: 'Graphs & BFS/DFS', topics: ['Graphs', 'Breadth-First Search', 'Depth-First Search'], problemSet: 'graphs' },
        { focus: 'Dynamic Programming', topics: ['Dynamic Programming', 'Memoization', 'Tabulation'], problemSet: 'dynamicProgramming' },
        { focus: 'Interview Preparation', topics: ['Company Specific', 'Mock Interviews', 'Review'], problemSet: 'heaps' }
      ],
      '90_days': [
        // First 8 weeks from 60-day plan
        { focus: 'Arrays & Strings Foundation', topics: ['Arrays', 'Strings', 'Hash Tables'], problemSet: 'arrays' },
        { focus: 'Advanced Arrays', topics: ['Two Pointers', 'Sliding Window', 'Prefix Sum'], problemSet: 'arrays' },
        { focus: 'Binary Search & Sorting', topics: ['Binary Search', 'Sorting', 'Two Pointers'], problemSet: 'binarySearch' },
        { focus: 'Linked Lists & Stacks', topics: ['Linked Lists', 'Stacks', 'Queues'], problemSet: 'linkedLists' },
        { focus: 'Trees & Binary Search Trees', topics: ['Trees', 'Binary Search Trees', 'Tree Traversal'], problemSet: 'trees' },
        { focus: 'Graphs & BFS/DFS', topics: ['Graphs', 'Breadth-First Search', 'Depth-First Search'], problemSet: 'graphs' },
        { focus: 'Dynamic Programming', topics: ['Dynamic Programming', 'Memoization', 'Tabulation'], problemSet: 'dynamicProgramming' },
        { focus: 'Interview Preparation', topics: ['Company Specific', 'Mock Interviews', 'Review'], problemSet: 'heaps' },
        // Additional weeks for 90-day plan
        { focus: 'Advanced Dynamic Programming', topics: ['DP on Trees', 'DP on Graphs', 'State Compression'], problemSet: 'dynamicProgramming' },
        { focus: 'Greedy Algorithms', topics: ['Greedy', 'Sorting', 'Priority Queues'], problemSet: 'heaps' },
        { focus: 'Backtracking', topics: ['Backtracking', 'Recursion', 'State Space Search'], problemSet: 'arrays' },
        { focus: 'Advanced Data Structures', topics: ['Heaps', 'Tries', 'Union Find'], problemSet: 'heaps' },
        { focus: 'System Design Basics', topics: ['Design Patterns', 'Scalability', 'Trade-offs'], problemSet: 'arrays' }
      ],
      '180_days': [
        // First 13 weeks from 90-day plan
        { focus: 'Arrays & Strings Foundation', topics: ['Arrays', 'Strings', 'Hash Tables'], problemSet: 'arrays' },
        { focus: 'Advanced Arrays', topics: ['Two Pointers', 'Sliding Window', 'Prefix Sum'], problemSet: 'arrays' },
        { focus: 'Binary Search & Sorting', topics: ['Binary Search', 'Sorting', 'Two Pointers'], problemSet: 'binarySearch' },
        { focus: 'Linked Lists & Stacks', topics: ['Linked Lists', 'Stacks', 'Queues'], problemSet: 'linkedLists' },
        { focus: 'Trees & Binary Search Trees', topics: ['Trees', 'Binary Search Trees', 'Tree Traversal'], problemSet: 'trees' },
        { focus: 'Graphs & BFS/DFS', topics: ['Graphs', 'Breadth-First Search', 'Depth-First Search'], problemSet: 'graphs' },
        { focus: 'Dynamic Programming', topics: ['Dynamic Programming', 'Memoization', 'Tabulation'], problemSet: 'dynamicProgramming' },
        { focus: 'Interview Preparation', topics: ['Company Specific', 'Mock Interviews', 'Review'], problemSet: 'heaps' },
        { focus: 'Advanced Dynamic Programming', topics: ['DP on Trees', 'DP on Graphs', 'State Compression'], problemSet: 'dynamicProgramming' },
        { focus: 'Greedy Algorithms', topics: ['Greedy', 'Sorting', 'Priority Queues'], problemSet: 'heaps' },
        { focus: 'Backtracking', topics: ['Backtracking', 'Recursion', 'State Space Search'], problemSet: 'arrays' },
        { focus: 'Advanced Data Structures', topics: ['Heaps', 'Tries', 'Union Find'], problemSet: 'heaps' },
        { focus: 'System Design Basics', topics: ['Design Patterns', 'Scalability', 'Trade-offs'], problemSet: 'arrays' },
        // Additional weeks for 180-day plan
        { focus: 'Advanced Graph Algorithms', topics: ['Shortest Path', 'Minimum Spanning Tree', 'Topological Sort'], problemSet: 'graphs' },
        { focus: 'String Algorithms', topics: ['KMP', 'Rabin-Karp', 'Suffix Arrays'], problemSet: 'strings' },
        { focus: 'Bit Manipulation', topics: ['Bit Operations', 'Bit Masks', 'Bit Counting'], problemSet: 'arrays' },
        { focus: 'Advanced Trees', topics: ['AVL Trees', 'Red-Black Trees', 'Segment Trees'], problemSet: 'trees' },
        { focus: 'Advanced Dynamic Programming', topics: ['Digit DP', 'Tree DP', 'Optimization'], problemSet: 'dynamicProgramming' },
        { focus: 'Game Theory', topics: ['Nim Game', 'Grundy Numbers', 'Minimax'], problemSet: 'arrays' },
        { focus: 'Advanced Greedy', topics: ['Interval Scheduling', 'Fractional Knapsack', 'Huffman Coding'], problemSet: 'heaps' },
        { focus: 'Advanced Backtracking', topics: ['Constraint Satisfaction', 'Optimization', 'Pruning'], problemSet: 'arrays' },
        { focus: 'Advanced Data Structures', topics: ['Skip Lists', 'B-Trees', 'Persistent Data Structures'], problemSet: 'heaps' },
        { focus: 'System Design', topics: ['Distributed Systems', 'Caching', 'Load Balancing'], problemSet: 'arrays' },
        { focus: 'Competitive Programming', topics: ['Fast I/O', 'Optimization', 'Advanced Algorithms'], problemSet: 'arrays' },
        { focus: 'Interview Mastery', topics: ['Mock Interviews', 'Problem Solving', 'Communication'], problemSet: 'arrays' },
        { focus: 'Final Review & Practice', topics: ['Review', 'Practice', 'Mock Interviews'], problemSet: 'arrays' }
      ]
    };

    const weekStructure = weekStructures[timelineKey] || weekStructures['60_days'];

    for (let i = 1; i <= weekCount; i++) {
      const weekConfig = weekStructure[i - 1] || weekStructure[weekStructure.length - 1];
      const problemSet = problemSets[weekConfig.problemSet] || problemSets.arrays;
      
             // Select problems for this week based on timeline intensity
       let problemCount;
       switch (timelineKey) {
         case '30_days':
           problemCount = 15; // 15 hours/week = ~15 problems
           break;
         case '60_days':
           problemCount = 12; // 12 hours/week = ~12 problems
           break;
         case '90_days':
           problemCount = 10; // 10 hours/week = ~10 problems
           break;
         case '180_days':
           problemCount = 8; // 8 hours/week = ~8 problems
           break;
         default:
           problemCount = 10;
       }
       
       const selectedProblems = problemSet.slice(0, problemCount);
      
      const weekData = {
        weekNumber: i,
        focus: weekConfig.focus,
        topics: weekConfig.topics,
        targetProblems: selectedProblems.length,
        keyProblems: selectedProblems.map(p => p.name),
        problems: selectedProblems, // Full problem details
        description: `Week ${i}: ${weekConfig.focus}. Master ${weekConfig.topics.join(', ')} through hands-on problem solving.`,
        milestones: { 
          problemsSolved: selectedProblems.length, 
          confidence: i <= 4 ? 'High' : i <= 8 ? 'Medium' : 'Low',
          targetCompletion: `${Math.round((i / weekCount) * 100)}%`
        }
      };

      weeks.push(weekData);
    }

    return {
      timeline: timeline,
      userLevel: { name: 'Intermediate' },
      targetCompanies: ['Google', 'Amazon', 'Microsoft'],
      startDate: new Date(),
      endDate: new Date(Date.now() + timeline.days * 24 * 60 * 60 * 1000),
      weeks: weeks,
      summary: {
        totalProblems: weeks.reduce((sum, week) => sum + week.targetProblems, 0),
        totalWeeks: weeks.length,
        problemsPerWeek: Math.round(weeks.reduce((sum, week) => sum + week.targetProblems, 0) / weeks.length),
        userLevel: 'Intermediate',
        timeline: timeline.description,
        estimatedHoursPerWeek: timeline.estimatedHoursPerWeek,
        successProbability: 0.85
      },
      prerequisites: [
        'Basic programming knowledge in any language (Java, Python, C++)',
        'Understanding of variables, loops, and functions',
        'Basic math skills (arithmetic, algebra)',
        'Willingness to learn and practice daily'
      ],
      successMetrics: [
        `Solve ${timeline.days * 2} problems with 80%+ success rate`,
        'Complete all weekly milestones on time',
        'Achieve confidence level "High" in 70% of topics',
        'Successfully complete 3+ mock interviews',
        'Score 85%+ on practice assessments'
      ]
    };
  };

  useEffect(() => {
    // Test backend connection first
    const testBackend = async () => {
      try {
        console.log('Testing backend connection...');
        const testResult = await roadmapAPI.testConnection();
        console.log('Backend test result:', testResult);
      } catch (error) {
        console.error('Backend test failed:', error);
      }
    };
    
    testBackend();
    loadTimelines();
    loadProgress();
    loadRecommendations();
  }, []);

  const loadTimelines = async () => {
    try {
      setError(null);
      console.log('Attempting to load timelines from backend...');
      const data = await roadmapAPI.getTimelines();
      console.log('Backend timelines response:', data);
      setTimelines(data.timelines);
    } catch (error) {
      console.error('Failed to load timelines, using mock data:', error);
      console.log('Error details:', error.response?.status, error.response?.data);
      setTimelines(mockTimelines);
    }
  };

  const loadProgress = async () => {
    try {
      const data = await roadmapAPI.getProgress();
      setProgress(data);
    } catch (error) {
      console.error('Failed to load progress, using mock data:', error);
      setProgress(mockProgress);
    }
  };

  const loadRecommendations = async () => {
    try {
      const data = await roadmapAPI.getRecommendations();
      setRecommendations(data.recommendations);
    } catch (error) {
      console.error('Failed to load recommendations, using mock data:', error);
      setRecommendations(mockRecommendations);
    }
  };

  const generateRoadmap = async (timelineKey) => {
    setLoading(true);
    setError(null);
    try {
      console.log('Attempting to generate roadmap for timeline:', timelineKey);
      const data = await roadmapAPI.generateRoadmap(timelineKey);
      console.log('Backend roadmap response:', data);
      setRoadmap(data);
      setSelectedTimeline(timelineKey);
    } catch (error) {
      console.error('Failed to generate roadmap, using mock data:', error);
      console.log('Error details:', error.response?.status, error.response?.data);
      const mockRoadmap = generateMockRoadmap(timelineKey);
      setRoadmap(mockRoadmap);
      setSelectedTimeline(timelineKey);
    } finally {
      setLoading(false);
    }
  };

  const updateProgress = async (weekNumber, problemsCompleted, event) => {
    try {
      console.log(`Updating progress: Week ${weekNumber}, Problems: ${problemsCompleted}`);
      
      // Show loading state
      const button = event?.target;
      const originalText = button?.textContent;
      if (button) {
        button.disabled = true;
        button.textContent = 'Updating...';
      }
      
      await roadmapAPI.updateProgress(weekNumber, problemsCompleted);
      
      // Reload progress
      await loadProgress();
      
      // Mark week as completed
      setCompletedWeeks(prev => new Set([...prev, weekNumber]));
      
      // Show success message
      toast({
        title: "Success!",
        description: `Week ${weekNumber} marked as complete! ${problemsCompleted} problems added to your progress.`,
        variant: "default"
      });
      
    } catch (error) {
      console.error('Failed to update progress:', error);
      
      // Update local progress for demo
      if (progress) {
        const newProgress = {
          ...progress,
          problemsCompleted: progress.problemsCompleted + problemsCompleted,
          completionPercentage: Math.min(100, ((progress.problemsCompleted + problemsCompleted) / progress.targetProblems) * 100)
        };
        setProgress(newProgress);
        
        // Mark week as completed
        setCompletedWeeks(prev => new Set([...prev, weekNumber]));
        
        toast({
          title: "Progress Updated",
          description: `Week ${weekNumber} completed! (Using demo data)`,
          variant: "default"
        });
      } else {
        toast({
          title: "Error",
          description: "Failed to update progress. Please try again.",
          variant: "destructive"
        });
      }
    } finally {
      // Reset button state
      const button = event?.target;
      if (button) {
        button.disabled = false;
        button.textContent = 'Mark Complete';
      }
    }
  };

  const getTimelineIcon = (difficulty) => {
    switch (difficulty) {
      case 'Intensive':
        return <Play className="w-4 h-4 text-red-500" />;
      case 'Moderate':
        return <Pause className="w-4 h-4 text-yellow-500" />;
      case 'Balanced':
        return <RotateCcw className="w-4 h-4 text-blue-500" />;
      case 'Relaxed':
        return <CheckCircle className="w-4 h-4 text-green-500" />;
      default:
        return <Clock className="w-4 h-4" />;
    }
  };

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'Intensive':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'Moderate':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Balanced':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'Relaxed':
        return 'bg-green-100 text-green-800 border-green-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="text-center space-y-4 animate-slide-in-up">
        <h1 className="text-4xl font-bold text-gradient">
          🗺️ Study Roadmap Generator
        </h1>
        <p className="text-lg text-muted-foreground">
          Choose your timeline and get a personalized study plan for your DSA journey
        </p>
      </div>

      {/* Error Alert */}
      {error && (
        <Card className="border-warning/50 bg-warning/10 animate-fade-in-scale">
          <CardContent className="p-4">
            <div className="flex items-center gap-3 text-warning">
              <div className="p-1 bg-warning/20 rounded-full">
                <AlertCircle className="w-4 h-4" />
              </div>
              <span className="text-sm font-medium">{error}</span>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Timeline Selection */}
      <Card className="card-premium animate-fade-in-scale">
        <CardHeader>
          <CardTitle className="flex items-center gap-3">
            <div className="p-2 bg-primary/10 rounded-lg">
              <Calendar className="w-5 h-5 text-primary" />
            </div>
            <span className="text-gradient font-semibold">Choose Your Timeline</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {timelines.map((timeline, index) => (
              <Card
                key={timeline.key}
                className={`card-premium cursor-pointer transition-all duration-500 hover:shadow-glow hover:scale-105 animate-slide-in-up ${
                  selectedTimeline === timeline.key ? 'ring-2 ring-primary shadow-glow' : ''
                }`}
                onClick={() => generateRoadmap(timeline.key)}
                style={{ animationDelay: `${index * 0.1}s` }}
              >
                <CardContent className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center gap-3">
                      <div className="p-2 bg-primary/10 rounded-lg">
                        {getTimelineIcon(timeline.difficulty)}
                      </div>
                      <span className="font-bold text-lg">{timeline.days} Days</span>
                    </div>
                    <Badge className={`${getDifficultyColor(timeline.difficulty)} font-semibold`}>
                      {timeline.difficulty}
                    </Badge>
                  </div>
                  <p className="text-sm text-muted-foreground mb-4 leading-relaxed">
                    {timeline.description}
                  </p>
                  <div className="space-y-2 text-xs text-muted-foreground mb-4">
                    <div className="flex justify-between items-center p-2 bg-muted/30 rounded-lg">
                      <span>Weeks:</span>
                      <span className="font-semibold">{timeline.weeks}</span>
                    </div>
                    <div className="flex justify-between items-center p-2 bg-muted/30 rounded-lg">
                      <span>Hours/Week:</span>
                      <span className="font-semibold">{timeline.estimatedHoursPerWeek}</span>
                    </div>
                  </div>
                  <div className="pt-4 border-t border-border/30">
                    <Button 
                      className="w-full btn-premium text-white font-semibold"
                      onClick={(e) => {
                        e.stopPropagation();
                        generateRoadmap(timeline.key);
                      }}
                    >
                      Generate Roadmap
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Progress Overview */}
      {progress && (
        <Card className="card-premium animate-fade-in-scale">
          <CardHeader>
            <CardTitle className="flex items-center gap-3">
              <div className="p-2 bg-success/10 rounded-lg">
                <Target className="w-5 h-5 text-success" />
              </div>
              <span className="text-gradient font-semibold">Your Progress</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-primary mb-2">
                  {progress.currentWeek}/{progress.totalWeeks}
                </div>
                <div className="text-sm text-muted-foreground font-medium">Current Week</div>
              </div>
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-success mb-2">
                  {progress.problemsCompleted}/{progress.targetProblems}
                </div>
                <div className="text-sm text-muted-foreground font-medium">Problems Solved</div>
              </div>
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-accent mb-2">
                  {progress.completionPercentage.toFixed(1)}%
                </div>
                <div className="text-sm text-muted-foreground font-medium">Completion</div>
              </div>
            </div>
            <div className="mt-6">
              <Progress value={progress.completionPercentage} className="h-4" />
            </div>
            <div className="mt-4 flex items-center gap-3 p-3 bg-muted/30 rounded-lg">
              {progress.onTrack ? (
                <div className="p-1 bg-success/20 rounded-full">
                  <CheckCircle className="w-4 h-4 text-success" />
                </div>
              ) : (
                <div className="p-1 bg-warning/20 rounded-full">
                  <ArrowRight className="w-4 h-4 text-warning" />
                </div>
              )}
              <span className="text-sm text-muted-foreground font-medium">{progress.nextMilestone}</span>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Recommendations */}
      {recommendations.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Trophy className="w-5 h-5" />
              AI Recommendations
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {recommendations.map((rec, index) => (
                <div key={index} className="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-800 rounded-lg">
                  <div>
                    <div className="font-semibold capitalize">{rec.timeline.replace('_', ' ')}</div>
                    <div className="text-sm text-gray-600">{rec.reason}</div>
                  </div>
                  <div className="text-right">
                    <div className="text-sm font-semibold">{rec.confidence}% confidence</div>
                    <div className="text-xs text-gray-500">{rec.estimatedSuccessRate}% success rate</div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Roadmap Details */}
      {roadmap && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <BookOpen className="w-5 h-5" />
              Your Personalized Roadmap
            </CardTitle>
            <div className="text-sm text-gray-600">
              {roadmap.userLevel.name} Level • {roadmap.timeline.description} • 
              {roadmap.targetCompanies.join(', ')} Focus
            </div>
          </CardHeader>
          <CardContent>
            {/* Week Navigation for Long Timelines */}
            {roadmap.weeks.length > 8 && (
              <div className="flex items-center justify-between mb-4 p-2 bg-gray-50 dark:bg-gray-800 rounded-lg">
                <Button 
                  variant="outline" 
                  size="sm"
                  onClick={() => setActiveWeek(Math.max(1, activeWeek - 1))}
                  disabled={activeWeek === 1}
                >
                  ← Previous Week
                </Button>
                <div className="text-sm font-medium">
                  Week {activeWeek} of {roadmap.weeks.length}
                </div>
                <Button 
                  variant="outline" 
                  size="sm"
                  onClick={() => setActiveWeek(Math.min(roadmap.weeks.length, activeWeek + 1))}
                  disabled={activeWeek === roadmap.weeks.length}
                >
                  Next Week →
                </Button>
              </div>
            )}
            
            <Tabs value={activeWeek.toString()} onValueChange={(value) => setActiveWeek(parseInt(value))}>
                              <TabsList className="grid w-full grid-cols-4 lg:grid-cols-8 xl:grid-cols-12 max-h-16 overflow-y-auto">
                  {roadmap.weeks.map((week, index) => (
                    <TabsTrigger 
                      key={week.weekNumber} 
                      value={week.weekNumber.toString()} 
                      className={`text-xs ${completedWeeks.has(week.weekNumber) ? 'bg-green-100 text-green-800 border-green-300' : ''}`}
                    >
                      {completedWeeks.has(week.weekNumber) && (
                        <CheckCircle className="w-3 h-3 mr-1" />
                      )}
                      W{week.weekNumber}
                    </TabsTrigger>
                  ))}
                </TabsList>
              
              {roadmap.weeks.map((week) => (
                <TabsContent key={week.weekNumber} value={week.weekNumber.toString()}>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h3 className="text-xl font-semibold">{week.focus}</h3>
                        <p className="text-gray-600">{week.description}</p>
                      </div>
                      <Button 
                        onClick={(e) => updateProgress(week.weekNumber, week.targetProblems, e)}
                        className={completedWeeks.has(week.weekNumber) 
                          ? "bg-green-700 hover:bg-green-800" 
                          : "bg-green-600 hover:bg-green-700"
                        }
                        disabled={completedWeeks.has(week.weekNumber)}
                      >
                        {completedWeeks.has(week.weekNumber) ? (
                          <>
                            <CheckCircle className="w-4 h-4 mr-2" />
                            Completed
                          </>
                        ) : (
                          'Mark Complete'
                        )}
                      </Button>
                    </div>
                    
                                         <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                       <div>
                         <h4 className="font-semibold mb-2">Topics Covered</h4>
                         <div className="flex flex-wrap gap-2">
                           {week.topics.map((topic) => (
                             <Badge key={topic} variant="secondary">
                               {topic}
                             </Badge>
                           ))}
                         </div>
                       </div>
                       
                       <div>
                         <h4 className="font-semibold mb-2">Key Problems</h4>
                         <div className="space-y-1">
                           {week.keyProblems.map((problem) => (
                             <div key={problem} className="text-sm text-gray-600 flex items-center gap-2">
                               <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                               {problem}
                             </div>
                           ))}
                         </div>
                       </div>
                     </div>

                     {/* Detailed Problem List */}
                     <div className="mt-6">
                       <h4 className="font-semibold mb-3">Problems to Solve This Week</h4>
                       <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                         {week.problems && week.problems.map((problem, index) => (
                           <div key={index} className="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-800 rounded-lg border">
                             <div className="flex items-center gap-3">
                               <div className={`w-3 h-3 rounded-full ${
                                 problem.difficulty === 'Easy' ? 'bg-green-500' :
                                 problem.difficulty === 'Medium' ? 'bg-yellow-500' : 'bg-red-500'
                               }`}></div>
                               <div>
                                 <div className="font-medium text-sm">{problem.name}</div>
                                 <div className="text-xs text-gray-500">LeetCode #{problem.leetcodeId}</div>
                               </div>
                             </div>
                             <div className="flex items-center gap-2">
                               <Badge className={
                                 problem.difficulty === 'Easy' ? 'bg-green-100 text-green-800' :
                                 problem.difficulty === 'Medium' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'
                               }>
                                 {problem.difficulty}
                               </Badge>
                               <Button 
                                 size="sm" 
                                 variant="outline"
                                 onClick={() => window.open(`https://leetcode.com/problems/${problem.name.toLowerCase().replace(/\s+/g, '-')}`, '_blank')}
                               >
                                 Solve
                               </Button>
                             </div>
                           </div>
                         ))}
                       </div>
                     </div>
                    
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                      <div className="text-center p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg">
                        <div className="text-2xl font-bold text-blue-600">{week.targetProblems}</div>
                        <div className="text-sm text-gray-600">Target Problems</div>
                      </div>
                      <div className="text-center p-3 bg-green-50 dark:bg-green-900/20 rounded-lg">
                        <div className="text-2xl font-bold text-green-600">
                          {week.milestones.confidence}
                        </div>
                        <div className="text-sm text-gray-600">Confidence Level</div>
                      </div>
                      <div className="text-center p-3 bg-purple-50 dark:bg-purple-900/20 rounded-lg">
                        <div className="text-2xl font-bold text-purple-600">
                          {roadmap.summary.estimatedHoursPerWeek}
                        </div>
                        <div className="text-sm text-gray-600">Hours/Week</div>
                      </div>
                    </div>
                  </div>
                </TabsContent>
              ))}
            </Tabs>
          </CardContent>
        </Card>
      )}

      {/* Roadmap Summary */}
      {roadmap && (
        <Card>
          <CardHeader>
            <CardTitle>Roadmap Summary</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <h4 className="font-semibold mb-3">Prerequisites</h4>
                <ul className="space-y-2">
                  {roadmap.prerequisites.map((prereq, index) => (
                    <li key={index} className="flex items-start gap-2">
                      <CheckCircle className="w-4 h-4 text-green-500 mt-0.5 flex-shrink-0" />
                      <span className="text-sm">{prereq}</span>
                    </li>
                  ))}
                </ul>
              </div>
              
              <div>
                <h4 className="font-semibold mb-3">Success Metrics</h4>
                <ul className="space-y-2">
                  {roadmap.successMetrics.map((metric, index) => (
                    <li key={index} className="flex items-start gap-2">
                      <Target className="w-4 h-4 text-blue-500 mt-0.5 flex-shrink-0" />
                      <span className="text-sm">{metric}</span>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
            
            <div className="mt-6 p-4 bg-gradient-to-r from-blue-50 to-purple-50 dark:from-blue-900/20 dark:to-purple-900/20 rounded-lg">
              <div className="text-center">
                <div className="text-2xl font-bold text-blue-600">
                  {Math.round(roadmap.summary.successProbability * 100)}%
                </div>
                <div className="text-sm text-gray-600">Estimated Success Probability</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {loading && (
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-2 text-gray-600">Generating your personalized roadmap...</p>
        </div>
      )}
    </div>
  );
};

export default RoadmapGenerator;
