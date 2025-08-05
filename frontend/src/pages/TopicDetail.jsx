import { useEffect, useState, useMemo, useCallback } from "react"
import { useParams, Link } from "react-router-dom"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Checkbox } from "@/components/ui/checkbox"
import {
  ArrowLeft,
  ExternalLink,
  Search,
  Filter,
  CheckCircle,
  Circle,
  Clock,
  Building,
  TrendingUp
} from "lucide-react"
import { getTopicDetails } from "@/api/topics"
import { updateProblemStatus } from "@/api/problems"
import { useToast } from "@/hooks/useToast"

// Move functions outside component to prevent recreation on every render
const getDifficultyColor = (difficulty) => {
  switch (difficulty) {
    case 'Easy': return 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400'
    case 'Medium': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-400'
    case 'Hard': return 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400'
    default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400'
  }
}

const getStatusIcon = (status) => {
  switch (status) {
    case 'Solved': return <CheckCircle className="w-4 h-4 text-green-600" />
    case 'Attempted': return <Clock className="w-4 h-4 text-yellow-600" />
    case 'In Progress': return <Clock className="w-4 h-4 text-yellow-600" />
    case 'Not Started': return <Circle className="w-4 h-4 text-gray-400" />
    default: return <Circle className="w-4 h-4 text-gray-400" />
  }
}

