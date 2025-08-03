import React from 'react';

export function SimpleTest() {
  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f0f0f0',
      padding: '20px',
      fontFamily: 'Arial, sans-serif'
    }}>
      <h1 style={{ color: '#333', textAlign: 'center' }}>React is Working!</h1>
      <div style={{
        maxWidth: '600px',
        margin: '0 auto',
        backgroundColor: 'white',
        padding: '20px',
        borderRadius: '8px',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
      }}>
        <h2>Test Information</h2>
        <ul>
          <li>✅ React is rendering</li>
          <li>✅ Components are working</li>
          <li>✅ Routing should work</li>
          <li>✅ CSS is loading</li>
        </ul>
        
        <div style={{ marginTop: '20px' }}>
          <button 
            onClick={() => alert('Button click works!')}
            style={{
              backgroundColor: '#007bff',
              color: 'white',
              border: 'none',
              padding: '10px 20px',
              borderRadius: '4px',
              cursor: 'pointer',
              marginRight: '10px'
            }}
          >
            Test Button
          </button>
          
          <button 
            onClick={() => window.location.href = '/debug'}
            style={{
              backgroundColor: '#28a745',
              color: 'white',
              border: 'none',
              padding: '10px 20px',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            Go to Debug Page
          </button>
        </div>
        
        <div style={{ marginTop: '20px', padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '4px' }}>
          <strong>Current URL:</strong> {window.location.href}
        </div>
      </div>
    </div>
  );
} 