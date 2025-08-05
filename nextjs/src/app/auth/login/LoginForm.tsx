/* eslint-disable react-hooks/exhaustive-deps */
'use client'

import React, { useEffect } from 'react'
import ButtonPrimary from '@/components/common/ButtonPrimary'
import Input from '@/components/common/Input/Input'
import { FaUserAlt } from 'react-icons/fa'
import { FaLock } from 'react-icons/fa6'
import Link from 'next/link'
import AuthWithGoogleButton from '../AuthWithGoogleButton'
import { useFormik } from 'formik'
import LoginSchema from '@/schema/Login.schema'
import { useAppDispatch, useAppSelector } from '@/hooks/redux.hook'
import { fetchLoginUser } from '@/store/thunks/user.thunk'
import HideWithDelay from '@/components/common/HideWithDelay'
import { HashLoader } from 'react-spinners'
import ShowWithDelay from '@/components/common/ShowWithDelay'
import { useRouter } from 'next/navigation'

export default function LoginForm() {
   const dispatch = useAppDispatch()
   const router = useRouter()

   const formik = useFormik({
      initialValues: {
         email: '',
         password: '',
      },
      onSubmit: (values) => {
         dispatch(fetchLoginUser(values))
      },
      validationSchema: LoginSchema,
   })

   const errorMessage = useAppSelector((state) => state.user.errorMessage)
   const isLoading = useAppSelector((state) => state.user.isLoading)
   const isLoggedIn = useAppSelector((state) => state.user.isLoggedIn)

   useEffect(() => {
      if (isLoggedIn) {
         router.push('/')
      }
   }, [isLoggedIn])

   return (
      <form onSubmit={formik.handleSubmit}>
         {/* Username */}
         <Input
            name="email"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.email}
            errorMessage={formik.touched.email ? formik.errors.email : ''}
            icon={FaUserAlt}
         >
            Email đăng nhập
         </Input>

         {/* Password */}
         <Input
            name="password"
            icon={FaLock}
            togglePassword
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.password}
            type="password"
            errorMessage={formik.touched.password ? formik.errors.password : ''}
         >
            Mật khẩu
         </Input>

         {/* Forgot password link */}
         <Link
            className="block mt-4 text-sm text-gray-600 hover:underline text-right"
            href="/auth/forgot-password"
         >
            Quên mật khẩu?
         </Link>

         {/* Loading icon */}
         <HideWithDelay isShow={isLoading} minDuration={1000}>
            {errorMessage && (
               <p className="text-red-500 text-sm mt-2">{errorMessage}</p>
            )}
         </HideWithDelay>

         <ShowWithDelay isShow={isLoading} minDuration={1000}>
            <HashLoader
               className="mx-auto my-4"
               color="#3b82f6"
               size={30}
               aria-label="Loading Spinner"
               data-testid="loader"
            />
         </ShowWithDelay>

         {/* Login button */}
         <ButtonPrimary className="mt-6 w-full" type="submit">
            Đăng nhập
         </ButtonPrimary>

         {/* Or with Google */}
         <span className="text-sm italic block text-center my-4">Hoặc</span>

         {/* Google authentication button */}
         <AuthWithGoogleButton>
            Đăng nhập bằng tài khoản Google
         </AuthWithGoogleButton>
      </form>
   )
}
