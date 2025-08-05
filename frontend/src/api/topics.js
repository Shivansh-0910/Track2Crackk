import api from './api';

// Description: Get all DSA topics with progress
// Endpoint: GET /api/topics
// Request: {}
// Response: { topics: Array<{ _id: string, name: string, icon: string, totalProblems: number, solvedProblems: number, accuracy: number, lastActivity: string, difficulty: { easy: number, medium: number, hard: number } }> }
export const getTopics = async () => {
  try {
    const response = await api.get('/api/topics');
    return response.data;
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Get topic details with problems
// Endpoint: GET /api/topics/:topicId
// Request: {}
// Response: { topic: { _id: string, name: string, problems: Array<{ _id: string, name: string, difficulty: string, status: string, companies: string[], url: string, frequency: number }> } }
export const getTopicDetails = async (topicId) => {
  try {
    const response = await api.get(`/api/topics/${topicId}`);
    return { topic: response.data };
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};