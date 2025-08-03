import api from './api';

// Description: Get all DSA topics with progress
// Endpoint: GET /api/topics
// Request: {}
// Response: { topics: Array<{ _id: string, name: string, icon: string, totalProblems: number, solvedProblems: number, accuracy: number, lastActivity: string, difficulty: { easy: number, medium: number, hard: number } }> }
export const getTopics = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        topics: [
          {
            _id: '507f1f77bcf86cd799439012',
            name: 'Arrays',
            icon: '📊',
            totalProblems: 150,
            solvedProblems: 45,
            accuracy: 78,
            lastActivity: '2024-01-15',
            difficulty: { easy: 15, medium: 25, hard: 5 }
          },
          {
            _id: '507f1f77bcf86cd799439013',
            name: 'Strings',
            icon: '🔤',
            totalProblems: 120,
            solvedProblems: 32,
            accuracy: 85,
            lastActivity: '2024-01-14',
            difficulty: { easy: 12, medium: 18, hard: 2 }
          },
          {
            _id: '507f1f77bcf86cd799439014',
            name: 'Linked Lists',
            icon: '🔗',
            totalProblems: 80,
            solvedProblems: 28,
            accuracy: 72,
            lastActivity: '2024-01-13',
            difficulty: { easy: 8, medium: 15, hard: 5 }
          },
          {
            _id: '507f1f77bcf86cd799439015',
            name: 'Trees',
            icon: '🌳',
            totalProblems: 100,
            solvedProblems: 18,
            accuracy: 65,
            lastActivity: '2024-01-12',
            difficulty: { easy: 5, medium: 10, hard: 3 }
          },
          {
            _id: '507f1f77bcf86cd799439016',
            name: 'Graphs',
            icon: '🗸',
            totalProblems: 90,
            solvedProblems: 12,
            accuracy: 58,
            lastActivity: '2024-01-10',
            difficulty: { easy: 3, medium: 7, hard: 2 }
          },
          {
            _id: '507f1f77bcf86cd799439017',
            name: 'Dynamic Programming',
            icon: '🧮',
            totalProblems: 110,
            solvedProblems: 8,
            accuracy: 50,
            lastActivity: '2024-01-08',
            difficulty: { easy: 2, medium: 4, hard: 2 }
          }
        ]
      });
    }, 500);
  });
  // try {
  //   return await api.get('/api/topics');
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Get topic details with problems
// Endpoint: GET /api/topics/:topicId
// Request: {}
// Response: { topic: { _id: string, name: string, problems: Array<{ _id: string, name: string, difficulty: string, status: string, companies: string[], url: string, frequency: number }> } }
export const getTopicDetails = (topicId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        topic: {
          _id: topicId,
          name: 'Arrays',
          problems: [
            {
              _id: '507f1f77bcf86cd799439018',
              name: 'Two Sum',
              difficulty: 'Easy',
              status: 'Solved',
              companies: ['Google', 'Amazon', 'Microsoft'],
              url: 'https://leetcode.com/problems/two-sum/',
              frequency: 95
            },
            {
              _id: '507f1f77bcf86cd799439019',
              name: 'Best Time to Buy and Sell Stock',
              difficulty: 'Easy',
              status: 'Solved',
              companies: ['Amazon', 'Microsoft'],
              url: 'https://leetcode.com/problems/best-time-to-buy-and-sell-stock/',
              frequency: 88
            },
            {
              _id: '507f1f77bcf86cd799439020',
              name: 'Contains Duplicate',
              difficulty: 'Easy',
              status: 'Not Started',
              companies: ['Google', 'Apple'],
              url: 'https://leetcode.com/problems/contains-duplicate/',
              frequency: 82
            }
          ]
        }
      });
    }, 500);
  });
  // try {
  //   return await api.get(`/api/topics/${topicId}`);
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};