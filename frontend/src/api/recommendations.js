import api from './api';

// Get personalized problem recommendations
export const getPersonalizedRecommendations = async (limit = 10, type = 'daily') => {
  try {
    const response = await api.get(`/api/recommendations/personalized`, {
      params: { limit, type }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching personalized recommendations:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Get topic-specific recommendations
export const getTopicRecommendations = async (topicName, limit = 5) => {
  try {
    const response = await api.get(`/api/recommendations/topic/${topicName}`, {
      params: { limit }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching topic recommendations:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Get company-specific recommendations
export const getCompanyRecommendations = async (companyName, limit = 10) => {
  try {
    const response = await api.get(`/api/recommendations/company/${companyName}`, {
      params: { limit }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching company recommendations:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Submit feedback on recommendations
export const submitRecommendationFeedback = async (feedbackData) => {
  try {
    const response = await api.post('/api/recommendations/feedback', feedbackData);
    return response.data;
  } catch (error) {
    console.error('Error submitting recommendation feedback:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Get recommendation insights and analytics
export const getRecommendationInsights = async () => {
  try {
    const response = await api.get('/api/recommendations/insights');
    return response.data;
  } catch (error) {
    console.error('Error fetching recommendation insights:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Helper function to format recommendation priority
export const getPriorityColor = (priority) => {
  switch (priority) {
    case 'HIGH': return 'text-red-600 bg-red-100';
    case 'MEDIUM': return 'text-yellow-600 bg-yellow-100';
    case 'LOW': return 'text-green-600 bg-green-100';
    default: return 'text-gray-600 bg-gray-100';
  }
};

// Helper function to format recommendation score
export const formatScore = (score) => {
  return Math.round(score * 100);
};
