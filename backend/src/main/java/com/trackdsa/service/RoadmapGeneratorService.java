package com.trackdsa.service;

import com.trackdsa.model.Problem;
import com.trackdsa.model.User;
import com.trackdsa.model.UserProblemAttempt;
import com.trackdsa.repository.ProblemRepository;
import com.trackdsa.repository.UserProblemAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RoadmapGeneratorService {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserProblemAttemptRepository attemptRepository;

    @Autowired
    private UserService userService;

    // Roadmap timeline options
    public enum Timeline {
        THIRTY_DAYS(30, "30 Days - Quick Prep"),
        SIXTY_DAYS(60, "60 Days - Standard Prep"),
        NINETY_DAYS(90, "90 Days - Comprehensive Prep"),
        ONE_EIGHTY_DAYS(180, "180 Days - Deep Dive");

        private final int days;
        private final String description;

        Timeline(int days, String description) {
            this.days = days;
            this.description = description;
        }

        public int getDays() { return days; }
        public String getDescription() { return description; }
    }

    // User experience levels
    public enum ExperienceLevel {
        BEGINNER("Beginner", 0, 10),
        INTERMEDIATE("Intermediate", 11, 50),
        ADVANCED("Advanced", 51, 100),
        EXPERT("Expert", 101, Integer.MAX_VALUE);

        private final String name;
        private final int minProblems;
        private final int maxProblems;

        ExperienceLevel(String name, int minProblems, int maxProblems) {
            this.name = name;
            this.minProblems = minProblems;
            this.maxProblems = maxProblems;
        }

        public String getName() { return name; }
        public int getMinProblems() { return minProblems; }
        public int getMaxProblems() { return maxProblems; }
    }

    public static class RoadmapWeek {
        private int weekNumber;
        private String focus;
        private List<String> topics;
        private int targetProblems;
        private List<String> keyProblems;
        private String description;
        private Map<String, Object> milestones;

        public RoadmapWeek(int weekNumber, String focus, List<String> topics, 
                          int targetProblems, List<String> keyProblems, 
                          String description, Map<String, Object> milestones) {
            this.weekNumber = weekNumber;
            this.focus = focus;
            this.topics = topics;
            this.targetProblems = targetProblems;
            this.keyProblems = keyProblems;
            this.description = description;
            this.milestones = milestones;
        }

        // Getters
        public int getWeekNumber() { return weekNumber; }
        public String getFocus() { return focus; }
        public List<String> getTopics() { return topics; }
        public int getTargetProblems() { return targetProblems; }
        public List<String> getKeyProblems() { return keyProblems; }
        public String getDescription() { return description; }
        public Map<String, Object> getMilestones() { return milestones; }
    }

    public static class Roadmap {
        private Timeline timeline;
        private ExperienceLevel userLevel;
        private List<String> targetCompanies;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<RoadmapWeek> weeks;
        private Map<String, Object> summary;
        private List<String> prerequisites;
        private List<String> successMetrics;

        public Roadmap(Timeline timeline, ExperienceLevel userLevel, List<String> targetCompanies,
                      LocalDate startDate, LocalDate endDate, List<RoadmapWeek> weeks,
                      Map<String, Object> summary, List<String> prerequisites, List<String> successMetrics) {
            this.timeline = timeline;
            this.userLevel = userLevel;
            this.targetCompanies = targetCompanies;
            this.startDate = startDate;
            this.endDate = endDate;
            this.weeks = weeks;
            this.summary = summary;
            this.prerequisites = prerequisites;
            this.successMetrics = successMetrics;
        }

        // Getters
        public Timeline getTimeline() { return timeline; }
        public ExperienceLevel getUserLevel() { return userLevel; }
        public List<String> getTargetCompanies() { return targetCompanies; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public List<RoadmapWeek> getWeeks() { return weeks; }
        public Map<String, Object> getSummary() { return summary; }
        public List<String> getPrerequisites() { return prerequisites; }
        public List<String> getSuccessMetrics() { return successMetrics; }
    }

    // Main method to generate roadmap
    public Roadmap generateRoadmap(String userId, Timeline timeline) {
        User user = userService.findById(userId).orElseThrow(() -> 
            new RuntimeException("User not found"));

        // Assess user's current level
        ExperienceLevel userLevel = assessUserLevel(userId);
        
        // Get user's target companies
        List<String> targetCompanies = user.getTargetCompanies() != null ? 
            user.getTargetCompanies() : Arrays.asList("Google", "Amazon", "Microsoft");

        // Calculate dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(timeline.getDays());

        // Generate weekly breakdown
        List<RoadmapWeek> weeks = generateWeeklyBreakdown(timeline, userLevel, targetCompanies);

        // Generate summary
        Map<String, Object> summary = generateSummary(timeline, userLevel, weeks);

        // Generate prerequisites
        List<String> prerequisites = generatePrerequisites(userLevel);

        // Generate success metrics
        List<String> successMetrics = generateSuccessMetrics(timeline, userLevel);

        return new Roadmap(timeline, userLevel, targetCompanies, startDate, endDate, 
                          weeks, summary, prerequisites, successMetrics);
    }

    private ExperienceLevel assessUserLevel(String userId) {
        List<UserProblemAttempt> attempts = attemptRepository.findByUserId(userId);
        long solvedCount = attempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()))
            .count();

        if (solvedCount <= 10) return ExperienceLevel.BEGINNER;
        else if (solvedCount <= 50) return ExperienceLevel.INTERMEDIATE;
        else if (solvedCount <= 100) return ExperienceLevel.ADVANCED;
        else return ExperienceLevel.EXPERT;
    }

    private List<RoadmapWeek> generateWeeklyBreakdown(Timeline timeline, ExperienceLevel userLevel, 
                                                     List<String> targetCompanies) {
        List<RoadmapWeek> weeks = new ArrayList<>();
        int totalWeeks = timeline.getDays() / 7;

        switch (timeline) {
            case THIRTY_DAYS:
                weeks = generate30DayRoadmap(userLevel, targetCompanies);
                break;
            case SIXTY_DAYS:
                weeks = generate60DayRoadmap(userLevel, targetCompanies);
                break;
            case NINETY_DAYS:
                weeks = generate90DayRoadmap(userLevel, targetCompanies);
                break;
            case ONE_EIGHTY_DAYS:
                weeks = generate180DayRoadmap(userLevel, targetCompanies);
                break;
        }

        return weeks;
    }

    private List<RoadmapWeek> generate30DayRoadmap(ExperienceLevel userLevel, List<String> targetCompanies) {
        List<RoadmapWeek> weeks = new ArrayList<>();

        // Week 1: Foundation
        weeks.add(new RoadmapWeek(1, "Foundation & Basics", 
            Arrays.asList("Arrays", "Strings", "Hash Tables"), 
            getTargetProblems(userLevel, 1),
            Arrays.asList("Two Sum", "Valid Parentheses", "Contains Duplicate"),
            "Build strong foundation with fundamental data structures and algorithms",
            Map.of("problemsSolved", getTargetProblems(userLevel, 1), "confidence", "High")));

        // Week 2: Core Concepts
        weeks.add(new RoadmapWeek(2, "Core Algorithms", 
            Arrays.asList("Two Pointers", "Sliding Window", "Binary Search"), 
            getTargetProblems(userLevel, 2),
            Arrays.asList("3Sum", "Longest Substring Without Repeating", "Search in Rotated Array"),
            "Master essential algorithmic patterns used in interviews",
            Map.of("problemsSolved", getTargetProblems(userLevel, 2), "confidence", "Medium")));

        // Week 3: Data Structures
        weeks.add(new RoadmapWeek(3, "Data Structures", 
            Arrays.asList("Linked Lists", "Stacks", "Queues"), 
            getTargetProblems(userLevel, 3),
            Arrays.asList("Add Two Numbers", "Valid Parentheses", "Implement Stack"),
            "Deep dive into linear data structures and their applications",
            Map.of("problemsSolved", getTargetProblems(userLevel, 3), "confidence", "Medium")));

        // Week 4: Interview Prep
        weeks.add(new RoadmapWeek(4, "Interview Readiness", 
            Arrays.asList("Trees", "Graphs", "Dynamic Programming"), 
            getTargetProblems(userLevel, 4),
            Arrays.asList("Binary Tree Level Order", "Number of Islands", "Climbing Stairs"),
            "Focus on advanced topics and company-specific problems",
            Map.of("problemsSolved", getTargetProblems(userLevel, 4), "confidence", "High")));

        return weeks;
    }

    private List<RoadmapWeek> generate60DayRoadmap(ExperienceLevel userLevel, List<String> targetCompanies) {
        List<RoadmapWeek> weeks = new ArrayList<>();

        // Weeks 1-2: Foundation
        weeks.add(new RoadmapWeek(1, "Arrays & Strings Foundation", 
            Arrays.asList("Arrays", "Strings", "Hash Tables"), 
            getTargetProblems(userLevel, 1),
            Arrays.asList("Two Sum", "Valid Parentheses", "Longest Common Prefix"),
            "Master fundamental array and string manipulation",
            Map.of("problemsSolved", getTargetProblems(userLevel, 1), "confidence", "High")));

        weeks.add(new RoadmapWeek(2, "Advanced Arrays", 
            Arrays.asList("Two Pointers", "Sliding Window", "Prefix Sum"), 
            getTargetProblems(userLevel, 2),
            Arrays.asList("3Sum", "Container With Most Water", "Subarray Sum Equals K"),
            "Learn advanced array techniques and optimization",
            Map.of("problemsSolved", getTargetProblems(userLevel, 2), "confidence", "Medium")));

        // Weeks 3-4: Core Algorithms
        weeks.add(new RoadmapWeek(3, "Binary Search & Sorting", 
            Arrays.asList("Binary Search", "Sorting", "Two Pointers"), 
            getTargetProblems(userLevel, 3),
            Arrays.asList("Search in Rotated Array", "Merge Intervals", "Sort Colors"),
            "Master binary search and sorting algorithms",
            Map.of("problemsSolved", getTargetProblems(userLevel, 3), "confidence", "Medium")));

        weeks.add(new RoadmapWeek(4, "Linked Lists & Stacks", 
            Arrays.asList("Linked Lists", "Stacks", "Queues"), 
            getTargetProblems(userLevel, 4),
            Arrays.asList("Add Two Numbers", "Reverse Linked List", "Valid Parentheses"),
            "Deep dive into linear data structures",
            Map.of("problemsSolved", getTargetProblems(userLevel, 4), "confidence", "Medium")));

        // Weeks 5-6: Trees & Graphs
        weeks.add(new RoadmapWeek(5, "Trees & Binary Search Trees", 
            Arrays.asList("Trees", "Binary Search Trees", "Tree Traversal"), 
            getTargetProblems(userLevel, 5),
            Arrays.asList("Binary Tree Level Order", "Validate BST", "Kth Smallest in BST"),
            "Master tree data structures and traversal",
            Map.of("problemsSolved", getTargetProblems(userLevel, 5), "confidence", "Medium")));

        weeks.add(new RoadmapWeek(6, "Graphs & BFS/DFS", 
            Arrays.asList("Graphs", "Breadth-First Search", "Depth-First Search"), 
            getTargetProblems(userLevel, 6),
            Arrays.asList("Number of Islands", "Clone Graph", "Course Schedule"),
            "Learn graph algorithms and search techniques",
            Map.of("problemsSolved", getTargetProblems(userLevel, 6), "confidence", "Low")));

        // Weeks 7-8: Advanced Topics
        weeks.add(new RoadmapWeek(7, "Dynamic Programming", 
            Arrays.asList("Dynamic Programming", "Memoization", "Tabulation"), 
            getTargetProblems(userLevel, 7),
            Arrays.asList("Climbing Stairs", "House Robber", "Longest Increasing Subsequence"),
            "Introduction to dynamic programming",
            Map.of("problemsSolved", getTargetProblems(userLevel, 7), "confidence", "Low")));

        weeks.add(new RoadmapWeek(8, "Interview Preparation", 
            Arrays.asList("Company Specific", "Mock Interviews", "Review"), 
            getTargetProblems(userLevel, 8),
            Arrays.asList("LRU Cache", "Design Add and Search Words", "Top K Frequent Elements"),
            "Final preparation with company-specific problems",
            Map.of("problemsSolved", getTargetProblems(userLevel, 8), "confidence", "High")));

        return weeks;
    }

    private List<RoadmapWeek> generate90DayRoadmap(ExperienceLevel userLevel, List<String> targetCompanies) {
        List<RoadmapWeek> weeks = new ArrayList<>();

        // Weeks 1-3: Foundation (More comprehensive)
        weeks.addAll(generate60DayRoadmap(userLevel, targetCompanies));

        // Weeks 9-13: Advanced Topics
        weeks.add(new RoadmapWeek(9, "Advanced Dynamic Programming", 
            Arrays.asList("DP on Trees", "DP on Graphs", "State Compression"), 
            getTargetProblems(userLevel, 9),
            Arrays.asList("House Robber III", "Unique Paths", "Coin Change"),
            "Advanced dynamic programming techniques",
            Map.of("problemsSolved", getTargetProblems(userLevel, 9), "confidence", "Low")));

        weeks.add(new RoadmapWeek(10, "Greedy Algorithms", 
            Arrays.asList("Greedy", "Sorting", "Priority Queues"), 
            getTargetProblems(userLevel, 10),
            Arrays.asList("Jump Game", "Meeting Rooms", "Task Scheduler"),
            "Master greedy algorithm design",
            Map.of("problemsSolved", getTargetProblems(userLevel, 10), "confidence", "Medium")));

        weeks.add(new RoadmapWeek(11, "Backtracking", 
            Arrays.asList("Backtracking", "Recursion", "State Space Search"), 
            getTargetProblems(userLevel, 11),
            Arrays.asList("N-Queens", "Sudoku Solver", "Combination Sum"),
            "Learn backtracking and recursive problem solving",
            Map.of("problemsSolved", getTargetProblems(userLevel, 11), "confidence", "Low")));

        weeks.add(new RoadmapWeek(12, "Advanced Data Structures", 
            Arrays.asList("Heaps", "Tries", "Union Find"), 
            getTargetProblems(userLevel, 12),
            Arrays.asList("Top K Frequent", "Implement Trie", "Number of Islands II"),
            "Master advanced data structures",
            Map.of("problemsSolved", getTargetProblems(userLevel, 12), "confidence", "Medium")));

        weeks.add(new RoadmapWeek(13, "System Design Basics", 
            Arrays.asList("Design Patterns", "Scalability", "Trade-offs"), 
            getTargetProblems(userLevel, 13),
            Arrays.asList("LRU Cache", "LFU Cache", "Design Twitter"),
            "Introduction to system design concepts",
            Map.of("problemsSolved", getTargetProblems(userLevel, 13), "confidence", "Low")));

        return weeks;
    }

    private List<RoadmapWeek> generate180DayRoadmap(ExperienceLevel userLevel, List<String> targetCompanies) {
        List<RoadmapWeek> weeks = new ArrayList<>();

        // Weeks 1-13: Comprehensive Foundation (90-day roadmap)
        weeks.addAll(generate90DayRoadmap(userLevel, targetCompanies));

        // Weeks 14-26: Specialized Topics
        for (int week = 14; week <= 26; week++) {
            switch (week) {
                case 14:
                    weeks.add(new RoadmapWeek(week, "Advanced Graph Algorithms", 
                        Arrays.asList("Shortest Path", "Minimum Spanning Tree", "Topological Sort"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Network Delay Time", "Minimum Cost to Connect Cities", "Course Schedule II"),
                        "Advanced graph algorithms and applications",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 15:
                    weeks.add(new RoadmapWeek(week, "String Algorithms", 
                        Arrays.asList("KMP", "Rabin-Karp", "Suffix Arrays"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Implement strStr", "Repeated String Match", "Longest Palindromic Substring"),
                        "Advanced string matching and manipulation",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 16:
                    weeks.add(new RoadmapWeek(week, "Bit Manipulation", 
                        Arrays.asList("Bit Operations", "Bit Masks", "Bit Counting"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Single Number", "Counting Bits", "Power of Two"),
                        "Master bit manipulation techniques",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Medium")));
                    break;
                case 17:
                    weeks.add(new RoadmapWeek(week, "Advanced Trees", 
                        Arrays.asList("AVL Trees", "Red-Black Trees", "Segment Trees"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Range Sum Query", "Count of Smaller Numbers", "Skyline Problem"),
                        "Advanced tree data structures",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 18:
                    weeks.add(new RoadmapWeek(week, "Advanced Dynamic Programming", 
                        Arrays.asList("Digit DP", "Tree DP", "Optimization"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Numbers At Most N Given Digit Set", "Binary Tree Cameras", "Cherry Pickup"),
                        "Advanced dynamic programming techniques",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 19:
                    weeks.add(new RoadmapWeek(week, "Game Theory", 
                        Arrays.asList("Nim Game", "Grundy Numbers", "Minimax"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Nim Game", "Stone Game", "Predict the Winner"),
                        "Introduction to game theory in algorithms",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 20:
                    weeks.add(new RoadmapWeek(week, "Advanced Greedy", 
                        Arrays.asList("Interval Scheduling", "Fractional Knapsack", "Huffman Coding"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Meeting Rooms II", "Task Scheduler", "Reorganize String"),
                        "Advanced greedy algorithm applications",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Medium")));
                    break;
                case 21:
                    weeks.add(new RoadmapWeek(week, "Advanced Backtracking", 
                        Arrays.asList("Constraint Satisfaction", "Optimization", "Pruning"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Sudoku Solver", "N-Queens", "Word Search II"),
                        "Advanced backtracking with optimization",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 22:
                    weeks.add(new RoadmapWeek(week, "Advanced Data Structures", 
                        Arrays.asList("Skip Lists", "B-Trees", "Persistent Data Structures"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Design Skiplist", "Range Sum Query 2D", "Snapshot Array"),
                        "Advanced and specialized data structures",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 23:
                    weeks.add(new RoadmapWeek(week, "System Design", 
                        Arrays.asList("Distributed Systems", "Caching", "Load Balancing"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Design Twitter", "Design Instagram", "Design Uber"),
                        "System design and architecture",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Low")));
                    break;
                case 24:
                    weeks.add(new RoadmapWeek(week, "Competitive Programming", 
                        Arrays.asList("Fast I/O", "Optimization", "Advanced Algorithms"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Sliding Window Maximum", "Trapping Rain Water", "Largest Rectangle in Histogram"),
                        "Competitive programming techniques",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "Medium")));
                    break;
                case 25:
                    weeks.add(new RoadmapWeek(week, "Interview Mastery", 
                        Arrays.asList("Mock Interviews", "Problem Solving", "Communication"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Design Patterns", "Behavioral Questions", "Technical Deep Dives"),
                        "Master interview techniques and communication",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "High")));
                    break;
                case 26:
                    weeks.add(new RoadmapWeek(week, "Final Review & Practice", 
                        Arrays.asList("Review", "Practice", "Mock Interviews"), 
                        getTargetProblems(userLevel, week),
                        Arrays.asList("Comprehensive Review", "Weak Area Focus", "Company-Specific Prep"),
                        "Final preparation and review",
                        Map.of("problemsSolved", getTargetProblems(userLevel, week), "confidence", "High")));
                    break;
            }
        }

        return weeks;
    }

    private int getTargetProblems(ExperienceLevel userLevel, int week) {
        // Adjust target problems based on user level and week
        int baseProblems = 15; // Base problems per week
        
        switch (userLevel) {
            case BEGINNER:
                return Math.max(8, baseProblems - week); // Start easier, increase gradually
            case INTERMEDIATE:
                return baseProblems;
            case ADVANCED:
                return baseProblems + 5;
            case EXPERT:
                return baseProblems + 10;
            default:
                return baseProblems;
        }
    }

    private Map<String, Object> generateSummary(Timeline timeline, ExperienceLevel userLevel, List<RoadmapWeek> weeks) {
        Map<String, Object> summary = new HashMap<>();
        
        int totalProblems = weeks.stream().mapToInt(RoadmapWeek::getTargetProblems).sum();
        int totalWeeks = weeks.size();
        
        summary.put("totalProblems", totalProblems);
        summary.put("totalWeeks", totalWeeks);
        summary.put("problemsPerWeek", Math.round((double) totalProblems / totalWeeks));
        summary.put("userLevel", userLevel.getName());
        summary.put("timeline", timeline.getDescription());
        summary.put("estimatedHoursPerWeek", calculateHoursPerWeek(userLevel, timeline));
        summary.put("successProbability", calculateSuccessProbability(userLevel, timeline));
        
        return summary;
    }

    private List<String> generatePrerequisites(ExperienceLevel userLevel) {
        List<String> prerequisites = new ArrayList<>();
        
        switch (userLevel) {
            case BEGINNER:
                prerequisites.addAll(Arrays.asList(
                    "Basic programming knowledge in any language (Java, Python, C++)",
                    "Understanding of variables, loops, and functions",
                    "Basic math skills (arithmetic, algebra)",
                    "Willingness to learn and practice daily"
                ));
                break;
            case INTERMEDIATE:
                prerequisites.addAll(Arrays.asList(
                    "Solid understanding of basic data structures (arrays, strings)",
                    "Experience with basic algorithms (sorting, searching)",
                    "Comfortable with recursion and basic problem solving",
                    "At least 10-20 problems solved on platforms like LeetCode"
                ));
                break;
            case ADVANCED:
                prerequisites.addAll(Arrays.asList(
                    "Strong foundation in data structures and algorithms",
                    "Experience with medium-difficulty problems",
                    "Understanding of time and space complexity",
                    "At least 50+ problems solved with good success rate"
                ));
                break;
            case EXPERT:
                prerequisites.addAll(Arrays.asList(
                    "Expert-level problem-solving skills",
                    "Experience with hard problems and system design",
                    "Strong understanding of advanced algorithms",
                    "100+ problems solved with high success rate"
                ));
                break;
        }
        
        return prerequisites;
    }

    private List<String> generateSuccessMetrics(Timeline timeline, ExperienceLevel userLevel) {
        List<String> metrics = new ArrayList<>();
        
        int targetProblems = timeline.getDays() * 2; // Rough estimate
        
        metrics.addAll(Arrays.asList(
            "Solve " + targetProblems + " problems with 80%+ success rate",
            "Complete all weekly milestones on time",
            "Achieve confidence level 'High' in 70% of topics",
            "Successfully complete 3+ mock interviews",
            "Score 85%+ on practice assessments"
        ));
        
        if (timeline == Timeline.ONE_EIGHTY_DAYS) {
            metrics.add("Participate in 2+ competitive programming contests");
            metrics.add("Build 1-2 personal projects demonstrating algorithms");
        }
        
        return metrics;
    }

    private int calculateHoursPerWeek(ExperienceLevel userLevel, Timeline timeline) {
        int baseHours = 10; // Base hours per week
        
        switch (userLevel) {
            case BEGINNER:
                return baseHours + 5; // More time for beginners
            case INTERMEDIATE:
                return baseHours;
            case ADVANCED:
                return baseHours - 2;
            case EXPERT:
                return baseHours - 5;
            default:
                return baseHours;
        }
    }

    private double calculateSuccessProbability(ExperienceLevel userLevel, Timeline timeline) {
        double baseProbability = 0.85; // Base 85% success rate
        
        // Adjust based on user level
        switch (userLevel) {
            case BEGINNER:
                baseProbability -= 0.10;
                break;
            case INTERMEDIATE:
                baseProbability -= 0.05;
                break;
            case ADVANCED:
                baseProbability += 0.05;
                break;
            case EXPERT:
                baseProbability += 0.10;
                break;
        }
        
        // Adjust based on timeline
        switch (timeline) {
            case THIRTY_DAYS:
                baseProbability -= 0.15; // Shorter timeline = harder
                break;
            case SIXTY_DAYS:
                baseProbability -= 0.05;
                break;
            case NINETY_DAYS:
                baseProbability += 0.05;
                break;
            case ONE_EIGHTY_DAYS:
                baseProbability += 0.10; // Longer timeline = easier
                break;
        }
        
        return Math.min(0.95, Math.max(0.60, baseProbability)); // Clamp between 60% and 95%
    }
}
