import axios from "axios";
import useAuth from "@/Auth/Store";
const apiClient=axios.create({
    baseURL:import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api/v1',
    headers:{
        'Content-Type':'application/json'
    },
    withCredentials:true,
    timeout:10000
});

apiClient.interceptors.request.use(config=>{
    const token=localStorage.getItem('token');
    const accessToken=useAuth.getState().AccessToken
    if(accessToken){
        config.headers.Authorization=`Bearer ${accessToken}`;
    }
return config;
});

export default apiClient;