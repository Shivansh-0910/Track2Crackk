import { useEffect, useState, useMemo } from "react"
import { Link } from "react-router-dom"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Search, BookOpen, TrendingUp, Calendar, Filter } from "lucide-react"
import { getTopics } from "@/api/topics"
import { useToast } from "@/hooks/useToast"

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
    if (progress >= 80) return 'bg-green-500'
    if (progress >= 60) return 'bg-blue-500'
    if (progress >= 40) return 'bg-yellow-500'
    return 'bg-red-500'
  }

  const getAccuracyBadge = (accuracy) => {
    if (accuracy >= 80) return 'default'
    if (accuracy >= 60) return 'secondary'
    return 'destructive'
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
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Topic Tracker</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Track your progress across different DSA topics</p>
        </div>
      </div>

      {/* Filters and Search */}
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardContent className="p-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <Input
                placeholder="Search topics..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={sortBy} onValueChange={setSortBy}>
              <SelectTrigger className="w-full md:w-48">
                <SelectValue placeholder="Sort by" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="name">Name</SelectItem>
                <SelectItem value="progress">Progress</SelectItem>
                <SelectItem value="accuracy">Accuracy</SelectItem>
                <SelectItem value="lastActivity">Last Activity</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filterBy} onValueChange={setFilterBy}>
              <SelectTrigger className="w-full md:w-48">
                <SelectValue placeholder="Filter by" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Topics</SelectItem>
                <SelectItem value="completed">Completed</SelectItem>
                <SelectItem value="in-progress">In Progress</SelectItem>
                <SelectItem value="not-started">Not Started</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Topics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredTopics.map((topic) => (
            <Link key={topic._id} to={`/topics/${topic._id}`}>
              <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 cursor-pointer">
                <CardHeader className="pb-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="text-2xl">{topic.icon}</div>
                      <div>
                        <CardTitle className="text-lg">{topic.name}</CardTitle>
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          {topic.solvedProblems}/{topic.totalProblems} problems
                        </p>
                      </div>
                    </div>
                    <Badge variant={getAccuracyBadge(topic.accuracy)}>
                      {topic.accuracy}%
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div>
                    <div className="flex justify-between text-sm mb-2">
                      <span className="text-gray-600 dark:text-gray-400">Progress</span>
                      <span className="font-medium">{topic.progress}%</span>
                    </div>
                    <Progress value={topic.progress} className="h-2" />
                  </div>

                  <div className="grid grid-cols-3 gap-2 text-center">
                    <div className="bg-green-50 dark:bg-green-900/20 rounded-lg p-2">
                      <div className="text-sm font-medium text-green-700 dark:text-green-400">
                        {topic.difficulty.easy}
                      </div>
                      <div className="text-xs text-green-600 dark:text-green-500">Easy</div>
                    </div>
                    <div className="bg-yellow-50 dark:bg-yellow-900/20 rounded-lg p-2">
                      <div className="text-sm font-medium text-yellow-700 dark:text-yellow-400">
                        {topic.difficulty.medium}
                      </div>
                      <div className="text-xs text-yellow-600 dark:text-yellow-500">Medium</div>
                    </div>
                    <div className="bg-red-50 dark:bg-red-900/20 rounded-lg p-2">
                      <div className="text-sm font-medium text-red-700 dark:text-red-400">
                        {topic.difficulty.hard}
                      </div>
                      <div className="text-xs text-red-600 dark:text-red-500">Hard</div>
                    </div>
                  </div>

                  <div className="flex items-center justify-between text-sm text-gray-600 dark:text-gray-400">
                    <div className="flex items-center gap-1">
                      <Calendar className="w-4 h-4" />
                      <span>Last: {new Date(topic.lastActivity).toLocaleDateString()}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <TrendingUp className="w-4 h-4" />
                      <span>{topic.accuracy}% accuracy</span>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </Link>
        ))}
      </div>

      {filteredTopics.length === 0 && (
        <div className="text-center py-12">
          <BookOpen className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">No topics found</h3>
          <p className="text-gray-600 dark:text-gray-400">Try adjusting your search or filter criteria</p>
        </div>
      )}
    </div>
  )
}