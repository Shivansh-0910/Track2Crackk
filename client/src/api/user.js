import api from './api';

// Description: Get user profile data
// Endpoint: GET /api/users/me
// Request: {}
// Response: { success: boolean, user: { _id: string, name: string, email: string, avatar?: string, level: string, targetCompanies: string[], timeAvailability: string, totalProblems: number, currentStreak: number, weeklyGoal: number, completedOnboarding: boolean, leetcode_username?: string, total_problems_solved: number, last_submission_date?: string } }
export const getUserProfile = async () => {
  try {
    const response = await api.get('/api/user/profile');
    // Backend returns user directly, so wrap it to match expected structure
    return { user: response.data };
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Update user profile
// Endpoint: PUT /api/users/me
// Request: { name?: string, avatar?: string, level?: string, targetCompanies?: string[], timeAvailability?: string, weeklyGoal?: number, leetcode_username?: string, total_problems_solved?: number, last_submission_date?: string }
// Response: { success: boolean, message: string, user: object }
export const updateUserProfile = async (data) => {
  try {
    const response = await api.put('/api/user/profile', data);
    return { user: response.data };
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Complete onboarding questionnaire
// Endpoint: POST /api/user/onboarding
// Request: { level: string, confidentTopics: string[], strugglingTopics: string[], targetCompanies: string[], totalProblems: number, platform: string, timeAvailability: string }
// Response: { success: boolean, message: string }
export const completeOnboarding = async (data) => {
  try {
    const response = await api.post('/api/user/onboarding', data);
    return response.data;
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