export function TopicDetail() {
  const { topicId } = useParams()
  const [topic, setTopic] = useState(null)
  const [problems, setProblems] = useState([])

  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [difficultyFilter, setDifficultyFilter] = useState('all')
  const [statusFilter, setStatusFilter] = useState('all')
  const [selectedProblems, setSelectedProblems] = useState([])
  const { toast } = useToast()

  // All useMemo hooks must be called before any conditional returns
  // Use useMemo instead of useEffect for filtering to prevent unnecessary re-renders
  const filteredProblems = useMemo(() => {
    // Safety check: only filter if problems array exists and has valid data
    if (!problems || problems.length === 0) {
      return []
    }

    let filtered = problems.filter(problem => {
      // Safety check: ensure problem has required fields
      if (!problem || !problem.name) {
        return false
      }
      return problem.name.toLowerCase().includes(searchTerm.toLowerCase())
    })

    if (difficultyFilter !== 'all') {
      filtered = filtered.filter(problem => problem.difficulty === difficultyFilter)
    }

    if (statusFilter !== 'all') {
      filtered = filtered.filter(problem => {
        if (statusFilter === 'solved') return problem.status === 'Solved'
        if (statusFilter === 'attempted') return problem.status === 'Attempted' || problem.status === 'In Progress'
        if (statusFilter === 'not-started') return problem.status === 'Not Started'
        return true
      })
    }

    // Pre-calculate expensive operations to prevent re-rendering
    return filtered.map(problem => ({
      ...problem,
      companiesDisplay: problem.companies ? problem.companies.slice(0, 3).join(', ') : '',
      companiesCount: problem.companies ? problem.companies.length : 0,
      hasMoreCompanies: problem.companies ? problem.companies.length > 3 : false,
      moreCompaniesCount: problem.companies ? Math.max(0, problem.companies.length - 3) : 0
    }))
  }, [problems, searchTerm, difficultyFilter, statusFilter])

  // Use useMemo for expensive calculations
  const { solvedCount, totalCount, percentage } = useMemo(() => {
    const solved = problems.filter(p => p.status === 'Solved').length;
    const total = problems.length;
    const percent = total > 0 ? Math.round((solved / total) * 100) : 0;

    return {
      solvedCount: solved,
      totalCount: total,
      percentage: percent
    };
  }, [problems])

  useEffect(() => {
    const fetchTopicDetails = async () => {
      if (!topicId) return
      try {
        setLoading(true)


        const response = await getTopicDetails(topicId)
        const topicData = response.topic

        // Safety checks for the received data
        if (topicData && topicData.problems && Array.isArray(topicData.problems)) {
          setTopic(topicData)
          setProblems(topicData.problems)

        } else {
          console.error('Invalid topic data received:', topicData)
          toast({
            title: "Error",
            description: "Invalid topic data received",
            variant: "destructive"
          })
        }
      } catch (error) {
        console.error('Error fetching topic details:', error)
        toast({
          title: "Error",
          description: "Failed to load topic details",
          variant: "destructive"
        })
        setLoading(false)
      }
      setLoading(false)
    }
    fetchTopicDetails()
  }, [topicId])

  const handleStatusChange = useCallback(async (problemId, newStatus) => {
    try {
      console.log('🔄 Updating problem status:', { problemId, newStatus })

      const response = await updateProblemStatus(problemId, newStatus)
      console.log('✅ API Response:', response)

      setProblems(prev => {
        const updated = prev.map(problem =>
          problem._id === problemId ? { ...problem, status: newStatus } : problem
        )
        console.log('🔄 Updated problem in state:', updated.find(p => p._id === problemId))
        return updated
      })

      toast({
        title: "Success",
        description: `Problem marked as ${newStatus}`,
      })
    } catch (error) {
      console.error('❌ Error updating problem status:', error)
      console.error('❌ Error details:', error.response?.data || error.message)
      toast({
        title: "Error",
        description: `Failed to update problem status: ${error.message}`,
        variant: "destructive"
      })
    }
  }, [toast])

  const handleBulkStatusChange = useCallback(async (status) => {
    if (selectedProblems.length === 0) {
      toast({
        title: "No problems selected",
        description: "Please select problems to update",
        variant: "destructive"
      })
      return
    }

    try {
      console.log('Bulk updating problems:', selectedProblems, status)
      await Promise.all(
        selectedProblems.map(problemId => updateProblemStatus(problemId, status))
      )

      setProblems(prev => prev.map(problem =>
        selectedProblems.includes(problem._id) ? { ...problem, status } : problem
      ))

      setSelectedProblems([])
      toast({
        title: "Success",
        description: `${selectedProblems.length} problems marked as ${status.toLowerCase()}`,
      })
    } catch (error) {
      console.error('Error bulk updating problems:', error)
      toast({
        title: "Error",
        description: "Failed to update problems",
        variant: "destructive"
      })
    }
  }, [selectedProblems, toast])

  const toggleProblemSelection = useCallback((problemId) => {
    setSelectedProblems(prev =>
      prev.includes(problemId)
        ? prev.filter(id => id !== problemId)
        : [...prev, problemId]
    )
  }, [])



  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  if (!topic) {
    return (
      <div className="text-center py-12">
        <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">Topic not found</h3>
        <Link to="/topics">
          <Button variant="outline">Back to Topics</Button>
        </Link>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-in fade-in-50 duration-500">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Link to="/topics">
          <Button variant="outline" size="sm">
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Topics
          </Button>
        </Link>
        <div className="flex-1">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">{topic.name}</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">
            {solvedCount}/{totalCount} problems solved ({percentage}%)
          </p>
        </div>
      </div>

      {/* Filters and Bulk Actions */}
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardContent className="p-6">
          <div className="flex flex-col lg:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <Input
                placeholder="Search problems..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={difficultyFilter} onValueChange={setDifficultyFilter}>
              <SelectTrigger className="w-full lg:w-40">
                <SelectValue placeholder="Difficulty" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Levels</SelectItem>
                <SelectItem value="Easy">Easy</SelectItem>
                <SelectItem value="Medium">Medium</SelectItem>
                <SelectItem value="Hard">Hard</SelectItem>
              </SelectContent>
            </Select>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full lg:w-40">
                <SelectValue placeholder="Status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Status</SelectItem>
                <SelectItem value="solved">Solved</SelectItem>
                <SelectItem value="attempted">Attempted</SelectItem>
                <SelectItem value="not-started">Not Started</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {selectedProblems.length > 0 && (
            <div className="flex items-center gap-2 mt-4 pt-4 border-t">
              <span className="text-sm text-gray-600 dark:text-gray-400">
                {selectedProblems.length} selected
              </span>
              <Button
                size="sm"
                variant="outline"
                onClick={() => handleBulkStatusChange('Solved')}
                className="text-green-600 hover:text-green-700"
              >
                Mark as Solved
              </Button>
              <Button
                size="sm"
                variant="outline"
                onClick={() => handleBulkStatusChange('Attempted')}
                className="text-yellow-600 hover:text-yellow-700"
              >
                Mark as Attempted
              </Button>
              <Button
                size="sm"
                variant="outline"
                onClick={() => setSelectedProblems([])}
              >
                Clear Selection
              </Button>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Problems List */}
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardContent className="p-0">
          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {filteredProblems.map((problem) => (
              <div key={problem._id} className="p-4 hover:bg-gray-50/50 dark:hover:bg-gray-800/50 transition-colors">
                <div className="flex items-center gap-4">
                  <Checkbox
                    checked={selectedProblems.includes(problem._id)}
                    onCheckedChange={() => toggleProblemSelection(problem._id)}
                  />
                  
                  <div className="flex items-center gap-2">
                    {getStatusIcon(problem.status)}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <h3 className="font-medium text-gray-900 dark:text-white truncate">
                        {problem.name}
                      </h3>
                      <Badge className={getDifficultyColor(problem.difficulty)}>
                        {problem.difficulty}
                      </Badge>
                      <div className="flex items-center gap-1 text-sm text-gray-500">
                        <TrendingUp className="w-3 h-3" />
                        {problem.frequency}%
                      </div>
                    </div>
                    
                    <div className="flex items-center gap-4 text-sm text-gray-600 dark:text-gray-400">
                      <div className="flex items-center gap-1">
                        <Building className="w-3 h-3" />
                        <span>{problem.companiesDisplay}</span>
                        {problem.hasMoreCompanies && (
                          <span>+{problem.moreCompaniesCount} more</span>
                        )}
                      </div>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <Select
                      value={problem.status}
                      onValueChange={(value) => handleStatusChange(problem._id, value)}
                    >
                      <SelectTrigger className="w-32">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="Not Started">Not Started</SelectItem>
                        <SelectItem value="In Progress">In Progress</SelectItem>
                        <SelectItem value="Attempted">Attempted</SelectItem>
                        <SelectItem value="Solved">Solved</SelectItem>
                      </SelectContent>
                    </Select>
                    
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => window.open(problem.url, '_blank')}
                    >
                      <ExternalLink className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {filteredProblems.length === 0 && (
        <div className="text-center py-12">
          <Filter className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">No problems found</h3>
          <p className="text-gray-600 dark:text-gray-400">Try adjusting your search or filter criteria</p>
        </div>
      )}
    </div>
  )
}