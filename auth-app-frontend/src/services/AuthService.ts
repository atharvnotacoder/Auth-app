import type RegisterData from  "@/models/RegisterData";
import apiClient from "@/Config/ApiClient";

//Register user function
export const registerUser=async(signUpData:RegisterData)=>{
    //api call to server to register user   
    const res=await apiClient.post('/auth/register',signUpData);
    return res.data;
};