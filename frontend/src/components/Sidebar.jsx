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
  Settings,
  Map
} from "lucide-react"
import { Track2CrackLogo } from "./ui/logo"
import { cn } from "@/lib/utils"

const navigation = [
  { name: 'Dashboard', href: '/', icon: LayoutDashboard },
  { name: 'Topics', href: '/topics', icon: BookOpen },
  { name: 'Daily Plan', href: '/daily-plan', icon: Calendar },
  { name: 'Roadmap', href: '/roadmap', icon: Map },
  { name: 'Analytics', href: '/analytics', icon: BarChart3 },
  { name: 'Notes', href: '/notes', icon: StickyNote },
  { name: 'Profile', href: '/profile', icon: User },
]

export function Sidebar() {
  return (
    <div className="fixed left-0 top-16 z-40 w-64 h-[calc(100vh-4rem)] card-glass border-r border-border/30">
      <div className="flex flex-col h-full p-6">
        <div className="mb-8 px-2">
          <Track2CrackLogo size="sm" showText={true} />
        </div>
        
        <nav className="flex-1 space-y-3">
          {navigation.map((item, index) => (
            <NavLink
              key={item.name}
              to={item.href}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-300",
                  "hover:bg-primary/5 hover:shadow-sm hover:scale-[1.02] hover:border-l-4 hover:border-l-primary/30",
                  "group relative overflow-hidden",
                  isActive
                    ? "bg-gradient-to-r from-primary/10 to-accent/10 text-primary border-l-4 border-l-primary shadow-glow"
                    : "text-muted-foreground hover:text-foreground"
                )
              }
              style={{ animationDelay: `${index * 0.1}s` }}
            >
              <div className={cn(
                "p-2 rounded-lg transition-all duration-300",
                "group-hover:bg-primary/10 group-hover:scale-110"
              )}>
                <item.icon className="w-5 h-5" />
              </div>
              <span className="font-semibold">{item.name}</span>
              {({ isActive }) => isActive && (
                <div className="absolute right-2 w-2 h-2 bg-primary rounded-full animate-pulse-premium"></div>
              )}
            </NavLink>
          ))}
        </nav>

        <div className="pt-6 border-t border-border/30">
          <div className="card-premium p-4 rounded-xl">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-warning/10 rounded-lg">
                <Trophy className="w-4 h-4 text-warning" />
              </div>
              <div>
                <div className="text-sm font-semibold text-foreground">7 day streak!</div>
                <div className="text-xs text-muted-foreground">Keep it up! 🔥</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}