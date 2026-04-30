import React from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import useAuth from "@/Auth/Store";

const UserProfile = () => {

    const user=useAuth(state=>state.user);

  return (
    <div className="min-h-screen bg-black text-white flex items-center justify-center px-4 py-10">
      <div className="w-full max-w-3xl space-y-8">

        {/* Heading */}
        <h1 className="text-center text-3xl font-bold">User Profile</h1>

        {/* Profile Card */}
        <Card className="bg-zinc-900 border border-zinc-800 rounded-2xl">
          <CardHeader>
            <CardTitle>Profile Information</CardTitle>
          </CardHeader>

          <CardContent className="space-y-6">

            {/* Avatar */}
            <div className="flex flex-col items-center gap-3">
              <div className="h-24 w-24 rounded-full bg-orange-400 flex items-center justify-center text-2xl" >
                <img src="{user.image}" alt="🙂" className="h-full w-full rounded-full object-cover" />
              </div>
              <Button variant="outline" size="sm">
                Change Picture
              </Button>
            </div>

            {/* Form */}
            <div className="grid sm:grid-cols-2 gap-4">

              <div className="space-y-2">
                <Label>Full Name</Label>
                <Input value={user?.name} />
              </div>

              <div className="space-y-2">
                <Label>Email</Label>
                <Input defaultValue={user?.email} />
              </div>

              <div className="space-y-2">
                <Label>Phone</Label>
                <Input defaultValue=""/>
              </div>

              <div className="space-y-2">
                <Label>Account Type</Label>
                <Input defaultValue="Standard User" />
              </div>

            </div>

            {/* Edit Button */}
            <Button className="w-full">
              Edit Profile
            </Button>

          </CardContent>
        </Card>

        {/* Account Settings */}
        <Card className="bg-zinc-900 border border-zinc-800 rounded-2xl">
          <CardHeader>
            <CardTitle>Account Settings</CardTitle>
          </CardHeader>

          <CardContent className="space-y-4">
            <Button variant="outline" className="w-full">
              Change Password
            </Button>

            <Button variant="destructive" className="w-full">
              Delete Account
            </Button>
          </CardContent>
        </Card>

      </div>
    </div>
  );
};

export default UserProfile;