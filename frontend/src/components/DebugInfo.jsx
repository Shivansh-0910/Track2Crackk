import React, { useEffect, useRef, useState } from 'react';

export function DebugInfo() {
  const rootRef = useRef(null);
  const [dimensions, setDimensions] = useState({});

  useEffect(() => {
    const updateDimensions = () => {
      if (rootRef.current) {
        const rect = rootRef.current.getBoundingClientRect();
        const computedStyle = window.getComputedStyle(rootRef.current);
        
        setDimensions({
          width: rect.width,
          height: rect.height,
          display: computedStyle.display,
          visibility: computedStyle.visibility,
          opacity: computedStyle.opacity,
          position: computedStyle.position,
          top: computedStyle.top,
          left: computedStyle.left,
          zIndex: computedStyle.zIndex,
          overflow: computedStyle.overflow,
          minHeight: computedStyle.minHeight,
          maxHeight: computedStyle.maxHeight,
        });
      }
    };

    updateDimensions();
    window.addEventListener('resize', updateDimensions);
    
    // Update after a short delay to catch any dynamic changes
    const timer = setTimeout(updateDimensions, 100);
    
    return () => {
      window.removeEventListener('resize', updateDimensions);
      clearTimeout(timer);
    };
  }, []);

  return (
    <div 
      ref={rootRef}
      className="min-h-screen bg-red-100 p-4"
      style={{ minHeight: '100vh', border: '2px solid red' }}
    >
      <div className="bg-white p-4 rounded shadow-lg max-w-2xl mx-auto">
        <h2 className="text-xl font-bold mb-4">Debug Information</h2>
        
        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <h3 className="font-semibold">Element Dimensions:</h3>
            <ul className="text-sm space-y-1">
              <li>Width: {dimensions.width}px</li>
              <li>Height: {dimensions.height}px</li>
              <li>Display: {dimensions.display}</li>
              <li>Visibility: {dimensions.visibility}</li>
              <li>Opacity: {dimensions.opacity}</li>
            </ul>
          </div>
          
          <div>
            <h3 className="font-semibold">Position & Layout:</h3>
            <ul className="text-sm space-y-1">
              <li>Position: {dimensions.position}</li>
              <li>Top: {dimensions.top}</li>
              <li>Left: {dimensions.left}</li>
              <li>Z-Index: {dimensions.zIndex}</li>
              <li>Overflow: {dimensions.overflow}</li>
            </ul>
          </div>
        </div>
        
        <div className="mb-4">
          <h3 className="font-semibold">Height Constraints:</h3>
          <ul className="text-sm space-y-1">
            <li>Min Height: {dimensions.minHeight}</li>
            <li>Max Height: {dimensions.maxHeight}</li>
          </ul>
        </div>
        
        <div className="mb-4">
          <h3 className="font-semibold">Browser Info:</h3>
          <ul className="text-sm space-y-1">
            <li>Viewport Width: {window.innerWidth}px</li>
            <li>Viewport Height: {window.innerHeight}px</li>
            <li>Document Height: {document.documentElement.scrollHeight}px</li>
            <li>Body Height: {document.body.scrollHeight}px</li>
          </ul>
        </div>
        
        <div className="space-x-2">
          <button 
            onClick={() => window.location.href = '/test'}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Go to Test Page
          </button>
          <button 
            onClick={() => window.location.href = '/login'}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Go to Login
          </button>
          <button 
            onClick={() => window.location.reload()}
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            Reload Page
          </button>
        </div>
      </div>
    </div>
  );
} 