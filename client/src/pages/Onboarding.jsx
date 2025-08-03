import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from "@/components/ui/textarea"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import {
  ChevronRight, ChevronLeft, Target, Users, Clock, Sparkles, Brain, Trophy,
  Code, Zap, Star, CheckCircle, ArrowRight, Rocket, BookOpen, TrendingUp,
  Award, Shield, Lightbulb, Heart, Coffee, Timer, Calendar, Building,
  Github, Globe, Briefcase, GraduationCap, Cpu, Database, Search,
  Smartphone, Cloud, Monitor, Music, Camera, Palette, Car, Home,
  Plane, MessageCircle, Headphones, FileText, ShoppingCart, Layers
} from "lucide-react"
import { Track2CrackLogo } from "@/components/ui/logo"
import { completeOnboarding } from "@/api/user"
import { useToast } from "@/hooks/useToast"

const DSA_TOPICS = [
  'Arrays', 'Strings', 'Linked Lists', 'Stacks', 'Queues', 'Trees', 'Graphs',
  'Hash Tables', 'Heaps', 'Dynamic Programming', 'Greedy Algorithms', 'Backtracking',
  'Binary Search', 'Sorting', 'Two Pointers', 'Sliding Window'
]

const COMPANIES = [
  'Google', 'Amazon', 'Microsoft', 'Meta', 'Apple', 'Netflix', 'Uber',
  'Airbnb', 'LinkedIn', 'Twitter', 'Spotify', 'Dropbox', 'Adobe', 'Salesforce'
]

const PLATFORMS = [
  'LeetCode', 'GeeksforGeeks', 'HackerRank', 'CodeChef', 'Codeforces', 'InterviewBit'
]

// Function to get company-specific icons
const getCompanyIcon = (company) => {
  const iconMap = {
    'Google': Search,
    'Amazon': ShoppingCart,
    'Microsoft': Monitor,
    'Meta': Users,
    'Apple': Smartphone,
    'Netflix': Camera,
    'Uber': Car,
    'Airbnb': Home,
    'LinkedIn': Briefcase,
    'Twitter': MessageCircle,
    'Spotify': Music,
    'Dropbox': Cloud,
    'Adobe': Palette,
    'Salesforce': Layers
  }
  return iconMap[company] || Building
}

