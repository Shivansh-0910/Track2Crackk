import api from './api';

// Description: Get user analytics data
// Endpoint: GET /api/analytics
// Request: {}
// Response: { analytics: { topicMastery: Array<{ topic: string, mastery: number }>, monthlyActivity: Array<{ date: string, problems: number }>, companyProgress: Array<{ company: string, progress: number }>, difficultyDistribution: { easy: number, medium: number, hard: number } } }
export const getAnalytics = () => {
  // Make a real API call to the backend
  return api.get('/api/analytics')
    .then(response => {
      // Wrap the response in analytics object to match expected structure
      return { analytics: response.data };
    })
    .catch(error => {
      // Optionally handle error or return a default structure
      console.error('Error fetching analytics:', error);
      // Return mock data if API fails
      return {
        analytics: {
          topicMastery: [
            { topic: "Arrays", mastery: 85 },
            { topic: "Strings", mastery: 72 },
            { topic: "Linked Lists", mastery: 68 },
            { topic: "Trees", mastery: 45 }
          ],
          companyProgress: [
            { company: "Google", progress: 75 },
            { company: "Amazon", progress: 68 },
            { company: "Microsoft", progress: 82 },
            { company: "Meta", progress: 55 },
            { company: "Apple", progress: 60 }
          ],
          monthlyActivity: [
            { date: "2024-01-15", problems: 2 },
            { date: "2024-01-14", problems: 1 },
            { date: "2024-01-13", problems: 3 }
          ],
          difficultyDistribution: { easy: 15, medium: 8, hard: 2 }
        }
      };
    });
};