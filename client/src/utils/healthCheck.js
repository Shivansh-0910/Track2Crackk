export async function checkBackendHealth() {
  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email: 'test@test.com', password: 'test' }),
    });
    
    // Even if login fails, if we get a response, the backend is running
    console.log('Backend health check response:', response.status);
    return response.status < 500; // Consider 4xx responses as "backend is running"
  } catch (error) {
    console.error('Backend health check failed:', error);
    return false;
  }
}

export async function checkMongoDBConnection() {
  try {
    // Try to access a simple endpoint that requires database connection
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email: 'test@test.com', password: 'test' }),
    });
    
    console.log('MongoDB connection check response:', response.status);
    return response.status < 500;
  } catch (error) {
    console.error('MongoDB connection check failed:', error);
    return false;
  }
} 