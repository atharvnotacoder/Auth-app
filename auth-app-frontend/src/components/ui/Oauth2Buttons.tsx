import React from 'react'
import { Button } from './button'
import { FaGithub } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
import { NavLink } from 'react-router';

const Oauth2Buttons = () => {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full">
      
      <NavLink to={`${import.meta.env.VITE_BASE_URL || "http://localhost:8081"}/oauth2/authorization/google`} className="block w-full">
        <Button type='button'
          variant="outline" 
          className="w-full flex cursor-pointer items-center justify-center gap-2"
        >
          <FcGoogle size={18} />
          Google
        </Button>
      </NavLink>

      <NavLink to={`${import.meta.env.VITE_BASE_URL || "http://localhost:8081"}/oauth2/authorization/github`}  className="block w-full">
        <Button type='button'
          variant="outline" 
          className="w-full flex cursor-pointer items-center justify-center gap-2"
        >
          <FaGithub size={18} />
          GitHub
        </Button>
      </NavLink>

    </div>
  )
}

export default Oauth2Buttons