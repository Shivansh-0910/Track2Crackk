import api from './api';

// Description: Login user functionality
// Endpoint: POST /api/auth/login
// Request: { email: string, password: string }
// Response: { accessToken: string, refreshToken: string, user: object }
export const login = async (email, password) => {
  try {
    console.log("Attempting login with:", { email });
    const response = await api.post('/api/auth/login', { email, password });
    console.log("Login response:", response.data);
    
    // Store tokens in localStorage
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
    }
    if (response.data.refreshToken) {
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    
    return response.data;
  } catch (error) {
    console.error('Login error:', error);
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Register user functionality
// Endpoint: POST /api/auth/register
// Request: { email: string, password: string }
// Response: { email: string }
export const register = async (email, password) => {
  try {
    const response = await api.post('/api/auth/register', {email, password});
    return response.data;
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Refresh token functionality
// Endpoint: POST /api/auth/refresh
// Request: { refreshToken: string }
// Response: { accessToken: string, refreshToken: string }
export const refreshToken = async (refreshToken) => {
  try {
    const response = await api.post('/api/auth/refresh', { refreshToken });
    
    // Update stored tokens
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
    }
    if (response.data.refreshToken) {
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    
    return response.data;
  } catch (error) {
    throw new Error(error?.response?.data?.message || error.message);
  }
};

// Description: Logout
// Endpoint: POST /api/auth/logout
// Request: {}
// Response: { success: boolean, message: string }
export const logout = async () => {
  try {
    const response = await api.post('/api/auth/logout');
    
    // Clear stored tokens
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    
    return response.data;
  } catch (error) {
    // Even if the API call fails, clear local tokens
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    throw new Error(error?.response?.data?.message || error.message);
  }
};
