'use client'

import Logo from '@/components/Logo'
import { usePathname } from 'next/navigation'
import React from 'react'
import RedirectButton from './RedirectButton'

export interface AuthLayoutProps {
   children: React.ReactNode
}

export default function AuthLayout({ children }: AuthLayoutProps) {
   const pathname = usePathname()

   // Form title
   const isLoginPage = pathname === '/auth/login'
   // const isRegisterPage = pathname === "/auth/register";

   const formTitle = isLoginPage ? 'Đăng nhập' : 'Đăng ký'

   // Redirect auth

   return (
      <div className="flex h-screen w-screen items-center justify-center">
         {/* Logo container */}
         <div className="fixed left-12 top-6">
            <Logo />
         </div>

         {/* Redirect auth */}
         <div className="fixed right-12 top-6 flex items-center rounded-full overflow-hidden ring-2 ring-offset-3 ring-gray-100">
            <RedirectButton href="/auth/login" isActive={isLoginPage}>
               Đăng nhập
            </RedirectButton>

            <RedirectButton href="/auth/register" isActive={!isLoginPage}>
               Đăng ký
            </RedirectButton>
         </div>

         {/* Form box */}
         <div className="shadow-md p-8 rounded-lg bg-white w-full max-w-lg">
            <h1 className="text-3xl font-bold text-gray-600">{formTitle}</h1>

            {/* Welcome */}
            <p className="text-gray-500 text-md mt-2">
               {isLoginPage
                  ? 'Chào mừng bạn trở lại!'
                  : 'Chào mừng bạn đến với ứng dụng quản lý in ấn!'}
            </p>

            {/* Form content */}
            <div className="mt-8">{children}</div>
         </div>
      </div>
   )
}
