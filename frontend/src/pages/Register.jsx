import { useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
  CardFooter,
} from "@/components/ui/card"
import { useToast } from "@/hooks/useToast"
import { UserPlus } from "lucide-react"
import { Track2CrackLogo } from "@/components/ui/logo"
import { useAuth } from "@/contexts/AuthContext"

export function Register() {
  const [loading, setLoading] = useState(false)
  const { toast } = useToast()
  const { register: registerUser, login } = useAuth()
  const navigate = useNavigate()
  const { register, handleSubmit } = useForm()

  const onSubmit = async (data) => {
    try {
      setLoading(true)
      await registerUser(data.email, data.password);

      // Auto-login after registration
      const user = await login(data.email, data.password);

      toast({
        title: "Success",
        description: "Account created successfully! Let's set up your profile.",
      })

      // Always redirect to onboarding for new users
      navigate("/onboarding")
    } catch (error) {
      console.log("Register error:", error)
      toast({
        variant: "destructive",
        title: "Error",
        description: error?.message,
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 dark:from-gray-900 dark:via-blue-900 dark:to-indigo-900 p-4">
      <Card className="w-full max-w-md shadow-2xl">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            <Track2CrackLogo size="lg" showText={true} />
          </div>
          <CardTitle className="text-2xl">Create an account</CardTitle>
          <CardDescription>Join Track2Crack and start your interview prep journey</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="Enter your email"
                {...register("email", { required: true })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input
                id="password"
                type="password"
                placeholder="Choose a password"
                {...register("password", { required: true })}
              />
            </div>
            <Button type="submit" className="w-full" disabled={loading}>
              {loading ? (
                "Loading..."
              ) : (
                <>
                  <UserPlus className="mr-2 h-4 w-4" />
                  Create Account
                </>
              )}
            </Button>
          </form>
        </CardContent>
        <CardFooter className="flex justify-center">
          <Button
            variant="link"
            className="text-sm text-muted-foreground"
            onClick={() => navigate("/login")}
          >
            Already have an account? Sign in
          </Button>
        </CardFooter>
      </Card>
    </div>
  )
}
