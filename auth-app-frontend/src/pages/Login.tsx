import React, { useState } from 'react'

import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Label } from "../components/ui/label";
import { motion, useScroll } from "framer-motion";

import type {LoginData}  from '@/models/LoginData';
import { useNavigate } from 'react-router';
import toast from 'react-hot-toast';

import { Alert, AlertTitle } from '@/components/ui/alert';
import { CheckCircle2Icon } from 'lucide-react';
import { Spinner } from '@/components/ui/spinner';
import useAuth from '@/Auth/Store';
import Oauth2Buttons from '@/components/ui/Oauth2Buttons';
const Login = () => {

    const[data,setData]=useState<LoginData>({
      email:"",password:""
    })

    const [loading,setLoading]=useState<boolean>(false)

    const[error,setError]=useState<any>(null)

    const navigator=useNavigate();

    const login=useAuth(state=>state.login);

    const handleChange=(e:any)=>{
      setData((data)=>({
        ...data,
        [e.target.name]:e.target.value
    }))
    };

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    if (data.email === "") {
      toast.error("Please enter your email");
      return;
    }
    if (data.password === "") {
      toast.error("Please fill password");
      return;
    }

    console.log(data);
    //server call for login
    try {
      setLoading(true);
      // const userInfo = await loginUser(data);

      const userInfo = await login(data);


      toast.success("Login successful");
      // console.log(userInfo); 

      //save the current user logged in info in local storage
      navigator('/dashboard');
      setData({ email: "", password: "" });
    } catch (error: any) {
      setError(error);
      toast.error("Login failed. Please check your credentials and try again.");
    }
    finally{
      setLoading(false);
    }
  };

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
          {
            error && (<div className='mt-2 text-center'>
            <Alert variant="destructive" className="max-w-md">
               <CheckCircle2Icon />
                <AlertTitle>{error?.response?.data?.message}</AlertTitle>
            </Alert>
          </div>
          )}
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
            <Button disabled={loading} className="w-full cursor-pointer">{loading ? <><Spinner/>'Please wait...' </>:"Login"}
              
              </Button>

            {/* Divider */}
            <div className="relative flex items-center">
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
              <span className="mx-3 text-sm text-gray-500">or continue with</span>
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
            </div>

            {/* Social Buttons */}
            <Oauth2Buttons/>
          </CardContent>
          </form>
        </Card>
      </motion.div>
    </div>
  )
}

export default Login
