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
import { User, Settings, Trophy, Code, Upload, Save, Award, Target, Calendar, Star } from "lucide-react"
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
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Loading your profile...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-8 animate-in fade-in-50 duration-500">
      {/* Header Section */}
      <div className="card-premium rounded-3xl p-8 text-white shadow-glow animate-fade-in-scale">
        <div className="absolute inset-0 rounded-3xl bg-gradient-to-br from-primary via-accent to-primary/80 opacity-90"></div>
        <div className="relative z-10 flex items-center justify-between">
          <div className="flex items-center gap-6">
            <div className="p-4 bg-white/20 rounded-full backdrop-blur-sm">
              <Avatar className="w-16 h-16 border-4 border-white/30">
                <AvatarImage src={profile?.avatar} />
                <AvatarFallback className="text-2xl font-bold bg-white/20 text-white">
                  {profile?.name?.charAt(0) || 'U'}
                </AvatarFallback>
              </Avatar>
            </div>
            <div>
              <h1 className="text-4xl font-bold mb-2 bg-gradient-to-r from-white to-white/90 bg-clip-text text-transparent">
                {profile?.name || 'User Profile'}
              </h1>
              <p className="text-lg text-white/90 font-medium">{profile?.email}</p>
              <div className="flex items-center gap-2 mt-2">
                <Badge className="bg-white/20 text-white border-white/30">
                  {profile?.level || 'Beginner'}
                </Badge>
                <Badge className="bg-white/20 text-white border-white/30">
                  {profile?.total_problems_solved || 0} problems solved
                </Badge>
              </div>
            </div>
          </div>
          <div className="text-right">
            <div className="text-2xl font-bold text-white">{profile?.currentStreak || 0}</div>
            <p className="text-sm text-white/80">day streak</p>
          </div>
        </div>
      </div>

      <Tabs defaultValue="profile" className="space-y-6">
        <TabsList className="grid w-full grid-cols-3 card-premium p-1">
          <TabsTrigger value="profile" className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground">
            <User className="w-4 h-4 mr-2" />
            Profile
          </TabsTrigger>
          <TabsTrigger value="achievements" className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground">
            <Trophy className="w-4 h-4 mr-2" />
            Achievements
          </TabsTrigger>
          <TabsTrigger value="stats" className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground">
            <Code className="w-4 h-4 mr-2" />
            Statistics
          </TabsTrigger>
        </TabsList>

        <TabsContent value="profile" className="space-y-6">
          <Card className="card-premium animate-fade-in-scale">
            <CardHeader>
              <CardTitle className="flex items-center gap-3">
                <div className="p-2 bg-primary/10 rounded-lg">
                  <Settings className="w-5 h-5 text-primary" />
                </div>
                <span className="text-gradient font-semibold">Personal Information</span>
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <Label htmlFor="name" className="text-sm font-medium text-foreground">Full Name</Label>
                  <Input
                    id="name"
                    value={formData.name}
                    onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                    className="input-premium"
                    placeholder="Enter your full name"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="email" className="text-sm font-medium text-foreground">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
                    className="input-premium"
                    placeholder="Enter your email"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="level" className="text-sm font-medium text-foreground">Experience Level</Label>
                  <Select value={formData.level} onValueChange={(value) => setFormData(prev => ({ ...prev, level: value }))}>
                    <SelectTrigger className="input-premium">
                      <SelectValue placeholder="Select your level" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="beginner">Beginner</SelectItem>
                      <SelectItem value="intermediate">Intermediate</SelectItem>
                      <SelectItem value="advanced">Advanced</SelectItem>
                      <SelectItem value="expert">Expert</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="leetcode" className="text-sm font-medium text-foreground">LeetCode Username</Label>
                  <Input
                    id="leetcode"
                    value={formData.leetcode_username}
                    onChange={(e) => setFormData(prev => ({ ...prev, leetcode_username: e.target.value }))}
                    className="input-premium"
                    placeholder="Your LeetCode username"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="timeAvailability" className="text-sm font-medium text-foreground">Daily Time Availability</Label>
                  <Select value={formData.timeAvailability} onValueChange={(value) => setFormData(prev => ({ ...prev, timeAvailability: value }))}>
                    <SelectTrigger className="input-premium">
                      <SelectValue placeholder="Select time availability" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="30min">30 minutes</SelectItem>
                      <SelectItem value="1hour">1 hour</SelectItem>
                      <SelectItem value="2hours">2 hours</SelectItem>
                      <SelectItem value="3hours">3+ hours</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="weeklyGoal" className="text-sm font-medium text-foreground">Weekly Goal (problems)</Label>
                  <Input
                    id="weeklyGoal"
                    type="number"
                    value={formData.weeklyGoal}
                    onChange={(e) => setFormData(prev => ({ ...prev, weeklyGoal: parseInt(e.target.value) || 0 }))}
                    className="input-premium"
                    placeholder="Number of problems per week"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="bio" className="text-sm font-medium text-foreground">Bio</Label>
                <Textarea
                  id="bio"
                  value={formData.bio}
                  onChange={(e) => setFormData(prev => ({ ...prev, bio: e.target.value }))}
                  className="input-premium min-h-[100px]"
                  placeholder="Tell us about yourself and your goals..."
                />
              </div>

              <div className="space-y-3">
                <Label className="text-sm font-medium text-foreground">Target Companies</Label>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                  {COMPANIES.map((company) => (
                    <div key={company} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={company}
                        checked={formData.targetCompanies.includes(company)}
                        onChange={() => handleCompanyToggle(company)}
                        className="rounded border-border"
                      />
                      <Label htmlFor={company} className="text-sm text-foreground cursor-pointer">
                        {company}
                      </Label>
                    </div>
                  ))}
                </div>
              </div>

              <Button 
                onClick={handleSave} 
                disabled={saving}
                className="w-full btn-premium text-white font-semibold"
              >
                {saving ? (
                  <>
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                    Saving...
                  </>
                ) : (
                  <>
                    <Save className="w-4 h-4 mr-2" />
                    Save Changes
                  </>
                )}
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="achievements" className="space-y-6">
          <Card className="card-premium animate-fade-in-scale">
            <CardHeader>
              <CardTitle className="flex items-center gap-3">
                <div className="p-2 bg-warning/10 rounded-lg">
                  <Award className="w-5 h-5 text-warning" />
                </div>
                <span className="text-gradient font-semibold">Your Achievements</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {ACHIEVEMENTS.map((achievement) => (
                  <Card 
                    key={achievement.id} 
                    className={`card-premium transition-all duration-300 hover:scale-105 ${
                      achievement.earned ? 'ring-2 ring-success/50' : 'opacity-60'
                    }`}
                  >
                    <CardContent className="p-6 text-center">
                      <div className="text-4xl mb-3">{achievement.icon}</div>
                      <h3 className="font-semibold text-foreground mb-2">{achievement.name}</h3>
                      <p className="text-sm text-muted-foreground mb-3">{achievement.description}</p>
                      <Badge 
                        variant={achievement.earned ? 'default' : 'secondary'}
                        className={achievement.earned ? 'bg-success text-success-foreground' : ''}
                      >
                        {achievement.earned ? 'Earned' : 'Locked'}
                      </Badge>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="stats" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up">
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">Total Problems</CardTitle>
                <div className="p-2 bg-primary/10 rounded-lg">
                  <Target className="h-4 w-4 text-primary" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-3xl font-bold text-foreground">{profile?.total_problems_solved || 0}</div>
                <p className="text-xs text-muted-foreground mt-2">problems solved</p>
              </CardContent>
            </Card>

            <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.1s'}}>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">Current Streak</CardTitle>
                <div className="p-2 bg-warning/10 rounded-lg">
                  <Star className="h-4 w-4 text-warning" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-3xl font-bold text-foreground">{profile?.currentStreak || 0}</div>
                <p className="text-xs text-muted-foreground mt-2">days in a row</p>
              </CardContent>
            </Card>

            <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.2s'}}>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">Weekly Goal</CardTitle>
                <div className="p-2 bg-accent/10 rounded-lg">
                  <Calendar className="h-4 w-4 text-accent" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-3xl font-bold text-foreground">{profile?.weeklyGoal || 0}</div>
                <p className="text-xs text-muted-foreground mt-2">problems per week</p>
              </CardContent>
            </Card>

            <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.3s'}}>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">Experience Level</CardTitle>
                <div className="p-2 bg-success/10 rounded-lg">
                  <Trophy className="h-4 w-4 text-success" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-3xl font-bold text-foreground capitalize">{profile?.level || 'Beginner'}</div>
                <p className="text-xs text-muted-foreground mt-2">current level</p>
              </CardContent>
            </Card>
          </div>

          <Card className="card-premium animate-fade-in-scale">
            <CardHeader>
              <CardTitle className="flex items-center gap-3">
                <div className="p-2 bg-accent/10 rounded-lg">
                  <Code className="w-5 h-5 text-accent" />
                </div>
                <span className="text-gradient font-semibold">Recent Activity</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="flex items-center justify-between p-4 rounded-lg bg-muted/30">
                  <div className="flex items-center gap-4">
                    <div className="p-2 bg-primary/10 rounded-lg">
                      <Target className="w-4 h-4 text-primary" />
                    </div>
                    <div>
                      <h4 className="font-semibold text-foreground">Last Problem Solved</h4>
                      <p className="text-sm text-muted-foreground">
                        {profile?.last_submission_date 
                          ? new Date(profile.last_submission_date).toLocaleDateString()
                          : 'No problems solved yet'
                        }
                      </p>
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-between p-4 rounded-lg bg-muted/30">
                  <div className="flex items-center gap-4">
                    <div className="p-2 bg-success/10 rounded-lg">
                      <Calendar className="w-4 h-4 text-success" />
                    </div>
                    <div>
                      <h4 className="font-semibold text-foreground">Member Since</h4>
                      <p className="text-sm text-muted-foreground">
                        {profile?.createdAt 
                          ? new Date(profile.createdAt).toLocaleDateString()
                          : 'Recently'
                        }
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
