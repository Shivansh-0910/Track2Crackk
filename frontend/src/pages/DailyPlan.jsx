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
  BookOpen,
  Sparkles,
  Timer,
  Award
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
    switch (difficulty.toLowerCase()) {
      case 'easy':
        return 'bg-success text-success-foreground'
      case 'medium':
        return 'bg-warning text-warning-foreground'
      case 'hard':
        return 'bg-destructive text-destructive-foreground'
      default:
        return 'bg-secondary text-secondary-foreground'
    }
  }

  const getDifficultyIcon = (difficulty) => {
    switch (difficulty.toLowerCase()) {
      case 'easy':
        return '🟢'
      case 'medium':
        return '🟡'
      case 'hard':
        return '🔴'
      default:
        return '⚪'
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Loading your personalized plan...</p>
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
              📅 Your Daily Plan
            </h1>
            <p className="text-lg text-white/90 font-medium">Personalized problems for today's practice session</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
              <Sparkles className="w-6 h-6 text-white" />
            </div>
            <div className="text-right">
              <div className="text-2xl font-bold text-white">{recommendations.length}</div>
              <p className="text-sm text-white/80">problems today</p>
            </div>
          </div>
        </div>
      </div>

      {/* Plan Configuration */}
      <Card className="card-premium animate-fade-in-scale">
        <CardHeader>
          <CardTitle className="flex items-center gap-3">
            <div className="p-2 bg-primary/10 rounded-lg">
              <Settings className="w-5 h-5 text-primary" />
            </div>
            <span className="text-gradient font-semibold">Plan Configuration</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-foreground mb-2 block">
                  Time Available: {planConfig.timeAvailable} minutes
                </label>
                <Slider
                  value={[planConfig.timeAvailable]}
                  onValueChange={(value) => setPlanConfig(prev => ({ ...prev, timeAvailable: value[0] }))}
                  max={180}
                  min={30}
                  step={15}
                  className="w-full"
                />
                <div className="flex justify-between text-xs text-muted-foreground mt-1">
                  <span>30 min</span>
                  <span>180 min</span>
                </div>
              </div>

              <div>
                <label className="text-sm font-medium text-foreground mb-2 block">Difficulty Preference</label>
                <Select value={planConfig.difficultyPreference} onValueChange={(value) => setPlanConfig(prev => ({ ...prev, difficultyPreference: value }))}>
                  <SelectTrigger className="input-premium">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="balanced">Balanced Mix</SelectItem>
                    <SelectItem value="easy">Focus on Easy</SelectItem>
                    <SelectItem value="medium">Focus on Medium</SelectItem>
                    <SelectItem value="hard">Focus on Hard</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-foreground mb-2 block">Topic Focus</label>
                <Select value={planConfig.topicFocus} onValueChange={(value) => setPlanConfig(prev => ({ ...prev, topicFocus: value }))}>
                  <SelectTrigger className="input-premium">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="weak-areas">Weak Areas</SelectItem>
                    <SelectItem value="recent-topics">Recent Topics</SelectItem>
                    <SelectItem value="random">Random Mix</SelectItem>
                    <SelectItem value="company-specific">Company Specific</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox
                  id="companySpecific"
                  checked={planConfig.companySpecific}
                  onCheckedChange={(checked) => setPlanConfig(prev => ({ ...prev, companySpecific: checked }))}
                />
                <label htmlFor="companySpecific" className="text-sm font-medium text-foreground">
                  Include company-specific problems
                </label>
              </div>

              {planConfig.companySpecific && (
                <div>
                  <label className="text-sm font-medium text-foreground mb-2 block">Target Company</label>
                  <Select value={planConfig.targetCompany} onValueChange={(value) => setPlanConfig(prev => ({ ...prev, targetCompany: value }))}>
                    <SelectTrigger className="input-premium">
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
                </div>
              )}
            </div>
          </div>

          <Button 
            onClick={generateNewPlan} 
            disabled={generating}
            className="w-full btn-premium text-white font-semibold"
          >
            {generating ? (
              <>
                <RefreshCw className="mr-2 h-4 w-4 animate-spin" />
                Generating Plan...
              </>
            ) : (
              <>
                <Sparkles className="mr-2 h-4 w-4" />
                Generate New Plan
              </>
            )}
          </Button>
        </CardContent>
      </Card>

      {/* Progress Overview */}
      <Card className="card-premium animate-fade-in-scale">
        <CardHeader>
          <CardTitle className="flex items-center gap-3">
            <div className="p-2 bg-success/10 rounded-lg">
              <Target className="w-5 h-5 text-success" />
            </div>
            <span className="text-gradient font-semibold">Today's Progress</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center p-4 bg-muted/30 rounded-xl">
              <div className="text-3xl font-bold text-primary mb-2">
                {completedProblems.length}/{recommendations.length}
              </div>
              <div className="text-sm text-muted-foreground font-medium">Problems Completed</div>
            </div>
            <div className="text-center p-4 bg-muted/30 rounded-xl">
              <div className="text-3xl font-bold text-accent mb-2">
                {getTotalEstimatedTime()} min
              </div>
              <div className="text-sm text-muted-foreground font-medium">Total Time</div>
            </div>
            <div className="text-center p-4 bg-muted/30 rounded-xl">
              <div className="text-3xl font-bold text-success mb-2">
                {getCompletionProgress().toFixed(1)}%
              </div>
              <div className="text-sm text-muted-foreground font-medium">Completion</div>
            </div>
          </div>
          <div className="mt-6">
            <div className="flex justify-between text-sm text-muted-foreground mb-2">
              <span>Progress</span>
              <span>{getCompletionProgress().toFixed(1)}%</span>
            </div>
            <div className="w-full bg-muted rounded-full h-3">
              <div 
                className="bg-gradient-to-r from-primary to-accent h-3 rounded-full transition-all duration-500"
                style={{ width: `${getCompletionProgress()}%` }}
              ></div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Problems List */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold text-foreground">Today's Problems</h2>
          <Badge variant="secondary" className="font-semibold">
            {recommendations.length} problems
          </Badge>
        </div>

        {recommendations.map((problem, index) => (
          <Card 
            key={problem._id || index} 
            className={`card-premium hover:shadow-glow transition-all duration-300 hover:scale-[1.02] animate-slide-in-up ${
              completedProblems.includes(problem._id || index) ? 'ring-2 ring-success/50' : ''
            }`}
            style={{ animationDelay: `${index * 0.1}s` }}
          >
            <CardContent className="p-6">
              <div className="flex items-start justify-between">
                <div className="flex-1 space-y-3">
                  <div className="flex items-center gap-3">
                    <div className="flex items-center gap-2">
                      <span className="text-lg">{getDifficultyIcon(problem.difficulty)}</span>
                      <Badge className={getDifficultyColor(problem.difficulty)}>
                        {problem.difficulty}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Clock className="w-4 h-4" />
                      <span className="text-sm">{problem.estimatedTime} min</span>
                    </div>
                  </div>

                  <div>
                    <h3 className="text-lg font-semibold text-foreground mb-1">
                      {problem.title}
                    </h3>
                    <p className="text-muted-foreground text-sm line-clamp-2">
                      {problem.description}
                    </p>
                  </div>

                  <div className="flex items-center gap-4">
                    <div className="flex items-center gap-2">
                      <BookOpen className="w-4 h-4 text-primary" />
                      <span className="text-sm text-muted-foreground">{problem.topic}</span>
                    </div>
                    {problem.company && (
                      <div className="flex items-center gap-2">
                        <Award className="w-4 h-4 text-accent" />
                        <span className="text-sm text-muted-foreground">{problem.company}</span>
                      </div>
                    )}
                  </div>
                </div>

                <div className="flex flex-col items-end gap-3 ml-4">
                  {completedProblems.includes(problem._id || index) ? (
                    <div className="flex items-center gap-2 text-success">
                      <CheckCircle className="w-5 h-5" />
                      <span className="text-sm font-medium">Completed</span>
                    </div>
                  ) : (
                    <Button
                      onClick={() => markAsCompleted(problem._id || index)}
                      variant="outline"
                      size="sm"
                      className="hover:bg-success/10 hover:text-success hover:border-success/30"
                    >
                      Mark Complete
                    </Button>
                  )}
                  
                  <Button
                    variant="ghost"
                    size="sm"
                    className="hover:bg-primary/10 hover:text-primary"
                    onClick={() => window.open(problem.leetcodeUrl, '_blank')}
                  >
                    <ExternalLink className="w-4 h-4 mr-1" />
                    Solve
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}

        {recommendations.length === 0 && (
          <Card className="card-premium">
            <CardContent className="p-12 text-center">
              <div className="flex flex-col items-center gap-4">
                <div className="p-4 bg-muted/30 rounded-full">
                  <BookOpen className="w-8 h-8 text-muted-foreground" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-foreground mb-2">No problems generated yet</h3>
                  <p className="text-muted-foreground">Configure your plan settings and generate your daily problems!</p>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}