import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Database, RefreshCw, Trash2, BarChart3 } from 'lucide-react'
import { useToast } from '@/hooks/useToast'
import api from '@/api/api'

/**
 * Admin-only component for database management
 * This should only be accessible to administrators
 */
export function DataSeeder() {
  const [loading, setLoading] = useState(false)
  const [stats, setStats] = useState(null)
  const { toast } = useToast()

  const seedData = async () => {
    setLoading(true)
    try {
      const response = await api.post('/api/admin/data/seed')
      toast({
        title: "Success",
        description: response.data.message,
      })
      fetchStats()
    } catch (error) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to seed data",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const resetData = async () => {
    setLoading(true)
    try {
      const response = await api.delete('/api/admin/data/reset')
      toast({
        title: "Success",
        description: response.data.message,
      })
      setStats(null)
    } catch (error) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to reset data",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const fetchStats = async () => {
    try {
      const response = await api.get('/api/admin/data/stats')
      setStats(response.data)
    } catch (error) {
      console.error('Error fetching stats:', error)
    }
  }

  return (
    <Card className="w-full max-w-2xl">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Database className="w-5 h-5" />
          Database Management
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex gap-3">
          <Button 
            onClick={seedData} 
            disabled={loading}
            className="flex items-center gap-2"
          >
            <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
            Seed Sample Data
          </Button>
          
          <Button 
            variant="outline" 
            onClick={fetchStats}
            className="flex items-center gap-2"
          >
            <BarChart3 className="w-4 h-4" />
            Check Stats
          </Button>
          
          <Button 
            variant="destructive" 
            onClick={resetData}
            disabled={loading}
            className="flex items-center gap-2"
          >
            <Trash2 className="w-4 h-4" />
            Reset Data
          </Button>
        </div>

        {stats && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center">
                <div className="text-2xl font-bold text-blue-600">{stats.totalProblems}</div>
                <div className="text-sm text-gray-600">Total Problems</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-green-600">{stats.easyProblems}</div>
                <div className="text-sm text-gray-600">Easy</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-yellow-600">{stats.mediumProblems}</div>
                <div className="text-sm text-gray-600">Medium</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-red-600">{stats.hardProblems}</div>
                <div className="text-sm text-gray-600">Hard</div>
              </div>
            </div>

            {stats.topicBreakdown && (
              <div>
                <h4 className="font-semibold mb-2">Topics Breakdown:</h4>
                <div className="flex flex-wrap gap-2">
                  {Object.entries(stats.topicBreakdown).map(([topic, count]) => (
                    <Badge key={topic} variant="outline">
                      {topic}: {count}
                    </Badge>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  )
}
