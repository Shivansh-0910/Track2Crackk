import api from './api';

export const roadmapAPI = {
  // Test backend connection
  testConnection: async () => {
    try {
      const response = await api.get('/roadmap/test');
      return response.data;
    } catch (error) {
      console.error('Error testing backend connection:', error);
      throw error;
    }
  },

  // Get available timeline options
  getTimelines: async () => {
    try {
      const response = await api.get('/roadmap/timelines');
      return response.data;
    } catch (error) {
      console.error('Error fetching timelines:', error);
      throw error;
    }
  },

  // Generate roadmap for specific timeline
  generateRoadmap: async (timeline) => {
    try {
      const response = await api.get(`/roadmap/generate/${timeline}`);
      return response.data;
    } catch (error) {
      console.error('Error generating roadmap:', error);
      throw error;
    }
  },

  // Get roadmap progress
  getProgress: async () => {
    try {
      const response = await api.get('/roadmap/progress');
      return response.data;
    } catch (error) {
      console.error('Error fetching roadmap progress:', error);
      throw error;
    }
  },

  // Get roadmap recommendations
  getRecommendations: async () => {
    try {
      const response = await api.get('/roadmap/recommendations');
      return response.data;
    } catch (error) {
      console.error('Error fetching roadmap recommendations:', error);
      throw error;
    }
  },

  // Update roadmap progress
  updateProgress: async (weekNumber, problemsCompleted) => {
    try {
      const response = await api.post('/roadmap/progress/update', null, {
        params: { weekNumber, problemsCompleted }
      });
      return response.data;
    } catch (error) {
      console.error('Error updating roadmap progress:', error);
      throw error;
    }
  },

  // Get detailed week breakdown
  getWeekDetails: async (weekNumber, timeline) => {
    try {
      const response = await api.get(`/roadmap/week/${weekNumber}`, {
        params: { timeline }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching week details:', error);
      throw error;
    }
  }
};
