import React, { useState } from 'react'

import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Label } from "../components/ui/label";
import { motion, useScroll } from "framer-motion";
import { FaGithub } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
import type {LoginData}  from '@/models/LoginData';
import { data } from 'react-router';
import toast from 'react-hot-toast';

const Login = () => {

    const[data,setData]=useState<LoginData>({
      email:"",password:""
    })

    const [loading,setLoading]=useState<boolean>(false)

    const[error,setError]=useState<any>(null)


    const handleChange=(e:any)=>{
      setData((data)=>({
        ...data,
        [e.target.name]:e.target.value
    }))
    };

  const handleSubmit=(e:any)=>{
    e.preventDefault();
    console.log(data);

    if(data.email===""){
      toast.error("Please enter your email");
      return;
    }
    if(data.password===""){
      toast.error("Please fill password");
      return;
    }
    setData({email:"",password:""});
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-4 sm:px-6 lg:px-8 py-10 bg-gradient-to-br from-white via-gray-100 to-gray-200 dark:from-black dark:via-gray-900 dark:to-gray-950 transition-colors">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="w-full max-w-sm sm:max-w-md"
      >
        <Card className="rounded-2xl shadow-xl border border-gray-200 dark:border-gray-800 bg-white/90 dark:bg-gray-900/90 backdrop-blur-xl">
          <CardHeader className="space-y-2 text-center">
            <CardTitle className="text-2xl sm:text-3xl font-bold tracking-tight">
              Welcome Back
            </CardTitle>
            <CardDescription className="text-sm sm:text-base text-gray-600 dark:text-gray-400">
              Login to your account to continue
            </CardDescription>
          </CardHeader>
          <form onSubmit={handleSubmit} className="space-y-5">
          <CardContent className="p-5 sm:p-6 space-y-5 sm:space-y-6">
            {/* Email */}
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" name='email' value={data.email} onChange={handleChange} placeholder="you@example.com" />
            </div>

            {/* Password */}
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" name='password' value={data.password} onChange={handleChange} placeholder="••••••••" />
            </div>

            {/* Login Button */}
            <Button className="w-full">Login</Button>

            {/* Divider */}
            <div className="relative flex items-center">
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
              <span className="mx-3 text-sm text-gray-500">or continue with</span>
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
            </div>

            {/* Social Buttons */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Button variant="outline" className="flex items-center justify-center gap-2">
                <FcGoogle size={18} />
                Google
              </Button>

              <Button variant="outline" className="flex items-center justify-center gap-2">
                <FaGithub size={18} />
                GitHub
              </Button>
            </div>
          </CardContent>
          </form>
        </Card>
      </motion.div>
    </div>
  )
}

export default Login
