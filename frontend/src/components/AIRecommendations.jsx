import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Progress } from '@/components/ui/progress'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { 
  Brain, 
  Target, 
  TrendingUp, 
  Clock, 
  Star, 
  ThumbsUp, 
  ThumbsDown, 
  ExternalLink,
  Sparkles,
  Lightbulb,
  Zap,
  BookOpen,
  Award,
  RefreshCw
} from 'lucide-react'
import { 
  getPersonalizedRecommendations, 
  getTopicRecommendations,
  getCompanyRecommendations,
  submitRecommendationFeedback,
  getPriorityColor,
  formatScore 
} from '@/api/recommendations'
import { useToast } from '@/hooks/useToast'

export function AIRecommendations({ userProfile }) {
  const [recommendations, setRecommendations] = useState([])
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState('daily')
  const [feedbackSubmitted, setFeedbackSubmitted] = useState(new Set())
  const { toast } = useToast()

  useEffect(() => {
    fetchRecommendations(activeTab)
  }, [activeTab])

  const fetchRecommendations = async (type) => {
    setLoading(true)
    try {
      let data
      switch (type) {
        case 'daily':
          data = await getPersonalizedRecommendations(10, 'daily')
          break
        case 'weekly':
          data = await getPersonalizedRecommendations(20, 'weekly')
          break
        case 'skill-gap':
          data = await getPersonalizedRecommendations(15, 'skill-gap')
          break
        default:
          data = await getPersonalizedRecommendations(10, 'daily')
      }
      setRecommendations(data.recommendations || [])
    } catch (error) {
      console.error('Error fetching recommendations:', error)
      toast({
        title: "Error",
        description: "Failed to load AI recommendations",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const handleFeedback = async (recommendationId, isPositive) => {
    try {
      await submitRecommendationFeedback({
        recommendationId,
        feedback: isPositive ? 'positive' : 'negative',
        timestamp: new Date().toISOString()
      })
      
      setFeedbackSubmitted(prev => new Set([...prev, recommendationId]))
      
      toast({
        title: "Feedback Submitted",
        description: "Thank you! This helps improve our recommendations.",
      })
    } catch (error) {
      console.error('Error submitting feedback:', error)
      toast({
        title: "Error",
        description: "Failed to submit feedback",
        variant: "destructive"
      })
    }
  }

  const handleRefresh = () => {
    fetchRecommendations(activeTab)
  }

  if (loading) {
    return (
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Brain className="w-5 h-5 text-purple-600" />
              AI-Powered Recommendations
            </CardTitle>
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-purple-600"></div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="animate-pulse">
                <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                <div className="h-3 bg-gray-200 rounded w-1/2"></div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <div className="w-8 h-8 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg flex items-center justify-center">
              <Brain className="w-4 h-4 text-white" />
            </div>
            AI-Powered Recommendations
            <Badge variant="secondary" className="ml-2">
              <Sparkles className="w-3 h-3 mr-1" />
              Smart
            </Badge>
          </CardTitle>
          <Button 
            variant="outline" 
            size="sm" 
            onClick={handleRefresh}
            className="flex items-center gap-1"
          >
            <RefreshCw className="w-4 h-4" />
            Refresh
          </Button>
        </div>
        <p className="text-sm text-gray-600 dark:text-gray-400">
          Personalized problem recommendations based on your learning patterns and goals
        </p>
      </CardHeader>
      
      <CardContent>
        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
          <TabsList className="grid w-full grid-cols-3 mb-6">
            <TabsTrigger value="daily" className="flex items-center gap-2">
              <Target className="w-4 h-4" />
              Daily Focus
            </TabsTrigger>
            <TabsTrigger value="weekly" className="flex items-center gap-2">
              <TrendingUp className="w-4 h-4" />
              Weekly Plan
            </TabsTrigger>
            <TabsTrigger value="skill-gap" className="flex items-center gap-2">
              <Zap className="w-4 h-4" />
              Skill Gaps
            </TabsTrigger>
          </TabsList>

          <TabsContent value={activeTab} className="space-y-4">
            {recommendations.length === 0 ? (
              <div className="text-center py-8">
                <Lightbulb className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                <p className="text-gray-500">No recommendations available at the moment.</p>
                <Button onClick={handleRefresh} className="mt-4">
                  Generate Recommendations
                </Button>
              </div>
            ) : (
              recommendations.map((rec, index) => (
                <RecommendationCard 
                  key={rec.problem.id || index}
                  recommendation={rec}
                  index={index}
                  onFeedback={handleFeedback}
                  feedbackSubmitted={feedbackSubmitted.has(rec.problem.id)}
                />
              ))
            )}
          </TabsContent>
        </Tabs>

        {/* AI Insights Section */}
        <div className="mt-6 p-4 bg-gradient-to-r from-blue-50 to-purple-50 dark:from-blue-900/20 dark:to-purple-900/20 rounded-lg border border-blue-100 dark:border-blue-800/50">
          <div className="flex items-center gap-2 mb-3">
            <Award className="w-5 h-5 text-blue-600" />
            <h4 className="font-semibold text-gray-900 dark:text-white">AI Insights</h4>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-green-500 rounded-full"></div>
              <span className="text-gray-600 dark:text-gray-400">
                Your array skills have improved 23% this week
              </span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-yellow-500 rounded-full"></div>
              <span className="text-gray-600 dark:text-gray-400">
                Focus on dynamic programming for Google prep
              </span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
              <span className="text-gray-600 dark:text-gray-400">
                Best performance time: 9-11 AM
              </span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
              <span className="text-gray-600 dark:text-gray-400">
                Medium problems: 85% success rate
              </span>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

function RecommendationCard({ recommendation, index, onFeedback, feedbackSubmitted }) {
  const { problem, score, reasoning, priority, estimatedTime } = recommendation

  return (
    <div className="group relative p-4 bg-gray-50/50 dark:bg-gray-700/50 rounded-xl border border-gray-200 dark:border-gray-600 hover:bg-gray-100/50 dark:hover:bg-gray-600/50 transition-all duration-300 hover:shadow-md">
      {/* Priority indicator */}
      <div className="absolute top-2 right-2">
        <Badge className={`${getPriorityColor(priority)} text-xs font-medium`}>
          {priority}
        </Badge>
      </div>

      <div className="flex items-start justify-between mb-3">
        <div className="flex-1">
          <div className="flex items-center gap-2 mb-2">
            <div className="w-6 h-6 bg-gradient-to-r from-blue-500 to-purple-600 rounded-md flex items-center justify-center text-white text-xs font-bold">
              {index + 1}
            </div>
            <h3 className="font-semibold text-gray-900 dark:text-white group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
              {problem.name}
            </h3>
            <Badge variant={
              problem.difficulty === 'Easy' ? 'secondary' : 
              problem.difficulty === 'Medium' ? 'default' : 
              'destructive'
            }>
              {problem.difficulty}
            </Badge>
          </div>

          <div className="flex items-center gap-4 text-sm text-gray-600 dark:text-gray-400 mb-2">
            <span className="flex items-center gap-1">
              <BookOpen className="w-4 h-4" />
              {problem.topic}
            </span>
            <span className="flex items-center gap-1">
              <Clock className="w-4 h-4" />
              ~{estimatedTime || 30} min
            </span>
            <span className="flex items-center gap-1">
              <Star className="w-4 h-4 text-yellow-500" />
              {formatScore(score)}% match
            </span>
          </div>

          <p className="text-sm text-gray-600 dark:text-gray-400 mb-3">
            <span className="font-medium">Why recommended:</span> {reasoning}
          </p>

          {/* Progress bar for recommendation strength */}
          <div className="mb-3">
            <div className="flex items-center justify-between text-xs text-gray-500 mb-1">
              <span>Recommendation Strength</span>
              <span>{formatScore(score)}%</span>
            </div>
            <Progress value={formatScore(score)} className="h-2" />
          </div>
        </div>
      </div>

      {/* Action buttons */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => window.open(problem.url, '_blank')}
            className="flex items-center gap-1"
          >
            <ExternalLink className="w-4 h-4" />
            Solve
          </Button>
          
          {problem.companies && problem.companies.length > 0 && (
            <div className="flex items-center gap-1">
              {problem.companies.slice(0, 3).map((company, idx) => (
                <Badge key={idx} variant="outline" className="text-xs">
                  {company}
                </Badge>
              ))}
              {problem.companies.length > 3 && (
                <Badge variant="outline" className="text-xs">
                  +{problem.companies.length - 3}
                </Badge>
              )}
            </div>
          )}
        </div>

        {/* Feedback buttons */}
        {!feedbackSubmitted && (
          <div className="flex items-center gap-1">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => onFeedback(problem.id, true)}
              className="p-1 h-8 w-8 text-green-600 hover:text-green-700 hover:bg-green-50"
            >
              <ThumbsUp className="w-4 h-4" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => onFeedback(problem.id, false)}
              className="p-1 h-8 w-8 text-red-600 hover:text-red-700 hover:bg-red-50"
            >
              <ThumbsDown className="w-4 h-4" />
            </Button>
          </div>
        )}
        
        {feedbackSubmitted && (
          <Badge variant="secondary" className="text-xs">
            ✓ Feedback submitted
          </Badge>
        )}
      </div>
    </div>
  )
}
