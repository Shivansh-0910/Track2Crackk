import { Target, Zap, Code, Trophy } from "lucide-react"

export function Track2CrackLogo({ size = "md", showText = true, className = "" }) {
  const sizeClasses = {
    sm: "w-8 h-8",
    md: "w-12 h-12", 
    lg: "w-16 h-16",
    xl: "w-20 h-20"
  }

  const textSizeClasses = {
    sm: "text-lg",
    md: "text-2xl",
    lg: "text-3xl", 
    xl: "text-4xl"
  }

  return (
    <div className={`flex items-center gap-3 ${className}`}>
      {/* Logo Icon */}
      <div className="relative">
        {/* Main container with gradient background */}
        <div className={`${sizeClasses[size]} bg-gradient-to-br from-blue-500 via-purple-600 to-indigo-700 rounded-2xl flex items-center justify-center shadow-2xl transform transition-all duration-300 hover:scale-110 hover:rotate-3`}>
          {/* Primary icon - Target for tracking */}
          <Target className={`${size === 'sm' ? 'w-4 h-4' : size === 'md' ? 'w-6 h-6' : size === 'lg' ? 'w-8 h-8' : 'w-10 h-10'} text-white`} />
        </div>
        
        {/* Accent badges */}
        <div className={`absolute -top-1 -right-1 ${size === 'sm' ? 'w-3 h-3' : size === 'md' ? 'w-4 h-4' : 'w-5 h-5'} bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-lg animate-pulse`}>
          <Zap className={`${size === 'sm' ? 'w-1.5 h-1.5' : size === 'md' ? 'w-2 h-2' : 'w-2.5 h-2.5'} text-white`} />
        </div>
        
        <div className={`absolute -bottom-1 -left-1 ${size === 'sm' ? 'w-3 h-3' : size === 'md' ? 'w-4 h-4' : 'w-5 h-5'} bg-gradient-to-r from-green-400 to-emerald-500 rounded-full flex items-center justify-center shadow-md`}>
          <Code className={`${size === 'sm' ? 'w-1.5 h-1.5' : size === 'md' ? 'w-2 h-2' : 'w-2.5 h-2.5'} text-white`} />
        </div>

        {size !== 'sm' && (
          <div className={`absolute -top-1 -left-1 ${size === 'md' ? 'w-3 h-3' : 'w-4 h-4'} bg-gradient-to-r from-pink-400 to-rose-500 rounded-full flex items-center justify-center shadow-md animate-bounce`}>
            <Trophy className={`${size === 'md' ? 'w-1.5 h-1.5' : 'w-2 h-2'} text-white`} />
          </div>
        )}
      </div>

      {/* Logo Text */}
      {showText && (
        <div className="flex flex-col">
          <h1 className={`${textSizeClasses[size]} font-bold bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-600 bg-clip-text text-transparent leading-tight`}>
            Track2Crack
          </h1>
          {size !== 'sm' && (
            <p className={`${size === 'md' ? 'text-xs' : size === 'lg' ? 'text-sm' : 'text-base'} text-gray-500 dark:text-gray-400 font-medium -mt-1`}>
              Master DSA & Crack Interviews
            </p>
          )}
        </div>
      )}
    </div>
  )
}

export function Track2CrackIcon({ size = "md", className = "" }) {
  return <Track2CrackLogo size={size} showText={false} className={className} />
}

// Animated version for loading states
export function Track2CrackLogoAnimated({ size = "md", showText = true, className = "" }) {
  const sizeClasses = {
    sm: "w-8 h-8",
    md: "w-12 h-12", 
    lg: "w-16 h-16",
    xl: "w-20 h-20"
  }

  const textSizeClasses = {
    sm: "text-lg",
    md: "text-2xl",
    lg: "text-3xl", 
    xl: "text-4xl"
  }

  return (
    <div className={`flex items-center gap-3 ${className}`}>
      {/* Animated Logo Icon */}
      <div className="relative">
        {/* Main container with gradient background and pulse animation */}
        <div className={`${sizeClasses[size]} bg-gradient-to-br from-blue-500 via-purple-600 to-indigo-700 rounded-2xl flex items-center justify-center shadow-2xl animate-pulse`}>
          <Target className={`${size === 'sm' ? 'w-4 h-4' : size === 'md' ? 'w-6 h-6' : size === 'lg' ? 'w-8 h-8' : 'w-10 h-10'} text-white animate-spin`} style={{ animationDuration: '3s' }} />
        </div>
        
        {/* Animated accent badges */}
        <div className={`absolute -top-1 -right-1 ${size === 'sm' ? 'w-3 h-3' : size === 'md' ? 'w-4 h-4' : 'w-5 h-5'} bg-gradient-to-r from-yellow-400 to-orange-500 rounded-full flex items-center justify-center shadow-lg animate-bounce`}>
          <Zap className={`${size === 'sm' ? 'w-1.5 h-1.5' : size === 'md' ? 'w-2 h-2' : 'w-2.5 h-2.5'} text-white`} />
        </div>
        
        <div className={`absolute -bottom-1 -left-1 ${size === 'sm' ? 'w-3 h-3' : size === 'md' ? 'w-4 h-4' : 'w-5 h-5'} bg-gradient-to-r from-green-400 to-emerald-500 rounded-full flex items-center justify-center shadow-md animate-ping`}>
          <Code className={`${size === 'sm' ? 'w-1.5 h-1.5' : size === 'md' ? 'w-2 h-2' : 'w-2.5 h-2.5'} text-white`} />
        </div>
      </div>

      {/* Logo Text with typing animation */}
      {showText && (
        <div className="flex flex-col">
          <h1 className={`${textSizeClasses[size]} font-bold bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-600 bg-clip-text text-transparent leading-tight animate-pulse`}>
            Track2Crack
          </h1>
          {size !== 'sm' && (
            <p className={`${size === 'md' ? 'text-xs' : size === 'lg' ? 'text-sm' : 'text-base'} text-gray-500 dark:text-gray-400 font-medium -mt-1 animate-pulse`}>
              Master DSA & Crack Interviews
            </p>
          )}
        </div>
      )}
    </div>
  )
}
