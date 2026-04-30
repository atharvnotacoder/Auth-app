import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

import { BrowserRouter, Route, Routes } from 'react-router-dom'

import Services from './pages/Services.tsx'
import Login from './pages/Login.tsx'
import SignUp from './pages/SignUp.tsx'
import About from './pages/About.tsx'

import RootLayout from './pages/RootLayout.tsx'

import UserLayout from './pages/Users/UserLayout.tsx'
import UserHome from './pages/Users/UserHome.tsx'
import UserProfile from './pages/Users/UserProfile.tsx'
import OAuth2SuccessPage from './pages/Users/OAuth2SuccessPage.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RootLayout />}>
          <Route index element={<App />} />
          <Route path="services" element={<Services />} />
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<SignUp />} />
          <Route path="about" element={<About />} />

          {/* Dashboard routes */}
          <Route path="dashboard" element={<UserLayout />}>
            <Route index element={<UserHome />} />
            <Route path="profile" element={<UserProfile />} />
          </Route>

          {/* OAuth routes (keep OUTSIDE dashboard) */}
          <Route path="oauth/success" element={<OAuth2SuccessPage />} />
          <Route path="oauth/failure" element={<OAuth2SuccessPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
)