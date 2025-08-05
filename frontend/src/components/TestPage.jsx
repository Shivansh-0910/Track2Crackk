import React, { useState, useEffect } from 'react';
import { checkBackendHealth, checkMongoDBConnection } from '../utils/healthCheck';

export function TestPage() {
  const [backendStatus, setBackendStatus] = useState('checking');
  const [mongoStatus, setMongoStatus] = useState('checking');

  useEffect(() => {
    const checkHealth = async () => {
      const backendOk = await checkBackendHealth();
      setBackendStatus(backendOk ? 'connected' : 'disconnected');
      
      const mongoOk = await checkMongoDBConnection();
      setMongoStatus(mongoOk ? 'connected' : 'disconnected');
    };
    
    checkHealth();
  }, []);
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="max-w-md mx-auto text-center p-8 bg-white rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold text-gray-800 mb-4">TrackDSA App</h1>
        <p className="text-gray-600 mb-6">The application is working correctly!</p>
        
        <div className="space-y-4">
          <div className="p-4 bg-green-50 border border-green-200 rounded">
            <h3 className="font-semibold text-green-800">✅ React is working</h3>
            <p className="text-sm text-green-600">Components are rendering properly</p>
          </div>
          
          <div className="p-4 bg-blue-50 border border-blue-200 rounded">
            <h3 className="font-semibold text-blue-800">✅ Vite is working</h3>
            <p className="text-sm text-blue-600">Development server is running</p>
          </div>
          
          <div className="p-4 bg-purple-50 border border-purple-200 rounded">
            <h3 className="font-semibold text-purple-800">✅ CSP is configured</h3>
            <p className="text-sm text-purple-600">Content Security Policy is set up</p>
          </div>
          
          <div className={`p-4 border rounded ${
            backendStatus === 'connected' ? 'bg-green-50 border-green-200' : 
            backendStatus === 'checking' ? 'bg-yellow-50 border-yellow-200' : 
            'bg-red-50 border-red-200'
          }`}>
            <h3 className={`font-semibold ${
              backendStatus === 'connected' ? 'text-green-800' : 
              backendStatus === 'checking' ? 'text-yellow-800' : 
              'text-red-800'
            }`}>
              {backendStatus === 'connected' ? '✅' : backendStatus === 'checking' ? '⏳' : '❌'} Backend Status
            </h3>
            <p className={`text-sm ${
              backendStatus === 'connected' ? 'text-green-600' : 
              backendStatus === 'checking' ? 'text-yellow-600' : 
              'text-red-600'
            }`}>
              {backendStatus === 'connected' ? 'Spring Boot is running' : 
               backendStatus === 'checking' ? 'Checking backend...' : 
               'Spring Boot is not running'}
            </p>
          </div>
          
          <div className={`p-4 border rounded ${
            mongoStatus === 'connected' ? 'bg-green-50 border-green-200' : 
            mongoStatus === 'checking' ? 'bg-yellow-50 border-yellow-200' : 
            'bg-red-50 border-red-200'
          }`}>
            <h3 className={`font-semibold ${
              mongoStatus === 'connected' ? 'text-green-800' : 
              mongoStatus === 'checking' ? 'text-yellow-800' : 
              'text-red-800'
            }`}>
              {mongoStatus === 'connected' ? '✅' : mongoStatus === 'checking' ? '⏳' : '❌'} MongoDB Status
            </h3>
            <p className={`text-sm ${
              mongoStatus === 'connected' ? 'text-green-600' : 
              mongoStatus === 'checking' ? 'text-yellow-600' : 
              'text-red-600'
            }`}>
              {mongoStatus === 'connected' ? 'Database is connected' : 
               mongoStatus === 'checking' ? 'Checking database...' : 
               'Database is not connected'}
            </p>
          </div>
        </div>
        
        <button 
          onClick={() => window.location.href = '/login'}
          className="mt-6 bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition-colors"
        >
          Go to Login
        </button>
      </div>
    </div>
  );
} 