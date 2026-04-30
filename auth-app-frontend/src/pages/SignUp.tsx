import React, { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Label } from "../components/ui/label";
import { motion } from "framer-motion";
import toast from 'react-hot-toast';
import type RegisterData from '../models/RegisterData';
import { registerUser } from '@/services/AuthService';
import { useNavigate } from 'react-router';
import { Spinner } from '@/components/ui/spinner';
import Oauth2Buttons from '@/components/ui/Oauth2Buttons';

const SignUp = () => {

  const [data,setData]=useState<RegisterData>({
    name:"",email:"",password:"",});



    const[loading,setLoading]=useState<boolean>(false);
    const[error,seError]=useState(null);
    
    const navigator=useNavigate();
    //handle form change
const handleInputChange=(e:any)=>{
  // console.log(e.target.name);
  // console.log(e.target.value);
  setData((value)=>({
    ...value,
    [e.target.name]:e.target.value
  }))
}

//handle form submit
const handleSubmit=async(e:any)=>{
  e.preventDefault();
  console.log(data);

  if(data.name===""){
    toast.error("Please enter your name");
    return;
  }
  if(data.email===""){
    toast.error("Please enter your email");
    return;
}
if(data.password===""){
    toast.error("Please fill password");
    return;
}

//form submit for registration
try{
  const res= await registerUser(data);
  console.log(res);
  toast.success("Registration successful! Please login to continue.");
  setData({
    name:"",email:"",password:"",
  });
  //navigate to login page after successful registration
  navigator('/login');
}
catch(err:any){
  toast.error(err.message || "Something went wrong");

}
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
              Create Account
            </CardTitle>
            <CardDescription className="text-sm sm:text-base text-gray-600 dark:text-gray-400">
              Sign up to get started with your account
            </CardDescription>
          </CardHeader>

          <CardContent className="p-5 sm:p-6 space-y-5 sm:space-y-6">
            {/* Name */}
            <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-2">
              <Label htmlFor="name">Full Name</Label>
              <Input id="name" type="text" name='name' value={data.name} onChange={handleInputChange} placeholder="John Doe" />
            </div>

            {/* Email */}
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" name='email'
              value={data.email} onChange={handleInputChange} placeholder="you@example.com" />
            </div>

            {/* Password */}
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" name='password' value={data.password} onChange={handleInputChange} placeholder="••••••••" />
            </div>

            {/* Signup Button */}
            {/* <Button className="w-full">Sign Up</Button> */}
            <Button disabled={loading} className="w-full cursor-pointer">{loading ? <><Spinner/>'Please wait...' </>:"Sign Up"}
              
              </Button>

            {/* Divider */}
            <div className="relative flex items-center">
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
              <span className="mx-3 text-sm text-gray-500">or continue with</span>
              <div className="flex-grow border-t border-gray-300 dark:border-gray-700"></div>
            </div>

            {/* Social Buttons */}
            <Oauth2Buttons/>
            </form>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  )
}

export default SignUp
