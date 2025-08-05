import { NavLink } from "react-router-dom"
import {
  LayoutDashboard,
  Target,
  BookOpen,
  User,
  Calendar,
  BarChart3,
  StickyNote,
  Trophy,
  Settings
} from "lucide-react"
import { Track2CrackLogo } from "./ui/logo"
import { cn } from "@/lib/utils"

const navigation = [
  { name: 'Dashboard', href: '/', icon: LayoutDashboard },
  { name: 'Topics', href: '/topics', icon: BookOpen },
  { name: 'Daily Plan', href: '/daily-plan', icon: Calendar },
  { name: 'Analytics', href: '/analytics', icon: BarChart3 },
  { name: 'Notes', href: '/notes', icon: StickyNote },
  { name: 'Profile', href: '/profile', icon: User },
]

export function Sidebar() {
  return (
    <div className="fixed left-0 top-16 z-40 w-64 h-[calc(100vh-4rem)] bg-white/80 dark:bg-gray-900/80 backdrop-blur-lg border-r border-gray-200/50 dark:border-gray-700/50">
      <div className="flex flex-col h-full p-4">
        <div className="mb-8 px-2">
          <Track2CrackLogo size="sm" showText={true} />
        </div>
        
        <nav className="flex-1 space-y-2">
          {navigation.map((item) => (
            <NavLink
              key={item.name}
              to={item.href}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all duration-200",
                  "hover:bg-gradient-to-r hover:from-blue-50 hover:to-purple-50 dark:hover:from-blue-900/20 dark:hover:to-purple-900/20",
                  "hover:shadow-sm hover:scale-[1.02]",
                  isActive
                    ? "bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg"
                    : "text-gray-700 dark:text-gray-300"
                )
              }
            >
              <item.icon className="w-5 h-5" />
              {item.name}
            </NavLink>
          ))}
        </nav>

        <div className="pt-4 border-t border-gray-200/50 dark:border-gray-700/50">
          <div className="flex items-center gap-3 px-3 py-2 text-sm text-gray-600 dark:text-gray-400">
            <Trophy className="w-4 h-4" />
            <span>7 day streak! 🔥</span>
          </div>
        </div>
      </div>
    </div>
  )
}