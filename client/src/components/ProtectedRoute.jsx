import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";

export function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  console.log("ProtectedRoute - isAuthenticated:", isAuthenticated);
  console.log("ProtectedRoute - current location:", location.pathname);

  if (!isAuthenticated) {
    console.log("Redirecting to login");
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  console.log("Rendering protected content");
  return <>{children}</>;
}
