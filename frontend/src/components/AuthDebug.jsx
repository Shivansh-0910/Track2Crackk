import { useAuth } from "@/contexts/AuthContext"
import { useEffect, useState } from "react"
import { getUserProfile } from "@/api/user"

export function AuthDebug() {
  const { isAuthenticated, login } = useAuth()
  const [profile, setProfile] = useState(null)
  const [profileError, setProfileError] = useState(null)
  const [testLoginResult, setTestLoginResult] = useState(null)
  
  const testLogin = async () => {
    try {
      await login('test@example.com', 'testpassword123')
      setTestLoginResult('✅ Test login successful')
    } catch (error) {
      setTestLoginResult(`❌ Test login failed: ${error.message}`)
    }
  }

  const fetchProfile = async () => {
    try {
      const response = await getUserProfile()
      setProfile(response.user)
      setProfileError(null)
    } catch (error) {
      setProfileError(error.message)
      setProfile(null)
    }
  }

  useEffect(() => {
    if (isAuthenticated) {
      fetchProfile()
    }
  }, [isAuthenticated])
  
  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Authentication Debug</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="space-y-4">
          <div className="p-4 border rounded">
            <h2 className="text-lg font-semibold mb-2">Authentication Status</h2>
            <div className="space-y-2">
              <p>Status: {isAuthenticated ? "✅ Authenticated" : "❌ Not Authenticated"}</p>
              <p>Access Token: {localStorage.getItem('accessToken') ? "✅ Present" : "❌ Missing"}</p>
              <p>Refresh Token: {localStorage.getItem('refreshToken') ? "✅ Present" : "❌ Missing"}</p>
            </div>
          </div>

          <div className="p-4 border rounded">
            <h2 className="text-lg font-semibold mb-2">Test Login</h2>
            <button 
              onClick={testLogin}
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              Test Login (test@example.com)
            </button>
            {testLoginResult && (
              <p className="mt-2">{testLoginResult}</p>
            )}
          </div>
        </div>

        <div className="space-y-4">
          <div className="p-4 border rounded">
            <h2 className="text-lg font-semibold mb-2">Profile Data</h2>
            <button 
              onClick={fetchProfile}
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 mb-2"
              disabled={!isAuthenticated}
            >
              Fetch Profile
            </button>
            
            {profileError && (
              <div className="text-red-600 mb-2">Error: {profileError}</div>
            )}
            
            {profile && (
              <div className="bg-gray-100 p-3 rounded text-sm">
                <pre>{JSON.stringify(profile, null, 2)}</pre>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="mt-6 p-4 border rounded">
        <h2 className="text-lg font-semibold mb-2">Local Storage Contents</h2>
        <div className="bg-gray-100 p-3 rounded text-sm">
          <pre>{JSON.stringify({
            accessToken: localStorage.getItem('accessToken')?.substring(0, 50) + '...',
            refreshToken: localStorage.getItem('refreshToken')?.substring(0, 50) + '...',
            allKeys: Object.keys(localStorage)
          }, null, 2)}</pre>
        </div>
      </div>

      <div className="mt-6 space-x-2">
        <button 
          onClick={() => window.location.href = '/login'}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Go to Login
        </button>
        <button 
          onClick={() => window.location.href = '/register'}
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
        >
          Go to Register
        </button>
        <button 
          onClick={() => window.location.href = '/profile'}
          className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
        >
          Go to Profile
        </button>
      </div>
    </div>
  )
}
