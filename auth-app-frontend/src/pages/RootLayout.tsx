import React from 'react'
import { Outlet } from 'react-router'
import Navbar from '../components/ui/navbar'
import { Toaster } from 'react-hot-toast'

const RootLayout = () => {
  return (
    <div>
        <Toaster/>
        <Navbar/>
      <Outlet />
    </div>
  )
}

export default RootLayout
