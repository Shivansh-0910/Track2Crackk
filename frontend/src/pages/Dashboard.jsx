import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  Target,
  Calendar,
  TrendingUp,
  CheckCircle,
  Clock,
  ExternalLink,
  Flame,
  Trophy,
  BookOpen
} from "lucide-react"
import { getUserProfile } from "@/api/user"
import { getDailyRecommendations } from "@/api/problems"
import { getAnalytics } from "@/api/analytics"
import { useToast } from "@/hooks/useToast"
import { PieChart, Pie, Cell, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts'
import { AIRecommendations } from "@/components/AIRecommendations"

export function Dashboard() {
  const [userProfile, setUserProfile] = useState(null)
  const [recommendations, setRecommendations] = useState([])
  const [analytics, setAnalytics] = useState(null)
  const [loading, setLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [profileRes, recommendationsRes, analyticsRes] = await Promise.all([
          getUserProfile(),
          getDailyRecommendations(),
          getAnalytics()
        ])

        setUserProfile(profileRes.user)
        setRecommendations(recommendationsRes.problems)
        setAnalytics(analyticsRes.analytics)
      } catch (error) {
        console.error('Error fetching dashboard data:', error)
        toast({
          title: "Error",
          description: "Failed to load dashboard data",
          variant: "destructive"
        })
      } finally {
        setLoading(false)
      }
    }

    fetchDashboardData()
  }, [])

  const handleMarkAsSolved = async (problemId) => {
    console.log('Marking problem as solved:', problemId)
    toast({
      title: "Success",
      description: "Problem marked as solved!",
    })
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  const pieData = analytics?.topicMastery?.slice(0, 4).map((item) => ({
    name: item.topic,
    value: item.mastery,
    color: ['#3B82F6', '#8B5CF6', '#10B981', '#F59E0B'][analytics.topicMastery.indexOf(item) % 4]
  })) || []

  const weeklyProgress = userProfile?.totalProblems ? (userProfile.totalProblems % userProfile.weeklyGoal) : 0

  return (
    <div className="space-y-6 animate-in fade-in-50 duration-500">
      {/* Welcome Section */}
      <div className="card-premium rounded-3xl p-8 text-white shadow-glow animate-fade-in-scale">
        <div className="absolute inset-0 rounded-3xl bg-gradient-to-br from-primary via-accent to-primary/80 opacity-90"></div>
        <div className="relative z-10 flex items-center justify-between">
          <div>
            <h1 className="text-4xl font-bold mb-3 bg-gradient-to-r from-white to-white/90 bg-clip-text text-transparent">
              Welcome back, {userProfile?.name}! 👋
            </h1>
            <p className="text-lg text-white/90 font-medium">Ready to crack some interviews today?</p>
          </div>
          <div className="text-right">
            <div className="flex items-center gap-3 mb-2">
              <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
                <Flame className="w-6 h-6 text-orange-300" />
              </div>
              <div>
                <span className="text-3xl font-bold text-white">{userProfile?.currentStreak}</span>
                <p className="text-sm text-white/80">day streak</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Problems</CardTitle>
            <div className="p-2 bg-primary/10 rounded-lg">
              <Target className="h-4 w-4 text-primary" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{userProfile?.totalProblems}</div>
            <p className="text-xs text-success flex items-center gap-1 mt-2">
              <TrendingUp className="w-3 h-3" />
              +12 this week
            </p>
          </CardContent>
        </Card>

        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.1s'}}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Current Streak</CardTitle>
            <div className="p-2 bg-warning/10 rounded-lg">
              <Flame className="h-4 w-4 text-warning" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{userProfile?.currentStreak}</div>
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
            <div className="text-3xl font-bold text-foreground">{weeklyProgress}/{userProfile?.weeklyGoal}</div>
            <Progress value={(weeklyProgress / userProfile?.weeklyGoal) * 100} className="mt-2" />
          </CardContent>
        </Card>

        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.3s'}}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Today's Status</CardTitle>
            <div className="p-2 bg-success/10 rounded-lg">
              <CheckCircle className="h-4 w-4 text-success" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-success">3/3</div>
            <p className="text-xs text-muted-foreground mt-2">problems completed</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* AI-Powered Recommendations */}
        <div className="lg:col-span-2">
          <AIRecommendations userProfile={userProfile} />
        </div>

        {/* Topic Mastery Chart */}
        <Card className="card-premium animate-fade-in-scale">
          <CardHeader>
            <CardTitle className="flex items-center gap-3">
              <div className="p-2 bg-warning/10 rounded-lg">
                <Trophy className="w-5 h-5 text-warning" />
              </div>
              <span className="text-gradient font-semibold">Topic Mastery</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={200}>
              <PieChart>
                <Pie
                  data={pieData}
                  cx="50%"
                  cy="50%"
                  innerRadius={40}
                  outerRadius={80}
                  paddingAngle={5}
                  dataKey="value"
                >
                  {pieData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
            <div className="space-y-3 mt-6">
              {pieData.map((item, index) => (
                <div key={index} className="flex items-center justify-between p-2 rounded-lg bg-muted/30">
                  <div className="flex items-center gap-3">
                    <div className="w-3 h-3 rounded-full shadow-sm" style={{ backgroundColor: item.color }}></div>
                    <span className="text-sm font-medium text-foreground">{item.name}</span>
                  </div>
                  <span className="font-bold text-foreground">{item.value}%</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Company Progress */}
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <TrendingUp className="w-5 h-5 text-indigo-600" />
            Company Preparation Progress
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={analytics?.companyProgress || []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="company" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="progress" fill="url(#colorGradient)" radius={[4, 4, 0, 0]} />
              <defs>
                <linearGradient id="colorGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#3B82F6" stopOpacity={0.8}/>
                  <stop offset="95%" stopColor="#8B5CF6" stopOpacity={0.8}/>
                </linearGradient>
              </defs>
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>


    </div>
  )
}