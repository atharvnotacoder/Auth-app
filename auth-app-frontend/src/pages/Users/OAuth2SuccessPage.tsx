import useAuth from '@/Auth/Store'
import { refreshToken } from '@/services/AuthService';
import React, { useEffect,useState } from 'react'
import toast from 'react-hot-toast';
import { Spinner } from '@/components/ui/spinner';
import { useNavigate } from 'react-router';

const OAuth2SuccessPage = () => {

    const[isRefreshing,setIsRefreshing]=useState<boolean>(false);
    const changeLocalLoginData=useAuth(state=>state.changeLocalLoginData);
    const navigator=useNavigate();

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
               navigator('/dashboard');
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
    <div className='p-10 flex flex-col gap-3 justify-center items-center'>
      <Spinner/>
    <h1 className='text 2xl font-bold'>Please wait</h1>
    </div>
  )
}

export default OAuth2SuccessPage