// Function to get company-specific colors with enhanced vibrancy
const getCompanyColors = (company) => {
  const colorMap = {
    'Google': {
      bg: 'from-blue-500 via-green-500 to-yellow-500',
      unselected: 'from-blue-100 to-green-100 dark:from-blue-900/30 dark:to-green-900/30',
      hover: 'from-blue-200 to-green-200 dark:from-blue-800/50 dark:to-green-800/50',
      iconColor: 'text-blue-600 group-hover:text-green-600'
    },
    'Amazon': {
      bg: 'from-orange-500 via-yellow-500 to-orange-600',
      unselected: 'from-orange-100 to-yellow-100 dark:from-orange-900/30 dark:to-yellow-900/30',
      hover: 'from-orange-200 to-yellow-200 dark:from-orange-800/50 dark:to-yellow-800/50',
      iconColor: 'text-orange-600 group-hover:text-yellow-600'
    },
    'Microsoft': {
      bg: 'from-blue-600 via-cyan-500 to-blue-700',
      unselected: 'from-blue-100 to-cyan-100 dark:from-blue-900/30 dark:to-cyan-900/30',
      hover: 'from-blue-200 to-cyan-200 dark:from-blue-800/50 dark:to-cyan-800/50',
      iconColor: 'text-blue-600 group-hover:text-cyan-600'
    },
    'Meta': {
      bg: 'from-blue-500 via-purple-600 to-pink-600',
      unselected: 'from-blue-100 to-purple-100 dark:from-blue-900/30 dark:to-purple-900/30',
      hover: 'from-blue-200 to-purple-200 dark:from-blue-800/50 dark:to-purple-800/50',
      iconColor: 'text-blue-600 group-hover:text-purple-600'
    },
    'Apple': {
      bg: 'from-gray-600 via-slate-700 to-gray-800',
      unselected: 'from-gray-100 to-slate-100 dark:from-gray-900/30 dark:to-slate-900/30',
      hover: 'from-gray-200 to-slate-200 dark:from-gray-800/50 dark:to-slate-800/50',
      iconColor: 'text-gray-600 group-hover:text-slate-700'
    },
    'Netflix': {
      bg: 'from-red-600 via-red-500 to-red-700',
      unselected: 'from-red-100 to-red-100 dark:from-red-900/30 dark:to-red-900/30',
      hover: 'from-red-200 to-red-200 dark:from-red-800/50 dark:to-red-800/50',
      iconColor: 'text-red-600 group-hover:text-red-700'
    },
    'Uber': {
      bg: 'from-black via-gray-800 to-gray-900',
      unselected: 'from-gray-100 to-gray-200 dark:from-gray-900/30 dark:to-gray-800/30',
      hover: 'from-gray-200 to-gray-300 dark:from-gray-800/50 dark:to-gray-700/50',
      iconColor: 'text-gray-700 group-hover:text-gray-800'
    },
    'Airbnb': {
      bg: 'from-pink-500 via-red-500 to-rose-600',
      unselected: 'from-pink-100 to-red-100 dark:from-pink-900/30 dark:to-red-900/30',
      hover: 'from-pink-200 to-red-200 dark:from-pink-800/50 dark:to-red-800/50',
      iconColor: 'text-pink-600 group-hover:text-red-600'
    },
    'LinkedIn': {
      bg: 'from-blue-600 via-blue-700 to-blue-800',
      unselected: 'from-blue-100 to-blue-100 dark:from-blue-900/30 dark:to-blue-900/30',
      hover: 'from-blue-200 to-blue-200 dark:from-blue-800/50 dark:to-blue-800/50',
      iconColor: 'text-blue-600 group-hover:text-blue-700'
    },
    'Twitter': {
      bg: 'from-blue-400 via-sky-500 to-blue-600',
      unselected: 'from-blue-100 to-sky-100 dark:from-blue-900/30 dark:to-sky-900/30',
      hover: 'from-blue-200 to-sky-200 dark:from-blue-800/50 dark:to-sky-800/50',
      iconColor: 'text-blue-500 group-hover:text-sky-500'
    },
    'Spotify': {
      bg: 'from-green-500 via-emerald-500 to-green-600',
      unselected: 'from-green-100 to-emerald-100 dark:from-green-900/30 dark:to-emerald-900/30',
      hover: 'from-green-200 to-emerald-200 dark:from-green-800/50 dark:to-emerald-800/50',
      iconColor: 'text-green-600 group-hover:text-emerald-600'
    },
    'Dropbox': {
      bg: 'from-blue-500 via-indigo-500 to-blue-600',
      unselected: 'from-blue-100 to-indigo-100 dark:from-blue-900/30 dark:to-indigo-900/30',
      hover: 'from-blue-200 to-indigo-200 dark:from-blue-800/50 dark:to-indigo-800/50',
      iconColor: 'text-blue-600 group-hover:text-indigo-600'
    },
    'Adobe': {
      bg: 'from-red-600 via-pink-600 to-purple-600',
      unselected: 'from-red-100 to-pink-100 dark:from-red-900/30 dark:to-pink-900/30',
      hover: 'from-red-200 to-pink-200 dark:from-red-800/50 dark:to-pink-800/50',
      iconColor: 'text-red-600 group-hover:text-pink-600'
    },
    'Salesforce': {
      bg: 'from-blue-500 via-teal-500 to-cyan-600',
      unselected: 'from-blue-100 to-teal-100 dark:from-blue-900/30 dark:to-teal-900/30',
      hover: 'from-blue-200 to-teal-200 dark:from-blue-800/50 dark:to-teal-800/50',
      iconColor: 'text-blue-600 group-hover:text-teal-600'
    }
  }
  return colorMap[company] || {
    bg: 'from-purple-500 via-pink-600 to-indigo-600',
    unselected: 'from-purple-100 to-pink-100 dark:from-purple-900/30 dark:to-pink-900/30',
    hover: 'from-purple-200 to-pink-200 dark:from-purple-800/50 dark:to-pink-800/50',
    iconColor: 'text-purple-600 group-hover:text-pink-600'
  }
}

