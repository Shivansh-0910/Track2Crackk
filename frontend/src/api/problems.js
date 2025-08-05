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
export const updateProblemStatus = async (problemId, status) => {
  try {
    console.log('🌐 Updating problem status:', { problemId, status });

    const response = await api.put(`/api/problems/${problemId}/status`, { status });
    console.log('✅ Status updated successfully');
    return response;
  } catch (error) {
    console.error('❌ Failed to update status:', error.response?.data || error.message);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Get daily recommended problems
// Endpoint: GET /api/problems/daily-recommendations
// Request: {}
// Response: { problems: Array<{ _id: string, name: string, difficulty: string, topic: string, estimatedTime: number, url: string }> }
export const getDailyRecommendations = async (config = {}) => {
  try {
    const response = await api.post('/api/problems/daily-recommendations', config);
    return response.data;
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};