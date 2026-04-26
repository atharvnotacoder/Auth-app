import React from 'react'
import { Link, NavLink } from 'react-router-dom'
import { Button } from './button'

const Navbar = () => {
  return (
    <nav className="py-5 border dark:border-b border-gray-700 md:py-0 flex md:flex-row gap-4 md:gap-0 flex-col md:h-14 justify-around items-center dark:bg-gray-800">
      <div className='font-semibold items-center flex gap-2'>
        <span className='inline-block text-center h-6 w-6 rounded-md bg-gradient-to-r from-primary to-primary/40'>
        {"A"}</span>
        {/* <span className='text-base tracking-tight'>Auth App</span> */}
        <Button variant="link" asChild>
  <Link to="/" className="text-base tracking-tight">
    Auth App
  </Link>
</Button>
      </div>

      <div className='flex gap-4 item-center'>
        <Link to="/">Home</Link>
        <NavLink to={'/login'}>
        <Button size={'sm'} className='cursor-pointer' variant={'outline'}>Login</Button>
        </NavLink>
        <NavLink to={'/signup'}>        
            <Button size={'sm'} className='cursor-pointer' variant={'outline'}>SignUp</Button>
        </NavLink>

      </div>
    </nav>
  )
}

export default Navbar