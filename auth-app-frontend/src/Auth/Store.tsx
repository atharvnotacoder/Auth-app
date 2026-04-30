import {create} from 'zustand';
import type User from '../models/User';
import type {LoginData} from '../models/LoginData';
import { loginUser, logoutUser } from '@/services/AuthService';
import type LoginResponseData from '@/models/LoginResponseData';
import {persist} from 'zustand/middleware';
// import { User } from 'lucide-react';

const LOCAL_KEY  ="app_state";


type AuthStatus = "idle" | "authenticating" | "authenticated" | "anonymous";

type AuthState = {
    AccessToken:string|null,
    user:User|null,
    authStatus: AuthStatus;
    authLoading:boolean;
    login:(loginData:LoginData)=>Promise<LoginResponseData>;
    logout:(silent?:boolean)=>void;
    checkLogin:()=>boolean | undefined;
};



const useAuth=create<AuthState>()(persist(
    (set,get)=>({
    AccessToken:null,
    user:null,
    authStatus: 'idle' as AuthStatus,
    authLoading:false,
    login: async (loginData: LoginData): Promise<LoginResponseData> => {
        set({ authLoading: true, authStatus: 'authenticating' });
        try {
            const loginResponseData = await loginUser(loginData);
            set({
                AccessToken: loginResponseData.accessToken,
                user: loginResponseData.user,
                authStatus: 'authenticated',
            });
            return loginResponseData;
        } catch (error) {
            set({ authStatus: 'anonymous' });
            throw error;
        } finally {
            set({ authLoading: false });
        }
    },

    logout: async (silent = false) => {
        set({ authLoading: true, authStatus: 'authenticating' });
        try {
            if (!silent) {
                await logoutUser();
            }
        } catch (error) {
            // Ignore logout errors for silent
        } finally {
            set({
                AccessToken: null,
                user: null,
                authStatus: 'anonymous',
                authLoading: false,
            });
        }
    },

    checkLogin: () => {
        const state = get();
        return !!(state.AccessToken && state.authStatus === 'authenticated');
    },

}),{name:LOCAL_KEY}));

export default useAuth;