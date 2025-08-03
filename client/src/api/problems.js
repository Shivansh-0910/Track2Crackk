import api from './api';

// Description: Add a solved problem
// Endpoint: POST /api/problems
// Request: { name: string, url: string, platform: string, difficulty: string, topics: string[], companies: string[], notes?: string, timeTaken?: number }
// Response: { success: boolean, message: string, problem: { _id: string } }
export const addProblem = (data) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        success: true,
        message: 'Problem added successfully',
        problem: { _id: '507f1f77bcf86cd799439021' }
      });
    }, 500);
  });
  // try {
  //   return await api.post('/api/problems', data);
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Update problem status
// Endpoint: PUT /api/problems/:problemId/status
// Request: { status: string }
// Response: { success: boolean, message: string }
export const updateProblemStatus = (problemId, status) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true, message: 'Problem status updated' });
    }, 300);
  });
  // try {
  //   return await api.put(`/api/problems/${problemId}/status`, { status });
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Get daily recommended problems
// Endpoint: GET /api/problems/daily-recommendations
// Request: {}
// Response: { problems: Array<{ _id: string, name: string, difficulty: string, topic: string, estimatedTime: number, url: string }> }
export const getDailyRecommendations = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        problems: [
          {
            _id: '507f1f77bcf86cd799439022',
            name: 'Valid Parentheses',
            difficulty: 'Easy',
            topic: 'Stack',
            estimatedTime: 20,
            url: 'https://leetcode.com/problems/valid-parentheses/'
          },
          {
            _id: '507f1f77bcf86cd799439023',
            name: 'Merge Two Sorted Lists',
            difficulty: 'Easy',
            topic: 'Linked Lists',
            estimatedTime: 25,
            url: 'https://leetcode.com/problems/merge-two-sorted-lists/'
          },
          {
            _id: '507f1f77bcf86cd799439024',
            name: 'Binary Tree Inorder Traversal',
            difficulty: 'Medium',
            topic: 'Trees',
            estimatedTime: 30,
            url: 'https://leetcode.com/problems/binary-tree-inorder-traversal/'
          }
        ]
      });
    }, 500);
  });
  // try {
  //   return await api.get('/api/problems/daily-recommendations');
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};