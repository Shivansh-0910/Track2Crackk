import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Slider } from "@/components/ui/slider"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Checkbox } from "@/components/ui/checkbox"
import {
  Calendar,
  Clock,
  Target,
  RefreshCw,
  CheckCircle,
  ExternalLink,
  Settings,
  TrendingUp,
  BookOpen
} from "lucide-react"
import { getDailyRecommendations } from "@/api/problems"
import { useToast } from "@/hooks/useToast"

export function DailyPlan() {
  const [recommendations, setRecommendations] = useState([])
  const [loading, setLoading] = useState(true)
  const [generating, setGenerating] = useState(false)
  const [planConfig, setPlanConfig] = useState({
    timeAvailable: 60,
    difficultyPreference: 'balanced',
    topicFocus: 'weak-areas',
    companySpecific: false,
    targetCompany: ''
  })
  const [completedProblems, setCompletedProblems] = useState([])
  const { toast } = useToast()

  useEffect(() => {
    fetchDailyPlan()
  }, [])

  const fetchDailyPlan = async (config = null) => {
    try {
      console.log('Fetching daily plan with config:', config || planConfig)
      const response = await getDailyRecommendations(config || planConfig)
      console.log('API Response:', response)
      console.log('Total estimated time from API:', response.totalEstimatedTime)
      console.log('Time limit:', response.timeLimit)
      setRecommendations(response.problems)
      console.log('Daily plan loaded successfully:', response.problems.length, 'problems')
    } catch (error) {
      console.error('Error fetching daily plan:', error)
      toast({
        title: "Error",
        description: "Failed to load daily plan",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const generateNewPlan = async () => {
    setGenerating(true)
    try {
      console.log('Generating new plan with config:', planConfig)
      // Generate new plan with current configuration
      await fetchDailyPlan(planConfig)
      toast({
        title: "Success",
        description: "New daily plan generated!",
      })
    } catch (error) {
      console.error('Error generating plan:', error)
      toast({
        title: "Error",
        description: "Failed to generate new plan",
        variant: "destructive"
      })
    } finally {
      setGenerating(false)
    }
  }

  const markAsCompleted = (problemId) => {
    setCompletedProblems(prev => [...prev, problemId])
    toast({
      title: "Great job! 🎉",
      description: "Problem marked as completed",
    })
  }

  const getTotalEstimatedTime = () => {
    return recommendations.reduce((total, problem) => total + problem.estimatedTime, 0)
  }

  const getCompletionProgress = () => {
    if (recommendations.length === 0) return 0
    return (completedProblems.length / recommendations.length) * 100
  }

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'Easy': return 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400'
      case 'Medium': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-400'
      case 'Hard': return 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400'
      default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400'
    }
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
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Daily Plan</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">
            Personalized problem recommendations for today
          </p>
        </div>
        <Button
          onClick={generateNewPlan}
          disabled={generating}
          className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700"
        >
          <RefreshCw className={`w-4 h-4 mr-2 ${generating ? 'animate-spin' : ''}`} />
          {generating ? 'Generating...' : 'Generate New Plan'}
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Plan Configuration */}
        <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Settings className="w-5 h-5" />
              Plan Settings
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div>
              <label className="text-sm font-medium mb-3 block">
                Time Available: {planConfig.timeAvailable} minutes
              </label>
              <Slider
                value={[planConfig.timeAvailable]}
                onValueChange={(value) => setPlanConfig(prev => ({ ...prev, timeAvailable: value[0] }))}
                max={180}
                min={15}
                step={15}
                className="w-full"
              />
            </div>

            <div>
              <label className="text-sm font-medium mb-2 block">Difficulty Preference</label>
              <Select
                value={planConfig.difficultyPreference}
                onValueChange={(value) => setPlanConfig(prev => ({ ...prev, difficultyPreference: value }))}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="easy">Focus on Easy</SelectItem>
                  <SelectItem value="balanced">Balanced Mix</SelectItem>
                  <SelectItem value="challenging">More Challenging</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div>
              <label className="text-sm font-medium mb-2 block">Topic Focus</label>
              <Select
                value={planConfig.topicFocus}
                onValueChange={(value) => setPlanConfig(prev => ({ ...prev, topicFocus: value }))}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="weak-areas">Weak Areas</SelectItem>
                  <SelectItem value="balanced">Balanced</SelectItem>
                  <SelectItem value="strong-areas">Strong Areas</SelectItem>
                  <SelectItem value="random">Random Mix</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-3">
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="companySpecific"
                  checked={planConfig.companySpecific}
                  onCheckedChange={(checked) => setPlanConfig(prev => ({ ...prev, companySpecific: !!checked }))}
                />
                <label htmlFor="companySpecific" className="text-sm font-medium">
                  Company-specific preparation
                </label>
              </div>

              {planConfig.companySpecific && (
                <Select
                  value={planConfig.targetCompany}
                  onValueChange={(value) => setPlanConfig(prev => ({ ...prev, targetCompany: value }))}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select company" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="google">Google</SelectItem>
                    <SelectItem value="amazon">Amazon</SelectItem>
                    <SelectItem value="microsoft">Microsoft</SelectItem>
                    <SelectItem value="meta">Meta</SelectItem>
                    <SelectItem value="apple">Apple</SelectItem>
                  </SelectContent>
                </Select>
              )}
            </div>
          </CardContent>
        </Card>

        {/* Daily Plan Content */}
        <div className="lg:col-span-3 space-y-6">
          {/* Progress Overview */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-blue-100 dark:bg-blue-900/20 rounded-lg">
                    <Target className="w-5 h-5 text-blue-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-600 dark:text-gray-400">Problems</p>
                    <p className="text-xl font-bold">{completedProblems.length}/{recommendations.length}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-green-100 dark:bg-green-900/20 rounded-lg">
                    <Clock className="w-5 h-5 text-green-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-600 dark:text-gray-400">Est. Time</p>
                    <div className="flex items-center gap-2">
                      <p className={`text-xl font-bold ${
                        getTotalEstimatedTime() > planConfig.timeAvailable
                          ? 'text-red-600 dark:text-red-400'
                          : 'text-green-600 dark:text-green-400'
                      }`}>
                        {getTotalEstimatedTime()} min
                      </p>
                      <span className="text-sm text-gray-500">/ {planConfig.timeAvailable} min</span>
                    </div>
                    {getTotalEstimatedTime() > planConfig.timeAvailable && (
                      <p className="text-xs text-red-500 mt-1">⚠️ Exceeds time limit</p>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-purple-100 dark:bg-purple-900/20 rounded-lg">
                    <TrendingUp className="w-5 h-5 text-purple-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-600 dark:text-gray-400">Progress</p>
                    <p className="text-xl font-bold">{Math.round(getCompletionProgress())}%</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Today's Problems */}
          <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Calendar className="w-5 h-5 text-blue-600" />
                Today's Problems
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {recommendations.map((problem, index) => {
                const isCompleted = completedProblems.includes(problem._id)
                return (
                  <div
                    key={problem._id}
                    className={`p-4 rounded-lg border transition-all duration-200 ${
                      isCompleted
                        ? 'bg-green-50 dark:bg-green-900/10 border-green-200 dark:border-green-800'
                        : 'bg-gray-50/50 dark:bg-gray-800/50 border-gray-200 dark:border-gray-700 hover:bg-gray-100/50 dark:hover:bg-gray-700/50'
                    }`}
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-4">
                        <div className="flex items-center justify-center w-8 h-8 rounded-full bg-blue-100 dark:bg-blue-900/20 text-blue-600 font-medium text-sm">
                          {index + 1}
                        </div>
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-1">
                            <h3 className="font-medium text-gray-900 dark:text-white">
                              {problem.name}
                            </h3>
                            <Badge className={getDifficultyColor(problem.difficulty)}>
                              {problem.difficulty}
                            </Badge>
                          </div>
                          <div className="flex items-center gap-4 text-sm text-gray-600 dark:text-gray-400">
                            <span className="flex items-center gap-1">
                              <BookOpen className="w-4 h-4" />
                              {problem.topic}
                            </span>
                            <span className="flex items-center gap-1">
                              <Clock className="w-4 h-4" />
                              ~{problem.estimatedTime} min
                            </span>
                          </div>
                        </div>
                      </div>
                      <div className="flex items-center gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => window.open(problem.url, '_blank')}
                        >
                          <ExternalLink className="w-4 h-4" />
                        </Button>
                        {!isCompleted ? (
                          <Button
                            size="sm"
                            onClick={() => markAsCompleted(problem._id)}
                            className="bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700"
                          >
                            <CheckCircle className="w-4 h-4 mr-1" />
                            Complete
                          </Button>
                        ) : (
                          <Badge variant="secondary" className="bg-green-100 text-green-800">
                            ✓ Completed
                          </Badge>
                        )}
                      </div>
                    </div>
                  </div>
                )
              })}
            </CardContent>
          </Card>

          {recommendations.length === 0 && (
            <div className="text-center py-12">
              <Calendar className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">No plan generated yet</h3>
              <p className="text-gray-600 dark:text-gray-400">Configure your preferences and generate a daily plan</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}