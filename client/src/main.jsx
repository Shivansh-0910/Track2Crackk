
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

console.log("main.jsx loading...");

const rootElement = document.getElementById('root');

if (!rootElement) {
  console.error("Root element not found!");
  document.body.innerHTML = '<h1>Error: Root element not found</h1>';
} else {
  console.log("Root element found, rendering app...");
  
  try {
    createRoot(rootElement).render(
      <StrictMode>
        <App />
      </StrictMode>,
    )
    console.log("App rendered successfully");
  } catch (error) {
    console.error("Error rendering app:", error);
    rootElement.innerHTML = `
      <div style="padding: 20px; font-family: Arial, sans-serif;">
        <h1>Error Loading App</h1>
        <p>There was an error loading the application. Please check the console for details.</p>
        <pre style="background: #f0f0f0; padding: 10px; border-radius: 4px;">${error.message}</pre>
      </div>
    `;
  }
}

