import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { User, Settings, Trophy, Code, Upload, Save } from "lucide-react"
import { getUserProfile, updateUserProfile } from "@/api/user"
import { useToast } from "@/hooks/useToast"

const COMPANIES = [
  'Google', 'Amazon', 'Microsoft', 'Meta', 'Apple', 'Netflix', 'Uber',
  'Airbnb', 'LinkedIn', 'Twitter', 'Spotify', 'Dropbox', 'Adobe', 'Salesforce'
]

const ACHIEVEMENTS = [
  { id: 1, name: '7-Day Streak', description: 'Solve problems for 7 consecutive days', earned: true, icon: '🔥' },
  { id: 2, name: 'Array Master', description: 'Solve 50 array problems', earned: true, icon: '📊' },
  { id: 3, name: '100 Problems', description: 'Solve 100 problems total', earned: true, icon: '💯' },
  { id: 4, name: 'Tree Climber', description: 'Solve 25 tree problems', earned: false, icon: '🌳' },
  { id: 5, name: 'Graph Explorer', description: 'Solve 20 graph problems', earned: false, icon: '🕸️' },
  { id: 6, name: '30-Day Streak', description: 'Solve problems for 30 consecutive days', earned: false, icon: '🏆' }
]

export function Profile() {
  const [profile, setProfile] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const { toast } = useToast()

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    level: '',
    targetCompanies: [],
    timeAvailability: '',
    weeklyGoal: 0,
    bio: '',
    leetcode_username: '',
    total_problems_solved: 0,
    last_submission_date: ''
  })

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getUserProfile()
        const userData = response.user
        setProfile(userData)
        setFormData({
          name: userData.name || '',
          email: userData.email || '',
          level: userData.level || '',
          targetCompanies: userData.targetCompanies || [],
          timeAvailability: userData.timeAvailability || '',
          weeklyGoal: userData.weeklyGoal || 0,
          bio: userData.bio || '',
          leetcode_username: userData.leetcode_username || '',
          total_problems_solved: userData.total_problems_solved || 0,
          last_submission_date: userData.last_submission_date
            ? new Date(userData.last_submission_date).toISOString().split('T')[0]
            : ''
        })
      } catch (error) {
        toast({
          title: "Error",
          description: error?.message || "Failed to load profile",
          variant: "destructive"
        })
      } finally {
        setLoading(false)
      }
    }

    fetchProfile()
  }, [toast])

  const handleSave = async () => {
    setSaving(true)
    try {
      const updateData = { ...formData }
      if (updateData.last_submission_date) {
        updateData.last_submission_date = new Date(updateData.last_submission_date).toISOString()
      } else {
        updateData.last_submission_date = null
      }

      const response = await updateUserProfile(updateData)
      setProfile(prev => ({ ...prev, ...response.user }))
      toast({ title: "Success", description: "Profile updated successfully" })
    } catch (error) {
      toast({
        title: "Error",
        description: error?.message || "Failed to update profile",
        variant: "destructive"
      })
    } finally {
      setSaving(false)
    }
  }

  const handleCompanyToggle = (company) => {
    setFormData(prev => ({
      ...prev,
      targetCompanies: prev.targetCompanies.includes(company)
        ? prev.targetCompanies.filter(c => c !== company)
        : [...prev.targetCompanies, company]
    }))
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-in fade-in-50 duration-500">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Profile</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Manage your account and preferences</p>
        </div>
      </div>

      {/* Welcome message for new users */}
      {(!formData.name || formData.name === formData.email?.split('@')[0]) && (
        <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-4">
          <div className="flex">
            <div className="flex-shrink-0">
              <User className="h-5 w-5 text-blue-400" />
            </div>
            <div className="ml-3">
              <h3 className="text-sm font-medium text-blue-800 dark:text-blue-200">
                Welcome! Complete your profile
              </h3>
              <div className="mt-2 text-sm text-blue-700 dark:text-blue-300">
                <p>
                  Fill out your profile information below to get personalized DSA recommendations and track your progress effectively.
                </p>
              </div>
            </div>
          </div>
        </div>
      )}



      <Tabs defaultValue="profile" className="space-y-6">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="profile"><User className="w-4 h-4 mr-2" />Profile</TabsTrigger>
          <TabsTrigger value="achievements"><Trophy className="w-4 h-4 mr-2" />Achievements</TabsTrigger>
          <TabsTrigger value="settings"><Settings className="w-4 h-4 mr-2" />Settings</TabsTrigger>
        </TabsList>

        <TabsContent value="profile" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Personal Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="name">Name</Label>
                  <Input
                    id="name"
                    placeholder="Enter your full name"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  />
                </div>
                <div>
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    disabled
                  />
                </div>
              </div>
              <div>
                <Label htmlFor="bio">Bio</Label>
                <Textarea
                  id="bio"
                  placeholder="Tell us about yourself..."
                  value={formData.bio}
                  onChange={(e) => setFormData({ ...formData, bio: e.target.value })}
                />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>DSA Progress</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="leetcode_username">LeetCode Username</Label>
                  <Input
                    id="leetcode_username"
                    placeholder="Your LeetCode username"
                    value={formData.leetcode_username}
                    onChange={(e) => setFormData({ ...formData, leetcode_username: e.target.value })}
                  />
                </div>
                <div>
                  <Label htmlFor="total_problems_solved">Problems Solved</Label>
                  <Input
                    id="total_problems_solved"
                    type="number"
                    value={formData.total_problems_solved}
                    onChange={(e) => setFormData({ ...formData, total_problems_solved: Number(e.target.value) })}
                  />
                </div>
              </div>
              <div>
                <Label htmlFor="last_submission_date">Last Submission Date</Label>
                <Input
                  id="last_submission_date"
                  type="date"
                  value={formData.last_submission_date}
                  onChange={(e) => setFormData({ ...formData, last_submission_date: e.target.value })}
                />
              </div>
            </CardContent>
          </Card>

          <div className="flex justify-end">
            <Button onClick={handleSave} disabled={saving}>
              <Save className="w-4 h-4 mr-2" />
              {saving ? 'Saving...' : 'Save Changes'}
            </Button>
          </div>
        </TabsContent>

        <TabsContent value="achievements">
          <Card>
            <CardHeader>
              <CardTitle>Achievements</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {ACHIEVEMENTS.map(achievement => (
                  <div
                    key={achievement.id}
                    className={`p-4 rounded-lg border ${
                      achievement.earned ? 'bg-yellow-100' : 'opacity-50 bg-gray-100'
                    }`}
                  >
                    <div className="text-center">
                      <div className="text-2xl">{achievement.icon}</div>
                      <h3 className="font-bold">{achievement.name}</h3>
                      <p className="text-sm">{achievement.description}</p>
                      {achievement.earned && <Badge variant="secondary">Earned</Badge>}
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="settings">
          <Card>
            <CardHeader>
              <CardTitle>Settings</CardTitle>
            </CardHeader>
            <CardContent>
              <p>Coming soon: Notifications, Privacy settings etc.</p>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
