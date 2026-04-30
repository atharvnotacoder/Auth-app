import React, { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";
import { User as User2,Activity, Shield, Clock } from "lucide-react";
import { getUser as getUserFromService } from "@/services/AuthService";
import useAuth from "@/Auth/Store";
import type User  from "@/models/User";
import toast from "react-hot-toast";


const UserHome = () => {

  const user=useAuth((state)=>state.user);
  const [user1,setUser1]=useState<User|null>(null);
  const getUser=  async (p0?: string) => {
    try{
        const userData=await getUserFromService(user?.email || "")
        setUser1(userData);
      }catch(err){
        toast.error("Failed to fetch user data");

    }
  }

 const stats = [
  {
    title: "Total Logins",
    value: "1,284",
    icon: <Activity size={22} />,
  },
  {
    title: "Active Sessions",
    value: "23",
    icon: <User2 size={22} />,
  },
  {
    title: "Security Score",
    value: "98%",
    icon: <Shield size={22} />,
  },
  {
    title: "Last Login",
    value: "2h ago",
    icon: <Clock size={22} />,
  },
];

  return (
    <div className="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-gray-50 dark:bg-black text-gray-900 dark:text-white transition-colors">
      
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-2xl sm:text-3xl font-bold">Dashboard</h1>
        <p className="text-gray-600 dark:text-gray-400 mt-1">
          Overview of your account activity and stats
        </p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
        {stats.map((stat, i) => (
          <motion.div
            key={i}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
          >
            <Card className="rounded-2xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900 shadow-sm hover:shadow-md transition">
              <CardContent className="p-5 flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-500 dark:text-gray-400">
                    {stat.title}
                  </p>
                  <h2 className="text-xl font-semibold">{stat.value}</h2>
                </div>
                <div className="text-indigo-600 dark:text-indigo-400">
                  {stat.icon}
                </div>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>

      {/* Bottom Sections */}
      <div className="grid lg:grid-cols-2 gap-6">
        
        {/* Activity */}
        <Card className="rounded-2xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900">
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {["Logged in", "Password changed", "New device added", "Logged out"].map((item, i) => (
              <div key={i} className="flex justify-between text-sm">
                <span className="text-gray-700 dark:text-gray-300">{item}</span>
                <span className="text-gray-400">{i + 1}h ago</span>
              </div>
            ))}
          </CardContent>
        </Card>

        {/* Actions */}
        <Card className="rounded-2xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900">
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <Button onClick={() => getUser()} className="w-full">Get User</Button>

            <p>
              {user1?.name}
            </p>


            <Button variant="outline" className="w-full">Change Password</Button>
            <Button variant="outline" className="w-full">Enable 2FA</Button>
          </CardContent>
        </Card>

      </div>
    </div>
  )
}

export default UserHome
