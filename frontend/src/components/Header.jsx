import { Bell, LogOut } from "lucide-react"
import { Button } from "./ui/button"
import { ThemeToggle } from "./ui/theme-toggle"
import { Track2CrackIcon } from "./ui/logo"
import { useAuth } from "@/contexts/AuthContext"
import { useNavigate } from "react-router-dom"

export function Header() {
  const { logout } = useAuth()
  const navigate = useNavigate()
  
  const handleLogout = () => {
    logout()
    navigate("/login")
  }

  const handleHomeClick = () => {
    navigate("/")
  }

  return (
    <header className="fixed top-0 z-50 w-full card-glass border-b border-border/30 shadow-premium">
      <div className="flex h-16 items-center justify-between px-6">
        <div className="flex items-center gap-4 cursor-pointer group" onClick={handleHomeClick}>
          <div className="p-2 rounded-lg group-hover:bg-primary/10 transition-all duration-300 group-hover:scale-110">
            <Track2CrackIcon size="sm" />
          </div>
          <div className="hidden md:block">
            <p className="text-sm font-medium text-gradient">
              Track your skills, crack your goals
            </p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <Button variant="ghost" size="icon" className="hover:bg-primary/10 hover:scale-110 transition-all duration-300">
            <Bell className="h-5 w-5" />
          </Button>
          <ThemeToggle />
          <Button 
            variant="ghost" 
            size="icon" 
            onClick={handleLogout}
            className="hover:bg-destructive/10 hover:text-destructive hover:scale-110 transition-all duration-300"
          >
            <LogOut className="h-5 w-5" />
          </Button>
        </div>
      </div>
    </header>
  )
}