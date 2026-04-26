import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Route, Routes } from 'react-router'
import Services from './pages/Services.tsx'
import Login from './pages/Login.tsx'
import SignUp from './pages/SignUp.tsx'
import About from './pages/About.tsx'
import RootLayout  from './pages/RootLayout.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
  <Routes>
    <Route path='/' element={<RootLayout/>}>
      <Route index element={<App />} />
      <Route path="/services" element={<Services />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path='/about' element={<About />} />
    </Route>
  </Routes>
  </BrowserRouter>
)
