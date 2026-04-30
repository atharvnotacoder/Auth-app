import axios from "axios";
import useAuth from "@/Auth/Store";
import { refreshToken } from "@/services/AuthService";
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

// let isRefreshing=false;
// let pending:any=[];

// function queueRequest(cb:any){
//     pending.push(cb);
// }

// function resolveQueue(newtoken:string){
//     pending.forEach((cb:any)=>cb(newtoken));
// }

// //response interceptor to handle 401 errors and refresh token
// apiClient.interceptors.response.use(response=>response,
//     async(error)=>{
//         const is401=error.response.status===401;
//         const originalRequest=error.config;

//         if(!is401 || originalRequest._retry){
//             return Promise.reject(error);
//         }
    
//         //we will refresh token 
//         if(isRefreshing){
//             return new Promise((resolve,reject)=>{
//                 queueRequest((newtoken:string)=>{
//                     if(!newtoken){
//                         reject();
//                     }
//                     originalRequest.headers.Authorization=`Bearer ${newtoken}`;
//                     resolve(apiClient(originalRequest));
//                 });
//            });
//         }

//         //set refresh
//         isRefreshing=true;
//         try{
//             const LoginResponseData=await refreshToken();
//             const newAccessToken=LoginResponseData.accessToken;
//             if(!newAccessToken){
//                 throw new Error("Failed to refresh token");
//             }
//             useAuth.getState().changeLocalLoginData(LoginResponseData.accessToken,LoginResponseData.user,'authenticated');

//             resolveQueue(newAccessToken);
//             originalRequest.headers.Authorization=`Bearer ${newAccessToken}`;
//             return apiClient(originalRequest);

//         }catch(err){
//             resolveQueue('null');
//             useAuth.getState().logout(true);
//             return Promise.reject(err);

//         }finally{
//             isRefreshing=false;
//         }
//     }
// );



let isRefreshing = false;
let pending: any[] = [];

function queueRequest(cb: any) {
    pending.push(cb);
}

function resolveQueue(newtoken: string | null) {
    pending.forEach((cb: any) => cb(newtoken));
    pending = [];
}

apiClient.interceptors.response.use(
    response => response,
    async (error) => {
        const is401 = error.response?.status === 401;
        const originalRequest = error.config;

        if (!is401 || originalRequest._retry) {
            return Promise.reject(error);
        }

        originalRequest._retry = true;

        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                queueRequest((newtoken: string) => {
                    if (!newtoken) {
                        return reject(new Error("Token refresh failed"));
                    }
                    originalRequest.headers.Authorization = `Bearer ${newtoken}`;
                    resolve(apiClient(originalRequest));
                });
            });
        }

        isRefreshing = true;

        try {
            const LoginResponseData = await refreshToken();
            const newAccessToken = LoginResponseData.accessToken;

            if (!newAccessToken) {
                throw new Error("Failed to refresh token");
            }

            useAuth.getState().changeLocalLoginData(
                newAccessToken,
                LoginResponseData.user,
                'authenticated'
            );

            resolveQueue(newAccessToken);

            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return apiClient(originalRequest);

        } catch (err) {
            resolveQueue(null);
            useAuth.getState().logout(true);
            return Promise.reject(err);

        } finally {
            isRefreshing = false;
        }
    }
);

export default apiClient;