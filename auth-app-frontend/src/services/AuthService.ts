import type RegisterData from  "@/models/RegisterData";
import apiClient from "@/Config/ApiClient";
import type { LoginData } from "@/models/LoginData";
import type LoginResponseData from "@/models/LoginResponseData";
import type User from "@/models/User";

//Register user function
export const registerUser=async(signUpData:RegisterData)=>{
    //api call to server to register user   
    const res=await apiClient.post('/auth/register',signUpData);
    return res.data;
};

export const loginUser=async(loginData:LoginData)=>{
    const res=await apiClient.post<LoginResponseData>('/auth/login',loginData);
    return res.data;
}

export const logoutUser=async()=>{
    const res= await apiClient.post('/auth/logout');
    return res.data
}

export const getUser=async(emailId:string|undefined)=>{
    if(!emailId){
        throw new Error("Email ID is required");
    }
    const res= await apiClient.post<User>(`/user/email/${emailId}`);
        return res.data;
    }
