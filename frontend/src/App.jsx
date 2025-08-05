import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { ThemeProvider } from "./components/ui/theme-provider"
import { Toaster } from "./components/ui/toaster"
import { AuthProvider } from "./contexts/AuthContext.jsx"
import ErrorBoundary from "./components/ErrorBoundary.jsx"
import { Login } from "./pages/Login.jsx"
import { Register } from "./pages/Register.jsx"
import { ProtectedRoute } from "./components/ProtectedRoute.jsx"
import { Layout } from "./components/Layout.jsx"
import { Dashboard } from "./pages/Dashboard.jsx"
import { Onboarding } from "./pages/Onboarding.jsx"
import { TopicTracker } from "./pages/TopicTracker.jsx"
import { TopicDetail } from "./pages/TopicDetail.jsx"
import { Profile } from "./pages/Profile.jsx"
import { DailyPlan } from "./pages/DailyPlan.jsx"
import { Analytics } from "./pages/Analytics.jsx"
import { Notes } from "./pages/Notes.jsx"
import { BlankPage } from "./pages/BlankPage.jsx"
import { TestPage } from "./components/TestPage.jsx"
import { DebugInfo } from "./components/DebugInfo.jsx"
import { SimpleTest } from "./components/SimpleTest.jsx"
import { AuthDebug } from "./components/AuthDebug.jsx"

// Temporary component to clear localStorage for testing
function ClearStorage() {
  const handleClear = () => {
    localStorage.clear();
    alert("localStorage cleared! Refresh the page.");
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Clear Storage (Debug)</h1>
      <button
        onClick={handleClear}
        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
      >
        Clear localStorage
      </button>
      <p className="mt-4 text-gray-600">
        This will clear all stored tokens and refresh the page.
      </p>
    </div>
  );
}

function App() {
  console.log("App component rendering");
  
  return (
    <ErrorBoundary>
      <AuthProvider>
        <ThemeProvider defaultTheme="light" storageKey="ui-theme">
          <Router>
            <Routes>
              <Route path="/simple" element={<SimpleTest />} />
              <Route path="/debug" element={<DebugInfo />} />
              <Route path="/auth-debug" element={<AuthDebug />} />
              <Route path="/test" element={<TestPage />} />
              <Route path="/clear-storage" element={<ClearStorage />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
                <Route index element={<Dashboard />} />
                <Route path="onboarding" element={<Onboarding />} />
                <Route path="topics" element={<TopicTracker />} />
                <Route path="topics/:topicId" element={<TopicDetail />} />
                <Route path="profile" element={<Profile />} />
                <Route path="daily-plan" element={<DailyPlan />} />
                <Route path="analytics" element={<Analytics />} />
                <Route path="notes" element={<Notes />} />
              </Route>
              <Route path="*" element={<BlankPage />} />
            </Routes>
          </Router>
          <Toaster />
        </ThemeProvider>
      </AuthProvider>
    </ErrorBoundary>
  )
}

export default App