import { useEffect, useState } from "react"
import { useAuth } from "@/contexts/AuthContext"
import { getUserProfile } from "@/api/user"

export function ProfileDebug() {
  const { isAuthenticated } = useAuth()
  const [profile, setProfile] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchProfile = async () => {
      if (!isAuthenticated) {
        console.log("ProfileDebug: Not authenticated")
        return
      }

      setLoading(true)
      setError(null)
      
      try {
        console.log("ProfileDebug: Fetching profile...")
        const response = await getUserProfile()
        console.log("ProfileDebug: Profile response:", response)
        setProfile(response.user)
      } catch (err) {
        console.error("ProfileDebug: Error fetching profile:", err)
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchProfile()
  }, [isAuthenticated])

  return (
    <div className="p-4 border rounded-lg bg-gray-50 dark:bg-gray-800">
      <h3 className="text-lg font-bold mb-4">Profile Debug Info</h3>
      
      <div className="space-y-2">
        <div>
          <strong>Authentication Status:</strong> {isAuthenticated ? "✅ Authenticated" : "❌ Not Authenticated"}
        </div>
        
        <div>
          <strong>Access Token:</strong> {localStorage.getItem('accessToken') ? "✅ Present" : "❌ Missing"}
        </div>
        
        <div>
          <strong>Refresh Token:</strong> {localStorage.getItem('refreshToken') ? "✅ Present" : "❌ Missing"}
        </div>
        
        <div>
          <strong>Loading:</strong> {loading ? "⏳ Loading..." : "✅ Not Loading"}
        </div>
        
        {error && (
          <div>
            <strong>Error:</strong> <span className="text-red-600">{error}</span>
          </div>
        )}
        
        {profile && (
          <div>
            <strong>Profile Data:</strong>
            <pre className="mt-2 p-2 bg-gray-100 dark:bg-gray-700 rounded text-sm overflow-auto">
              {JSON.stringify(profile, null, 2)}
            </pre>
          </div>
        )}
      </div>
    </div>
  )
}
