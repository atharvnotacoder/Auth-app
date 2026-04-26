import React from 'react'
import { Link, NavLink } from 'react-router-dom'
import { Button } from './button'
import { Sun, Moon } from 'lucide-react'

const Navbar = () => {

  const toggleTheme = () => {
    const root = document.documentElement

    if (root.classList.contains("dark")) {
      root.classList.remove("dark")
      localStorage.setItem("theme", "light")
    } else {
      root.classList.add("dark")
      localStorage.setItem("theme", "dark")
    }
  }

  return (
    <nav className="py-5 border dark:border-b border-gray-300 dark:border-gray-700 md:py-0 flex md:flex-row gap-4 md:gap-0 flex-col md:h-14 justify-around items-center bg-white dark:bg-gray-800 transition-colors">

      {/* Logo */}
      <div className='font-semibold items-center flex gap-2'>
        <span className='inline-block text-center h-6 w-6 rounded-md bg-gradient-to-r from-primary to-primary/40'>
          {"A"}
        </span>

        <Button variant="link" asChild>
          <Link to="/" className="text-base tracking-tight">
            Auth App
          </Link>
        </Button>
      </div>

      {/* Nav Links */}
      <div className='flex gap-4 items-center'>
        <Link to="/">Home</Link>

        <NavLink to={'/login'}>
          <Button size={'sm'} variant={'outline'}>Login</Button>
        </NavLink>

        <NavLink to={'/signup'}>
          <Button size={'sm'} variant={'outline'}>SignUp</Button>
        </NavLink>

        {/* Theme Toggle */}
        <Button
          size="icon"
          variant="outline"
          onClick={toggleTheme}
        >
          <Sun className="h-4 w-4 dark:hidden" />
          <Moon className="h-4 w-4 hidden dark:block" />
        </Button>
      </div>
    </nav>
  )
}

export default Navbar