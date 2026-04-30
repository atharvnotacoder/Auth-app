import React from 'react'
import  useAuth  from '@/Auth/Store';
import { Navigate, Outlet } from 'react-router-dom'

const UserLayout = () => {

  const checkLogin=useAuth((state)=>state.checkLogin);

    if(checkLogin()){
      return (
    <div>
      <Outlet/>
    </div>
    )
    }else{
      return <Navigate to="/login"  />
    }

  
}

export default UserLayout
