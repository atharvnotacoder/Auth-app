import useAuth from '@/Auth/Store'
import { refreshToken } from '@/services/AuthService';
import React, { useEffect,useState } from 'react'
import toast from 'react-hot-toast';

const OAuth2SuccessPage = () => {

    const[isRefreshing,setIsRefreshing]=useState<boolean>(false);
    const changeLocalLoginData=useAuth(state=>state.changeLocalLoginData);

    useEffect(()=>{
        const getAccessToken = async () => {
            if(!isRefreshing){
                setIsRefreshing(true);
              try{ const responseLoginData = await refreshToken();
               changeLocalLoginData(
                responseLoginData.accessToken,
                responseLoginData.user,
                'authenticated'
               );
               toast.success("Login successful");
            }
            catch(error:any){
                toast.error("Login failed. Please check your credentials and try again.");
                console.log(error);
            }
            finally{
                setIsRefreshing(false);
            }
            }
        };
        getAccessToken();
    },[]);

  return (
    <div>
      
    </div>
  )
}

export default OAuth2SuccessPage