export function Onboarding() {
  const [currentStep, setCurrentStep] = useState(1)
  const [formData, setFormData] = useState({
    name: '',
    level: '',
    confidentTopics: [],
    strugglingTopics: [],
    additionalNotes: '',
    targetCompanies: [],
    customCompany: '',
    totalProblems: '',
    platform: '',
    timeAvailability: ''
  })
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const { toast } = useToast()

  const totalSteps = 3
  const progress = (currentStep / totalSteps) * 100

  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep(currentStep + 1)
    }
  }

  const handlePrevious = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1)
    }
  }

  const handleTopicToggle = (topic, type) => {
    const field = type === 'confident' ? 'confidentTopics' : 'strugglingTopics'
    const otherField = type === 'confident' ? 'strugglingTopics' : 'confidentTopics'
    
    setFormData(prev => ({
      ...prev,
      [field]: prev[field].includes(topic)
        ? prev[field].filter(t => t !== topic)
        : [...prev[field], topic],
      [otherField]: prev[otherField].filter(t => t !== topic)
    }))
  }

  const handleCompanyToggle = (company) => {
    setFormData(prev => ({
      ...prev,
      targetCompanies: prev.targetCompanies.includes(company)
        ? prev.targetCompanies.filter(c => c !== company)
        : [...prev.targetCompanies, company]
    }))
  }

  const handleSubmit = async () => {
    setLoading(true)
    try {
      console.log('Submitting onboarding data:', formData)
      await completeOnboarding(formData)
      toast({
        title: "Success!",
        description: "Onboarding completed successfully. Welcome to DSA Tracker!",
      })
      navigate('/')
    } catch (error) {
      console.error('Onboarding error:', error)
      toast({
        title: "Error",
        description: "Failed to complete onboarding. Please try again.",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const canProceed = () => {
    switch (currentStep) {
      case 1:
        return formData.name.trim() && formData.level && (formData.confidentTopics.length > 0 || formData.strugglingTopics.length > 0)
      case 2:
        return formData.targetCompanies.length > 0
      case 3:
        return formData.totalProblems && formData.platform && formData.timeAvailability
      default:
        return false
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 dark:from-gray-900 dark:via-blue-900 dark:to-indigo-900 flex items-center justify-center p-4 relative overflow-hidden">
      {/* Background decorative elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-gradient-to-br from-blue-400/20 to-purple-600/20 rounded-full blur-3xl animate-pulse"></div>
        <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-gradient-to-tr from-indigo-400/20 to-pink-600/20 rounded-full blur-3xl animate-pulse delay-1000"></div>
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-gradient-to-r from-cyan-400/10 to-blue-600/10 rounded-full blur-3xl animate-pulse delay-500"></div>
      </div>

      <Card className="w-full max-w-4xl bg-white/90 dark:bg-gray-800/90 backdrop-blur-xl border border-white/20 dark:border-gray-700/50 shadow-2xl relative z-10">
        <CardHeader className="text-center pb-8 relative overflow-hidden">
          {/* Floating icons background */}
          <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-10">
            <Code className="absolute top-4 left-8 w-6 h-6 text-blue-500 animate-bounce delay-100" />
            <Zap className="absolute top-8 right-12 w-5 h-5 text-yellow-500 animate-pulse delay-300" />
            <Star className="absolute bottom-12 left-16 w-4 h-4 text-purple-500 animate-ping delay-500" />
            <Rocket className="absolute bottom-8 right-8 w-6 h-6 text-green-500 animate-bounce delay-700" />
          </div>

          <div className="flex items-center justify-center mb-8 relative z-10">
            <Track2CrackLogo size="xl" showText={true} />
          </div>

          <div className="text-center mb-4">
            <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Welcome to Your Setup Journey</h2>
            <div className="flex items-center justify-center gap-2 text-gray-600 dark:text-gray-400 mb-2">
              <Rocket className="w-4 h-4" />
              <p className="text-base">Let's personalize your coding journey</p>
            </div>
            <div className="flex items-center justify-center gap-1">
              <Star className="w-3 h-3 text-yellow-500 fill-current" />
              <Star className="w-3 h-3 text-yellow-500 fill-current" />
              <Star className="w-3 h-3 text-yellow-500 fill-current" />
              <span className="text-xs text-gray-500 ml-1">Personalized Experience</span>
            </div>
          </div>

          <div className="space-y-4 bg-gradient-to-r from-gray-50 to-slate-50 dark:from-gray-900/50 dark:to-slate-900/50 p-6 rounded-2xl border border-gray-200/50 dark:border-gray-700/50">
            <div className="flex justify-between items-center">
              <div className="flex items-center gap-2">
                <TrendingUp className="w-4 h-4 text-blue-600" />
                <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Progress</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="flex items-center gap-1">
                  {[...Array(Math.floor(progress / 20))].map((_, i) => (
                    <Star key={i} className="w-3 h-3 text-yellow-500 fill-current" />
                  ))}
                </div>
                <span className="text-sm font-bold text-blue-600">{Math.round(progress)}%</span>
              </div>
            </div>

            <Progress value={progress} className="w-full h-3 bg-gray-200 dark:bg-gray-700" />

            <div className="flex justify-center items-center gap-3">
              {[
                { step: 1, icon: Brain, label: "Skills", color: "blue" },
                { step: 2, icon: Building, label: "Companies", color: "purple" },
                { step: 3, icon: Timer, label: "Experience", color: "green" }
              ].map(({ step, icon: Icon, label, color }) => (
                <div key={step} className="flex items-center gap-3">
                  <div className="flex flex-col items-center gap-1">
                    <div className={`relative w-12 h-12 rounded-full flex items-center justify-center text-sm font-bold transition-all duration-500 transform ${
                      step < currentStep
                        ? 'bg-green-500 text-white shadow-lg scale-110'
                        : step === currentStep
                          ? `bg-${color}-500 text-white shadow-xl scale-125 ring-4 ring-${color}-200 dark:ring-${color}-800`
                          : 'bg-gray-200 text-gray-400 dark:bg-gray-700'
                    }`}>
                      {step < currentStep ? (
                        <CheckCircle className="w-6 h-6" />
                      ) : (
                        <Icon className="w-6 h-6" />
                      )}
                      {step === currentStep && (
                        <div className="absolute inset-0 rounded-full bg-current opacity-20 animate-ping"></div>
                      )}
                    </div>
                    <span className={`text-xs font-medium transition-colors ${
                      step <= currentStep ? 'text-gray-700 dark:text-gray-300' : 'text-gray-400'
                    }`}>
                      {label}
                    </span>
                  </div>
                  {step < 3 && (
                    <div className={`w-12 h-1 rounded-full transition-all duration-500 ${
                      step < currentStep ? 'bg-green-400' : 'bg-gray-200 dark:bg-gray-700'
                    }`} />
                  )}
                </div>
              ))}
            </div>
          </div>
        </CardHeader>

        <CardContent className="p-8">
          {currentStep === 1 && (
            <div className="space-y-8 animate-in fade-in-50 duration-500">
              <div className="text-center mb-8 relative">
                {/* Floating skill icons */}
                <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-20">
                  <Code className="absolute top-0 left-1/4 w-8 h-8 text-blue-500 animate-float" />
                  <Database className="absolute top-4 right-1/4 w-6 h-6 text-green-500 animate-float delay-200" />
                  <Cpu className="absolute bottom-8 left-1/3 w-7 h-7 text-purple-500 animate-float delay-400" />
                </div>

                <div className="relative inline-flex items-center justify-center mb-6">
                  <div className="w-20 h-20 bg-gradient-to-r from-blue-500 via-indigo-600 to-purple-600 rounded-3xl flex items-center justify-center shadow-2xl transform hover:scale-110 transition-all duration-300">
                    <Brain className="w-10 h-10 text-white" />
                  </div>
                  <div className="absolute -top-2 -right-2 w-8 h-8 bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-lg animate-bounce">
                    <Lightbulb className="w-4 h-4 text-white" />
                  </div>
                  <div className="absolute -bottom-2 -left-2 w-6 h-6 bg-gradient-to-r from-green-400 to-emerald-500 rounded-full flex items-center justify-center shadow-md">
                    <Zap className="w-3 h-3 text-white" />
                  </div>
                </div>

                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-3">
                  <span className="bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                    Skill Assessment
                  </span>
                </h2>
                <p className="text-gray-600 dark:text-gray-400 max-w-lg mx-auto text-lg leading-relaxed">
                  Help us understand your current level and strengths so we can create a
                  <span className="font-semibold text-blue-600 dark:text-blue-400"> personalized learning path</span>
                </p>

                <div className="flex items-center justify-center gap-4 mt-4">
                  <div className="flex items-center gap-1 px-3 py-1 bg-blue-100 dark:bg-blue-900/30 rounded-full">
                    <BookOpen className="w-4 h-4 text-blue-600" />
                    <span className="text-sm text-blue-700 dark:text-blue-300 font-medium">Adaptive Learning</span>
                  </div>
                  <div className="flex items-center gap-1 px-3 py-1 bg-purple-100 dark:bg-purple-900/30 rounded-full">
                    <Award className="w-4 h-4 text-purple-600" />
                    <span className="text-sm text-purple-700 dark:text-purple-300 font-medium">Skill Tracking</span>
                  </div>
                </div>
              </div>

              <div className="space-y-6">
                <div className="bg-gradient-to-r from-blue-50 to-indigo-50 dark:from-blue-950/30 dark:to-indigo-950/30 p-6 rounded-xl border border-blue-100 dark:border-blue-800/50 hover:shadow-lg transition-all duration-300">
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-8 h-8 bg-blue-500 rounded-lg flex items-center justify-center">
                      <Users className="w-4 h-4 text-white" />
                    </div>
                    <Label htmlFor="name" className="text-base font-semibold text-gray-900 dark:text-white">Full Name</Label>
                  </div>
                  <p className="text-sm text-blue-600 dark:text-blue-400 mb-3">This helps us personalize your experience</p>
                  <div className="relative">
                    <Input
                      id="name"
                      placeholder="Enter your full name"
                      value={formData.name}
                      onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                      className="mt-2 h-12 text-base pl-10"
                    />
                    <GraduationCap className="absolute left-3 top-5 w-5 h-5 text-gray-400" />
                  </div>
                </div>

                <div className="bg-gradient-to-r from-purple-50 to-pink-50 dark:from-purple-950/30 dark:to-pink-950/30 p-6 rounded-xl border border-purple-100 dark:border-purple-800/50 hover:shadow-lg transition-all duration-300">
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-8 h-8 bg-purple-500 rounded-lg flex items-center justify-center">
                      <TrendingUp className="w-4 h-4 text-white" />
                    </div>
                    <Label htmlFor="level" className="text-base font-semibold text-gray-900 dark:text-white">Current Level</Label>
                  </div>
                  <p className="text-sm text-purple-600 dark:text-purple-400 mb-3">Choose the level that best describes your current skills</p>
                  <Select value={formData.level} onValueChange={(value) => setFormData(prev => ({ ...prev, level: value }))}>
                    <SelectTrigger className="mt-2 h-12 text-base">
                      <SelectValue placeholder="Select your current level" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Beginner" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                            <BookOpen className="w-4 h-4 text-green-600" />
                          </div>
                          <div>
                            <div className="font-medium">Beginner</div>
                            <div className="text-sm text-gray-500">Just starting with DSA</div>
                          </div>
                        </div>
                      </SelectItem>
                      <SelectItem value="Intermediate" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-yellow-100 rounded-full flex items-center justify-center">
                            <Code className="w-4 h-4 text-yellow-600" />
                          </div>
                          <div>
                            <div className="font-medium">Intermediate</div>
                            <div className="text-sm text-gray-500">Some experience with algorithms</div>
                          </div>
                        </div>
                      </SelectItem>
                      <SelectItem value="Advanced" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
                            <Award className="w-4 h-4 text-red-600" />
                          </div>
                          <div>
                            <div className="font-medium">Advanced</div>
                            <div className="text-sm text-gray-500">Experienced problem solver</div>
                          </div>
                        </div>
                      </SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="bg-gradient-to-r from-green-50 to-emerald-50 dark:from-green-950/30 dark:to-emerald-950/30 p-6 rounded-xl border border-green-100 dark:border-green-800/50 hover:shadow-lg transition-all duration-300">
                  <div className="flex items-center gap-3 mb-4">
                    <div className="w-10 h-10 bg-gradient-to-r from-green-500 to-emerald-600 rounded-xl flex items-center justify-center shadow-md">
                      <Trophy className="w-5 h-5 text-white" />
                    </div>
                    <div>
                      <Label className="text-base font-semibold text-gray-900 dark:text-white">Topics you're confident with</Label>
                      <div className="flex items-center gap-1 mt-1">
                        <Shield className="w-3 h-3 text-green-600" />
                        <span className="text-xs text-green-600 dark:text-green-400 font-medium">Your strengths</span>
                      </div>
                    </div>
                  </div>
                  <p className="text-sm text-gray-600 dark:text-gray-400 mb-4">Select the areas where you feel comfortable solving problems</p>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                    {DSA_TOPICS.map(topic => (
                      <div
                        key={topic}
                        className={`group relative flex items-center space-x-3 p-4 rounded-xl border-2 transition-all duration-300 cursor-pointer hover:scale-105 hover:shadow-md ${
                          formData.confidentTopics.includes(topic)
                            ? 'border-green-500 bg-gradient-to-r from-green-100 to-emerald-100 dark:from-green-900/30 dark:to-emerald-900/30 shadow-lg transform scale-105'
                            : 'border-gray-200 dark:border-gray-700 hover:border-green-300 dark:hover:border-green-600 bg-white dark:bg-gray-800/50'
                        }`}
                        onClick={() => handleTopicToggle(topic, 'confident')}
                      >
                        <Checkbox
                          id={`confident-${topic}`}
                          checked={formData.confidentTopics.includes(topic)}
                          onCheckedChange={() => handleTopicToggle(topic, 'confident')}
                          className="pointer-events-none"
                        />
                        <Label htmlFor={`confident-${topic}`} className="text-sm font-medium cursor-pointer flex-1">{topic}</Label>
                        {formData.confidentTopics.includes(topic) && (
                          <div className="absolute -top-1 -right-1 w-5 h-5 bg-gradient-to-r from-green-400 to-emerald-500 rounded-full flex items-center justify-center shadow-md animate-bounce">
                            <CheckCircle className="w-3 h-3 text-white" />
                          </div>
                        )}
                        <div className="absolute inset-0 rounded-xl bg-green-400/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="bg-gradient-to-r from-orange-50 to-red-50 dark:from-orange-950/30 dark:to-red-950/30 p-6 rounded-xl border border-orange-100 dark:border-orange-800/50 hover:shadow-lg transition-all duration-300">
                  <div className="flex items-center gap-3 mb-4">
                    <div className="w-10 h-10 bg-gradient-to-r from-orange-500 to-red-600 rounded-xl flex items-center justify-center shadow-md">
                      <Brain className="w-5 h-5 text-white" />
                    </div>
                    <div>
                      <Label className="text-base font-semibold text-gray-900 dark:text-white">Topics you're struggling with</Label>
                      <div className="flex items-center gap-1 mt-1">
                        <Search className="w-3 h-3 text-orange-600" />
                        <span className="text-xs text-orange-600 dark:text-orange-400 font-medium">Areas for improvement</span>
                      </div>
                    </div>
                  </div>
                  <p className="text-sm text-gray-600 dark:text-gray-400 mb-4">Select areas where you'd like more practice and guidance</p>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                    {DSA_TOPICS.map(topic => (
                      <div
                        key={topic}
                        className={`group relative flex items-center space-x-3 p-4 rounded-xl border-2 transition-all duration-300 cursor-pointer hover:scale-105 hover:shadow-md ${
                          formData.strugglingTopics.includes(topic)
                            ? 'border-orange-500 bg-gradient-to-r from-orange-100 to-red-100 dark:from-orange-900/30 dark:to-red-900/30 shadow-lg transform scale-105'
                            : 'border-gray-200 dark:border-gray-700 hover:border-orange-300 dark:hover:border-orange-600 bg-white dark:bg-gray-800/50'
                        }`}
                        onClick={() => handleTopicToggle(topic, 'struggling')}
                      >
                        <Checkbox
                          id={`struggling-${topic}`}
                          checked={formData.strugglingTopics.includes(topic)}
                          onCheckedChange={() => handleTopicToggle(topic, 'struggling')}
                          className="pointer-events-none"
                        />
                        <Label htmlFor={`struggling-${topic}`} className="text-sm font-medium cursor-pointer flex-1">{topic}</Label>
                        {formData.strugglingTopics.includes(topic) && (
                          <div className="absolute -top-1 -right-1 w-5 h-5 bg-gradient-to-r from-orange-400 to-red-500 rounded-full flex items-center justify-center shadow-md animate-pulse">
                            <Heart className="w-3 h-3 text-white" />
                          </div>
                        )}
                        <div className="absolute inset-0 rounded-xl bg-orange-400/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="bg-gradient-to-r from-gray-50 to-slate-50 dark:from-gray-950/30 dark:to-slate-950/30 p-6 rounded-xl border border-gray-100 dark:border-gray-800/50">
                  <Label htmlFor="notes" className="text-base font-semibold text-gray-900 dark:text-white">Additional Notes (Optional)</Label>
                  <p className="text-sm text-gray-600 dark:text-gray-400 mt-1 mb-3">Share any specific goals or areas you'd like to focus on</p>
                  <Textarea
                    id="notes"
                    placeholder="e.g., Preparing for FAANG interviews, focusing on system design, improving problem-solving speed..."
                    value={formData.additionalNotes}
                    onChange={(e) => setFormData(prev => ({ ...prev, additionalNotes: e.target.value }))}
                    className="min-h-[100px] text-base"
                  />
                </div>
              </div>
            </div>
          )}

          {currentStep === 2 && (
            <div className="space-y-8 animate-in fade-in-50 duration-500">
              <div className="text-center mb-8 relative">
                {/* Floating company icons */}
                <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-15">
                  <Building className="absolute top-2 left-1/4 w-8 h-8 text-purple-500 animate-float" />
                  <Briefcase className="absolute top-6 right-1/4 w-6 h-6 text-pink-500 animate-float delay-200" />
                  <Globe className="absolute bottom-8 left-1/3 w-7 h-7 text-indigo-500 animate-float delay-400" />
                </div>

                <div className="relative inline-flex items-center justify-center mb-6">
                  <div className="w-20 h-20 bg-gradient-to-r from-purple-500 via-pink-600 to-indigo-600 rounded-3xl flex items-center justify-center shadow-2xl transform hover:scale-110 transition-all duration-300">
                    <Building className="w-10 h-10 text-white" />
                  </div>
                  <div className="absolute -top-2 -right-2 w-8 h-8 bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-lg animate-bounce">
                    <Target className="w-4 h-4 text-white" />
                  </div>
                  <div className="absolute -bottom-2 -left-2 w-6 h-6 bg-gradient-to-r from-green-400 to-emerald-500 rounded-full flex items-center justify-center shadow-md">
                    <Briefcase className="w-3 h-3 text-white" />
                  </div>
                </div>

                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-3">
                  <span className="bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">
                    Target Companies
                  </span>
                </h2>
                <p className="text-gray-600 dark:text-gray-400 max-w-lg mx-auto text-lg leading-relaxed">
                  Select the companies you're preparing for so we can
                  <span className="font-semibold text-purple-600 dark:text-purple-400"> tailor your practice</span> accordingly
                </p>

                <div className="flex items-center justify-center gap-4 mt-4">
                  <div className="flex items-center gap-1 px-3 py-1 bg-purple-100 dark:bg-purple-900/30 rounded-full">
                    <Target className="w-4 h-4 text-purple-600" />
                    <span className="text-sm text-purple-700 dark:text-purple-300 font-medium">Focused Prep</span>
                  </div>
                  <div className="flex items-center gap-1 px-3 py-1 bg-pink-100 dark:bg-pink-900/30 rounded-full">
                    <TrendingUp className="w-4 h-4 text-pink-600" />
                    <span className="text-sm text-pink-700 dark:text-pink-300 font-medium">Career Goals</span>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {COMPANIES.map((company, index) => (
                  <div
                    key={company}
                    className={`group relative p-5 rounded-2xl border-2 cursor-pointer transition-all duration-300 hover:scale-105 hover:shadow-xl hover:-translate-y-1 ${
                      formData.targetCompanies.includes(company)
                        ? `border-transparent bg-gradient-to-br ${getCompanyColors(company).bg} shadow-xl transform scale-105 -translate-y-1`
                        : `border-gray-200 dark:border-gray-700 hover:border-transparent bg-gradient-to-br ${getCompanyColors(company).unselected} hover:bg-gradient-to-br hover:${getCompanyColors(company).hover}`
                    }`}
                    onClick={() => handleCompanyToggle(company)}
                    style={{ animationDelay: `${index * 50}ms` }}
                  >
                    <div className="text-center relative">
                      <div className="mb-3">
                        {(() => {
                          const IconComponent = getCompanyIcon(company);
                          const colors = getCompanyColors(company);
                          return (
                            <div className={`w-14 h-14 mx-auto rounded-2xl flex items-center justify-center transition-all duration-300 shadow-lg ${
                              formData.targetCompanies.includes(company)
                                ? 'bg-white dark:bg-gray-900'
                                : 'bg-white dark:bg-gray-800 group-hover:bg-white dark:group-hover:bg-gray-800'
                            }`}>
                              <IconComponent className={`w-7 h-7 transition-all duration-300 ${
                                formData.targetCompanies.includes(company)
                                  ? `${colors.iconColor.split(' ')[0]} drop-shadow-sm`
                                  : colors.iconColor
                              }`} />
                            </div>
                          );
                        })()}
                      </div>
                      <div className={`font-bold text-sm transition-colors ${
                        formData.targetCompanies.includes(company)
                          ? 'text-white drop-shadow-sm'
                          : 'text-gray-700 dark:text-gray-300 group-hover:text-gray-800 dark:group-hover:text-gray-200'
                      }`}>
                        {company}
                      </div>
                      {formData.targetCompanies.includes(company) && (
                        <div className="mt-2">
                          <span className="inline-flex items-center gap-1 px-3 py-1 bg-white dark:bg-gray-900 text-gray-700 dark:text-gray-300 text-xs font-medium rounded-full shadow-md">
                            <CheckCircle className="w-3 h-3 text-green-600" />
                            Selected
                          </span>
                        </div>
                      )}
                      {formData.targetCompanies.includes(company) && (
                        <>
                          <div className={`absolute -top-2 -right-2 w-8 h-8 bg-gradient-to-r from-green-400 via-emerald-500 to-green-600 rounded-full flex items-center justify-center shadow-xl animate-bounce`}>
                            <CheckCircle className="w-5 h-5 text-white drop-shadow-sm" />
                          </div>
                          <div className="absolute -top-1 -left-1 w-4 h-4 bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-md animate-pulse">
                            <Sparkles className="w-2 h-2 text-white" />
                          </div>
                          <div className={`absolute inset-0 rounded-2xl bg-gradient-to-r ${getCompanyColors(company).bg} opacity-20 animate-pulse`}></div>
                          <div className="absolute inset-0 rounded-2xl bg-white/10"></div>
                        </>
                      )}
                      <div className={`absolute inset-0 rounded-2xl bg-gradient-to-r ${getCompanyColors(company).bg} opacity-0 group-hover:opacity-10 transition-all duration-300 pointer-events-none`}></div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="bg-gradient-to-r from-indigo-50 to-blue-50 dark:from-indigo-950/30 dark:to-blue-950/30 p-6 rounded-xl border border-indigo-100 dark:border-indigo-800/50">
                <Label htmlFor="customCompany" className="text-base font-semibold text-gray-900 dark:text-white">Add Custom Company</Label>
                <p className="text-sm text-gray-600 dark:text-gray-400 mt-1 mb-3">Don't see your target company? Add it here!</p>
                <Input
                  id="customCompany"
                  placeholder="Enter company name and press Enter"
                  value={formData.customCompany}
                  onChange={(e) => setFormData(prev => ({ ...prev, customCompany: e.target.value }))}
                  onKeyPress={(e) => {
                    if (e.key === 'Enter' && formData.customCompany.trim()) {
                      handleCompanyToggle(formData.customCompany.trim())
                      setFormData(prev => ({ ...prev, customCompany: '' }))
                    }
                  }}
                  className="h-12 text-base"
                />
              </div>
            </div>
          )}

          {currentStep === 3 && (
            <div className="space-y-8 animate-in fade-in-50 duration-500">
              <div className="text-center mb-8 relative">
                {/* Floating experience icons */}
                <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-15">
                  <Timer className="absolute top-2 left-1/4 w-8 h-8 text-emerald-500 animate-float" />
                  <Calendar className="absolute top-6 right-1/4 w-6 h-6 text-teal-500 animate-float delay-200" />
                  <Coffee className="absolute bottom-8 left-1/3 w-7 h-7 text-green-500 animate-float delay-400" />
                </div>

                <div className="relative inline-flex items-center justify-center mb-6">
                  <div className="w-20 h-20 bg-gradient-to-r from-emerald-500 via-teal-600 to-green-600 rounded-3xl flex items-center justify-center shadow-2xl transform hover:scale-110 transition-all duration-300">
                    <Clock className="w-10 h-10 text-white" />
                  </div>
                  <div className="absolute -top-2 -right-2 w-8 h-8 bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-lg animate-bounce">
                    <Timer className="w-4 h-4 text-white" />
                  </div>
                  <div className="absolute -bottom-2 -left-2 w-6 h-6 bg-gradient-to-r from-blue-400 to-indigo-500 rounded-full flex items-center justify-center shadow-md">
                    <TrendingUp className="w-3 h-3 text-white" />
                  </div>
                </div>

                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-3">
                  <span className="bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                    Experience Assessment
                  </span>
                </h2>
                <p className="text-gray-600 dark:text-gray-400 max-w-lg mx-auto text-lg leading-relaxed">
                  Help us understand your current progress and
                  <span className="font-semibold text-emerald-600 dark:text-emerald-400"> available time</span> for practice
                </p>

                <div className="flex items-center justify-center gap-4 mt-4">
                  <div className="flex items-center gap-1 px-3 py-1 bg-emerald-100 dark:bg-emerald-900/30 rounded-full">
                    <Clock className="w-4 h-4 text-emerald-600" />
                    <span className="text-sm text-emerald-700 dark:text-emerald-300 font-medium">Time Management</span>
                  </div>
                  <div className="flex items-center gap-1 px-3 py-1 bg-teal-100 dark:bg-teal-900/30 rounded-full">
                    <Award className="w-4 h-4 text-teal-600" />
                    <span className="text-sm text-teal-700 dark:text-teal-300 font-medium">Progress Tracking</span>
                  </div>
                </div>
              </div>

              <div className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="bg-gradient-to-r from-blue-50 to-cyan-50 dark:from-blue-950/30 dark:to-cyan-950/30 p-6 rounded-xl border border-blue-100 dark:border-blue-800/50 hover:shadow-lg transition-all duration-300">
                    <div className="flex items-center gap-2 mb-3">
                      <div className="w-8 h-8 bg-blue-500 rounded-lg flex items-center justify-center">
                        <Trophy className="w-4 h-4 text-white" />
                      </div>
                      <Label htmlFor="totalProblems" className="text-base font-semibold text-gray-900 dark:text-white">Total Problems Solved</Label>
                    </div>
                    <p className="text-sm text-blue-600 dark:text-blue-400 mb-3">Approximate number across all platforms</p>
                    <div className="relative">
                      <Input
                        id="totalProblems"
                        type="number"
                        placeholder="e.g., 150"
                        value={formData.totalProblems}
                        onChange={(e) => setFormData(prev => ({ ...prev, totalProblems: e.target.value }))}
                        className="h-12 text-base pl-10"
                      />
                      <Award className="absolute left-3 top-4 w-5 h-5 text-gray-400" />
                    </div>
                  </div>

                  <div className="bg-gradient-to-r from-purple-50 to-violet-50 dark:from-purple-950/30 dark:to-violet-950/30 p-6 rounded-xl border border-purple-100 dark:border-purple-800/50 hover:shadow-lg transition-all duration-300">
                    <div className="flex items-center gap-2 mb-3">
                      <div className="w-8 h-8 bg-purple-500 rounded-lg flex items-center justify-center">
                        <Code className="w-4 h-4 text-white" />
                      </div>
                      <Label htmlFor="platform" className="text-base font-semibold text-gray-900 dark:text-white">Primary Platform</Label>
                    </div>
                    <p className="text-sm text-purple-600 dark:text-purple-400 mb-3">Where do you practice most often?</p>
                    <Select value={formData.platform} onValueChange={(value) => setFormData(prev => ({ ...prev, platform: value }))}>
                      <SelectTrigger className="h-12 text-base">
                        <SelectValue placeholder="Select platform" />
                      </SelectTrigger>
                      <SelectContent>
                        {PLATFORMS.map(platform => (
                          <SelectItem key={platform} value={platform} className="text-base p-3">
                            <div className="flex items-center gap-2">
                              <Globe className="w-4 h-4 text-purple-600" />
                              {platform}
                            </div>
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                <div className="bg-gradient-to-r from-emerald-50 to-green-50 dark:from-emerald-950/30 dark:to-green-950/30 p-6 rounded-xl border border-emerald-100 dark:border-emerald-800/50 hover:shadow-lg transition-all duration-300">
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-8 h-8 bg-emerald-500 rounded-lg flex items-center justify-center">
                      <Clock className="w-4 h-4 text-white" />
                    </div>
                    <Label htmlFor="timeAvailability" className="text-base font-semibold text-gray-900 dark:text-white">Daily Time Availability</Label>
                  </div>
                  <p className="text-sm text-emerald-600 dark:text-emerald-400 mb-3">How much time can you dedicate to practice daily?</p>
                  <Select value={formData.timeAvailability} onValueChange={(value) => setFormData(prev => ({ ...prev, timeAvailability: value }))}>
                    <SelectTrigger className="h-12 text-base">
                      <SelectValue placeholder="Select your daily time commitment" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="30 minutes" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-yellow-100 rounded-full flex items-center justify-center">
                            <Coffee className="w-4 h-4 text-yellow-600" />
                          </div>
                          <div>
                            <div className="font-medium">30 minutes</div>
                            <div className="text-sm text-gray-500">Quick daily practice</div>
                          </div>
                        </div>
                      </SelectItem>
                      <SelectItem value="1 hour" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                            <Timer className="w-4 h-4 text-green-600" />
                          </div>
                          <div>
                            <div className="font-medium">1 hour</div>
                            <div className="text-sm text-gray-500">Steady progress</div>
                          </div>
                        </div>
                      </SelectItem>
                      <SelectItem value="2 hours" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                            <Clock className="w-4 h-4 text-blue-600" />
                          </div>
                          <div>
                            <div className="font-medium">2 hours</div>
                            <div className="text-sm text-gray-500">Intensive practice</div>
                          </div>
                        </div>
                      </SelectItem>
                      <SelectItem value="3+ hours" className="text-base p-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center">
                            <Rocket className="w-4 h-4 text-purple-600" />
                          </div>
                          <div>
                            <div className="font-medium">3+ hours</div>
                            <div className="text-sm text-gray-500">Full commitment</div>
                          </div>
                        </div>
                      </SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </div>
          )}

          <div className="flex justify-between items-center mt-12 pt-6 border-t border-gray-200 dark:border-gray-700">
            <Button
              variant="outline"
              onClick={handlePrevious}
              disabled={currentStep === 1}
              className="flex items-center gap-2 h-12 px-6 text-base font-medium transition-all duration-200 hover:scale-105 disabled:opacity-50 disabled:hover:scale-100"
            >
              <ChevronLeft className="w-5 h-5" />
              Previous
            </Button>

            <div className="flex items-center gap-2 text-sm text-gray-500 dark:text-gray-400">
              <span>Step {currentStep} of {totalSteps}</span>
            </div>

            {currentStep < totalSteps ? (
              <Button
                onClick={handleNext}
                disabled={!canProceed()}
                className="flex items-center gap-2 h-12 px-6 text-base font-medium bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 transition-all duration-200 hover:scale-105 disabled:opacity-50 disabled:hover:scale-100 shadow-lg"
              >
                Next
                <ChevronRight className="w-5 h-5" />
              </Button>
            ) : (
              <Button
                onClick={handleSubmit}
                disabled={!canProceed() || loading}
                className="flex items-center gap-2 h-12 px-6 text-base font-medium bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 transition-all duration-200 hover:scale-105 disabled:opacity-50 disabled:hover:scale-100 shadow-lg"
              >
                {loading ? (
                  <>
                    <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                    Completing...
                  </>
                ) : (
                  <>
                    Complete Setup
                    <Sparkles className="w-5 h-5" />
                  </>
                )}
              </Button>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}