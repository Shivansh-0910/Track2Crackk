import { useEffect, useState, useMemo } from "react"
import { Link } from "react-router-dom"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Search, BookOpen, TrendingUp, Calendar, Filter, Target, Award, Clock } from "lucide-react"
import { getTopics } from "@/api/topics"
import { useToast } from "@/hooks/useToast"
import { Button } from "@/components/ui/button"

export function TopicTracker() {
  const [topics, setTopics] = useState([])

  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [sortBy, setSortBy] = useState('name')
  const [filterBy, setFilterBy] = useState('all')
  const { toast } = useToast()

  useEffect(() => {
    const fetchTopics = async () => {
      try {
        const response = await getTopics()
        const topicsData = response.topics
        setTopics(topicsData)
      } catch (error) {
        console.error('Error fetching topics:', error)
        toast({
          title: "Error",
          description: "Failed to load topics",
          variant: "destructive"
        })
      } finally {
        setLoading(false)
      }
    }

    fetchTopics()
  }, [])

  // Use useMemo to prevent unnecessary re-calculations
  const filteredTopics = useMemo(() => {
    if (!topics || topics.length === 0) return []

    let filtered = topics.filter(topic =>
      topic.name.toLowerCase().includes(searchTerm.toLowerCase())
    )

    // Apply filters
    if (filterBy === 'completed') {
      filtered = filtered.filter(topic => topic.solvedProblems === topic.totalProblems)
    } else if (filterBy === 'in-progress') {
      filtered = filtered.filter(topic => topic.solvedProblems > 0 && topic.solvedProblems < topic.totalProblems)
    } else if (filterBy === 'not-started') {
      filtered = filtered.filter(topic => topic.solvedProblems === 0)
    }

    // Apply sorting
    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'progress':
          return (b.solvedProblems / b.totalProblems) - (a.solvedProblems / a.totalProblems)
        case 'accuracy':
          return b.accuracy - a.accuracy
        case 'lastActivity':
          return new Date(b.lastActivity).getTime() - new Date(a.lastActivity).getTime()
        default:
          return a.name.localeCompare(b.name)
      }
    })

    // Add pre-calculated progress to prevent re-calculation in render
    return filtered.map(topic => ({
      ...topic,
      progress: Math.round((topic.solvedProblems / topic.totalProblems) * 100)
    }))
  }, [topics, searchTerm, sortBy, filterBy])

  const getProgressColor = (progress) => {
    if (progress >= 80) return 'bg-success'
    if (progress >= 60) return 'bg-primary'
    if (progress >= 40) return 'bg-warning'
    return 'bg-destructive'
  }

  const getAccuracyBadge = (accuracy) => {
    if (accuracy >= 80) return 'default'
    if (accuracy >= 60) return 'secondary'
    return 'destructive'
  }

  const getStatusIcon = (progress) => {
    if (progress === 100) return '🏆'
    if (progress >= 80) return '🔥'
    if (progress >= 60) return '⚡'
    if (progress >= 40) return '📈'
    if (progress > 0) return '🚀'
    return '📚'
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Loading your topics...</p>
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
          <div>
            <h1 className="text-4xl font-bold mb-3 bg-gradient-to-r from-white to-white/90 bg-clip-text text-transparent">
              📚 Topic Tracker
            </h1>
            <p className="text-lg text-white/90 font-medium">Track your progress across all DSA topics</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
              <BookOpen className="w-6 h-6 text-white" />
            </div>
            <div className="text-right">
              <div className="text-2xl font-bold text-white">{topics.length}</div>
              <p className="text-sm text-white/80">topics available</p>
            </div>
          </div>
        </div>
      </div>

      {/* Search and Filter Controls */}
      <Card className="card-premium animate-fade-in-scale">
        <CardContent className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder="Search topics..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 input-premium"
              />
            </div>
            
            <Select value={filterBy} onValueChange={setFilterBy}>
              <SelectTrigger className="input-premium">
                <Filter className="w-4 h-4 mr-2" />
                <SelectValue placeholder="Filter by status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Topics</SelectItem>
                <SelectItem value="completed">Completed</SelectItem>
                <SelectItem value="in-progress">In Progress</SelectItem>
                <SelectItem value="not-started">Not Started</SelectItem>
              </SelectContent>
            </Select>

            <Select value={sortBy} onValueChange={setSortBy}>
              <SelectTrigger className="input-premium">
                <TrendingUp className="w-4 h-4 mr-2" />
                <SelectValue placeholder="Sort by" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="name">Name</SelectItem>
                <SelectItem value="progress">Progress</SelectItem>
                <SelectItem value="accuracy">Accuracy</SelectItem>
                <SelectItem value="lastActivity">Last Activity</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Topics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredTopics.map((topic, index) => (
          <Card 
            key={topic._id} 
            className="card-premium hover:shadow-glow transition-all duration-300 hover:scale-[1.02] animate-slide-in-up cursor-pointer group"
            style={{ animationDelay: `${index * 0.1}s` }}
            onClick={() => window.location.href = `/topics/${topic._id}`}
          >
            <CardContent className="p-6">
              <div className="space-y-4">
                {/* Header */}
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="text-2xl">{getStatusIcon(topic.progress)}</div>
                    <div>
                      <h3 className="font-semibold text-foreground group-hover:text-primary transition-colors">
                        {topic.name}
                      </h3>
                      <p className="text-sm text-muted-foreground">{topic.description}</p>
                    </div>
                  </div>
                  <Badge className={getAccuracyBadge(topic.accuracy)}>
                    {topic.accuracy}%
                  </Badge>
                </div>

                {/* Progress Section */}
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-foreground">Progress</span>
                    <span className="text-sm font-bold text-primary">{topic.progress}%</span>
                  </div>
                  <Progress value={topic.progress} className="h-2" />
                  <div className="flex justify-between text-xs text-muted-foreground">
                    <span>{topic.solvedProblems} solved</span>
                    <span>{topic.totalProblems} total</span>
                  </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-2 gap-3 pt-3 border-t border-border/30">
                  <div className="text-center p-2 bg-muted/30 rounded-lg">
                    <div className="flex items-center justify-center gap-1 mb-1">
                      <Target className="w-3 h-3 text-primary" />
                      <span className="text-xs font-medium text-foreground">Goal</span>
                    </div>
                    <div className="text-sm font-bold text-primary">{topic.targetProblems || topic.totalProblems}</div>
                  </div>
                  <div className="text-center p-2 bg-muted/30 rounded-lg">
                    <div className="flex items-center justify-center gap-1 mb-1">
                      <Clock className="w-3 h-3 text-accent" />
                      <span className="text-xs font-medium text-foreground">Avg Time</span>
                    </div>
                    <div className="text-sm font-bold text-accent">{topic.averageTime || '--'} min</div>
                  </div>
                </div>

                {/* Last Activity */}
                {topic.lastActivity && (
                  <div className="flex items-center gap-2 text-xs text-muted-foreground pt-2 border-t border-border/30">
                    <Calendar className="w-3 h-3" />
                    <span>Last active: {new Date(topic.lastActivity).toLocaleDateString()}</span>
                  </div>
                )}

                {/* Action Button */}
                <div className="pt-3">
                  <Button 
                    className="w-full btn-premium text-white font-semibold"
                    onClick={(e) => {
                      e.stopPropagation()
                      window.location.href = `/topics/${topic._id}`
                    }}
                  >
                    <BookOpen className="w-4 h-4 mr-2" />
                    {topic.progress === 100 ? 'Review' : 'Practice'}
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Empty State */}
      {filteredTopics.length === 0 && (
        <Card className="card-premium">
          <CardContent className="p-12 text-center">
            <div className="flex flex-col items-center gap-4">
              <div className="p-4 bg-muted/30 rounded-full">
                <BookOpen className="w-8 h-8 text-muted-foreground" />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-2">No topics found</h3>
                <p className="text-muted-foreground">
                  {searchTerm ? 'Try adjusting your search terms' : 'Topics will appear here once available'}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Summary Stats */}
      {filteredTopics.length > 0 && (
        <Card className="card-premium animate-fade-in-scale">
          <CardHeader>
            <CardTitle className="flex items-center gap-3">
              <div className="p-2 bg-accent/10 rounded-lg">
                <Award className="w-5 h-5 text-accent" />
              </div>
              <span className="text-gradient font-semibold">Summary</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-primary mb-2">
                  {filteredTopics.length}
                </div>
                <div className="text-sm text-muted-foreground font-medium">Total Topics</div>
              </div>
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-success mb-2">
                  {filteredTopics.filter(t => t.progress === 100).length}
                </div>
                <div className="text-sm text-muted-foreground font-medium">Completed</div>
              </div>
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-warning mb-2">
                  {filteredTopics.filter(t => t.progress > 0 && t.progress < 100).length}
                </div>
                <div className="text-sm text-muted-foreground font-medium">In Progress</div>
              </div>
              <div className="text-center p-4 bg-muted/30 rounded-xl">
                <div className="text-3xl font-bold text-accent mb-2">
                  {Math.round(filteredTopics.reduce((acc, t) => acc + t.progress, 0) / filteredTopics.length)}%
                </div>
                <div className="text-sm text-muted-foreground font-medium">Avg Progress</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}