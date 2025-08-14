import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  Radar
} from 'recharts'
import {
  TrendingUp,
  Calendar,
  Target,
  Award,
  Clock,
  BarChart3,
  Trophy,
  Zap,
  Activity,
  Users
} from "lucide-react"
import { getAnalytics } from "@/api/analytics"
import { useToast } from "@/hooks/useToast"
import { Badge } from "@/components/ui/badge"

export function Analytics() {
  const [analytics, setAnalytics] = useState(null)
  const [loading, setLoading] = useState(true)
  const [timeRange, setTimeRange] = useState('30d')
  const { toast } = useToast()

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const response = await getAnalytics()
        setAnalytics(response.analytics)
      } catch (error) {
        console.error('Error fetching analytics:', error)
        toast({
          title: "Error",
          description: "Failed to load analytics data",
          variant: "destructive"
        })
      } finally {
        setLoading(false)
      }
    }

    fetchAnalytics()
  }, [])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Loading your analytics...</p>
        </div>
      </div>
    )
  }

  const COLORS = ['#3B82F6', '#8B5CF6', '#10B981', '#F59E0B', '#EF4444', '#6366F1']

  const difficultyData = [
    { name: 'Easy', value: analytics?.difficultyDistribution?.easy || 0, color: '#10B981' },
    { name: 'Medium', value: analytics?.difficultyDistribution?.medium || 0, color: '#F59E0B' },
    { name: 'Hard', value: analytics?.difficultyDistribution?.hard || 0, color: '#EF4444' }
  ]

  return (
    <div className="space-y-8 animate-in fade-in-50 duration-500">
      {/* Header Section */}
      <div className="card-premium rounded-3xl p-8 text-white shadow-glow animate-fade-in-scale">
        <div className="absolute inset-0 rounded-3xl bg-gradient-to-br from-primary via-accent to-primary/80 opacity-90"></div>
        <div className="relative z-10 flex items-center justify-between">
          <div>
            <h1 className="text-4xl font-bold mb-3 bg-gradient-to-r from-white to-white/90 bg-clip-text text-transparent">
              📊 Analytics Dashboard
            </h1>
            <p className="text-lg text-white/90 font-medium">Track your progress and identify areas for improvement</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
              <BarChart3 className="w-6 h-6 text-white" />
            </div>
            <div className="text-right">
              <div className="text-2xl font-bold text-white">{analytics?.totalProblems || 0}</div>
              <p className="text-sm text-white/80">problems solved</p>
            </div>
          </div>
        </div>
      </div>

      {/* Time Range Selector */}
      <Card className="card-premium animate-fade-in-scale">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-primary/10 rounded-lg">
                <Calendar className="w-5 h-5 text-primary" />
              </div>
              <span className="font-semibold text-foreground">Time Range</span>
            </div>
            <Select value={timeRange} onValueChange={setTimeRange}>
              <SelectTrigger className="w-40 input-premium">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="7d">Last 7 days</SelectItem>
                <SelectItem value="30d">Last 30 days</SelectItem>
                <SelectItem value="90d">Last 90 days</SelectItem>
                <SelectItem value="1y">Last year</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Problems</CardTitle>
            <div className="p-2 bg-primary/10 rounded-lg">
              <Target className="h-4 w-4 text-primary" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{analytics?.totalProblems || 0}</div>
            <p className="text-xs text-success flex items-center gap-1 mt-2">
              <TrendingUp className="w-3 h-3" />
              +12 this week
            </p>
          </CardContent>
        </Card>

        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.1s'}}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Success Rate</CardTitle>
            <div className="p-2 bg-success/10 rounded-lg">
              <Trophy className="h-4 w-4 text-success" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{analytics?.successRate || 0}%</div>
            <p className="text-xs text-muted-foreground mt-2">accuracy</p>
          </CardContent>
        </Card>

        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.2s'}}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Avg Time</CardTitle>
            <div className="p-2 bg-warning/10 rounded-lg">
              <Clock className="h-4 w-4 text-warning" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{analytics?.averageTime || 0} min</div>
            <p className="text-xs text-muted-foreground mt-2">per problem</p>
          </CardContent>
        </Card>

        <Card className="card-premium hover:shadow-glow transition-all duration-500 hover:scale-105 animate-slide-in-up" style={{animationDelay: '0.3s'}}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Streak</CardTitle>
            <div className="p-2 bg-accent/10 rounded-lg">
              <Zap className="h-4 w-4 text-accent" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-foreground">{analytics?.currentStreak || 0}</div>
            <p className="text-xs text-muted-foreground mt-2">days</p>
          </CardContent>
        </Card>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Difficulty Distribution */}
        <Card className="card-premium animate-fade-in-scale">
          <CardHeader>
            <CardTitle className="flex items-center gap-3">
              <div className="p-2 bg-primary/10 rounded-lg">
                <BarChart3 className="w-5 h-5 text-primary" />
              </div>
              <span className="text-gradient font-semibold">Difficulty Distribution</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={difficultyData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {difficultyData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
            <div className="space-y-3 mt-6">
              {difficultyData.map((item, index) => (
                <div key={index} className="flex items-center justify-between p-2 rounded-lg bg-muted/30">
                  <div className="flex items-center gap-3">
                    <div className="w-3 h-3 rounded-full shadow-sm" style={{ backgroundColor: item.color }}></div>
                    <span className="text-sm font-medium text-foreground">{item.name}</span>
                  </div>
                  <span className="font-bold text-foreground">{item.value}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Progress Over Time */}
        <Card className="card-premium animate-fade-in-scale">
          <CardHeader>
            <CardTitle className="flex items-center gap-3">
              <div className="p-2 bg-success/10 rounded-lg">
                <TrendingUp className="w-5 h-5 text-success" />
              </div>
              <span className="text-gradient font-semibold">Progress Over Time</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart
                data={analytics?.progressOverTime || [
                  { date: 'Week 1', problems: 5 },
                  { date: 'Week 2', problems: 12 },
                  { date: 'Week 3', problems: 18 },
                  { date: 'Week 4', problems: 25 },
                  { date: 'Week 5', problems: 32 },
                  { date: 'Week 6', problems: 40 }
                ]}
              >
                <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
                <XAxis dataKey="date" stroke="#6B7280" />
                <YAxis stroke="#6B7280" />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'hsl(var(--card))', 
                    border: '1px solid hsl(var(--border))',
                    borderRadius: '8px'
                  }}
                />
                <Line 
                  type="monotone" 
                  dataKey="problems" 
                  stroke="#3B82F6" 
                  strokeWidth={3}
                  dot={{ fill: '#3B82F6', strokeWidth: 2, r: 4 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Topic Performance */}
      <Card className="card-premium animate-fade-in-scale">
        <CardHeader>
          <CardTitle className="flex items-center gap-3">
            <div className="p-2 bg-accent/10 rounded-lg">
              <Activity className="w-5 h-5 text-accent" />
            </div>
            <span className="text-gradient font-semibold">Topic Performance</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart
              data={analytics?.topicPerformance || [
                { topic: 'Arrays', solved: 15, accuracy: 85 },
                { topic: 'Strings', solved: 12, accuracy: 78 },
                { topic: 'Linked Lists', solved: 8, accuracy: 72 },
                { topic: 'Trees', solved: 10, accuracy: 68 },
                { topic: 'Graphs', solved: 6, accuracy: 65 },
                { topic: 'DP', solved: 4, accuracy: 60 }
              ]}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
              <XAxis dataKey="topic" stroke="#6B7280" />
              <YAxis stroke="#6B7280" />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: 'hsl(var(--card))', 
                  border: '1px solid hsl(var(--border))',
                  borderRadius: '8px'
                }}
              />
              <Bar dataKey="solved" fill="#3B82F6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Recent Activity */}
      <Card className="card-premium animate-fade-in-scale">
        <CardHeader>
          <CardTitle className="flex items-center gap-3">
            <div className="p-2 bg-warning/10 rounded-lg">
              <Users className="w-5 h-5 text-warning" />
            </div>
            <span className="text-gradient font-semibold">Recent Activity</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {(analytics?.recentActivity || [
              { problem: 'Two Sum', topic: 'Arrays', difficulty: 'Easy', date: '2024-01-15', status: 'Solved' },
              { problem: 'Valid Parentheses', topic: 'Stacks', difficulty: 'Easy', date: '2024-01-14', status: 'Solved' },
              { problem: 'Merge Two Lists', topic: 'Linked Lists', difficulty: 'Easy', date: '2024-01-13', status: 'Solved' },
              { problem: 'Binary Tree Inorder', topic: 'Trees', difficulty: 'Medium', date: '2024-01-12', status: 'Attempted' },
              { problem: 'Climbing Stairs', topic: 'DP', difficulty: 'Easy', date: '2024-01-11', status: 'Solved' }
            ]).map((activity, index) => (
              <div key={index} className="flex items-center justify-between p-4 rounded-lg bg-muted/30 hover:bg-muted/50 transition-colors">
                <div className="flex items-center gap-4">
                  <div className="p-2 bg-primary/10 rounded-lg">
                    <Target className="w-4 h-4 text-primary" />
                  </div>
                  <div>
                    <h4 className="font-semibold text-foreground">{activity.problem}</h4>
                    <p className="text-sm text-muted-foreground">{activity.topic} • {activity.difficulty}</p>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-sm text-muted-foreground">
                    {new Date(activity.date).toLocaleDateString()}
                  </span>
                  <Badge 
                    variant={activity.status === 'Solved' ? 'default' : 'secondary'}
                    className={activity.status === 'Solved' ? 'bg-success text-success-foreground' : ''}
                  >
                    {activity.status}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}