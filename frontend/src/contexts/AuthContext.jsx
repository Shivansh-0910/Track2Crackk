
import { createContext, useContext, useState, useEffect } from "react";
import { login as apiLogin, register as apiRegister } from "../api/auth";

const AuthContext = createContext(null);

// Helper function to check if token is valid (not expired)
function isTokenValid(token) {
  if (!token) return false;

  try {
    // Decode JWT token to check expiration
    const payload = JSON.parse(atob(token.split('.')[1]));
    const currentTime = Date.now() / 1000;

    // Check if token is expired (with 5 minute buffer)
    return payload.exp > currentTime + 300;
  } catch (error) {
    console.log("Invalid token format:", error);
    return false;
  }
}

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  // Check authentication status on mount
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    console.log("Checking initial auth state...");

    if (token && isTokenValid(token)) {
      console.log("Valid token found, user is authenticated");
      setIsAuthenticated(true);
    } else {
      console.log("No valid token found, clearing storage");
      // Clear invalid/expired tokens
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      setIsAuthenticated(false);
    }

    setIsLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await apiLogin(email, password);
      if (response?.refreshToken || response?.accessToken) {
        localStorage.setItem("refreshToken", response.refreshToken);
        localStorage.setItem("accessToken", response.accessToken);
        setIsAuthenticated(true);
        return response.user; // Return user data for onboarding check
      } else {
        throw new Error('Login failed');
      }
    } catch (error) {
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("accessToken");
      setIsAuthenticated(false);
      throw new Error(error?.message || 'Login failed');
    }
  };

  const register = async (email, password) => {
    try {
      await apiRegister(email, password);
    } catch (error) {
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("accessToken");
      setIsAuthenticated(false);
      throw new Error(error?.message || 'Registration failed');
    }
  };

  const logout = () => {
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("accessToken");
    setIsAuthenticated(false);
    window.location.reload();
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
