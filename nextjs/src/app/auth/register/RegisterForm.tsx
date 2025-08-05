'use client'

import ButtonPrimary from '@/components/common/ButtonPrimary'
import Input from '@/components/common/Input/Input'
import { useAppDispatch, useAppSelector } from '@/hooks/redux.hook'
import RegisterSchema from '@/schema/Register.schema'
import { setErrorMessage } from '@/store/slices/user.slice'
import { fetchRegisterUser } from '@/store/thunks/user.thunk'
import { RegisterPayload } from '@/types/https/auth.request'
import { useFormik } from 'formik'
import { useRouter } from 'next/navigation'
import React, { Fragment, useEffect, useState } from 'react'
import { FaRegAddressCard } from 'react-icons/fa6'
import { LuLockKeyhole } from 'react-icons/lu'
import { MdAlternateEmail } from 'react-icons/md'
import { HashLoader } from 'react-spinners'
import AuthWithGoogleButton from '../AuthWithGoogleButton'
import ShowWithDelay from '@/components/common/ShowWithDelay'
import HideWithDelay from '@/components/common/HideWithDelay'

export type RegisterFormProps = React.HTMLAttributes<HTMLFormElement>

interface RegisterFormikValues extends RegisterPayload {
   confirmPassword: string
   confirmedTerms: boolean
}

export default function RegisterForm({ ...props }: RegisterFormProps) {
   const dispatch = useAppDispatch()
   const router = useRouter()

   const formik = useFormik({
      initialValues: {
         email: '',
         password: '',
         confirmPassword: '',
         firstName: '',
         lastName: '',
         gender: 'male',
         confirmedTerms: false,
      },
      onSubmit: (values: RegisterFormikValues) => {
         dispatch(
            fetchRegisterUser({
               email: values.email,
               password: values.password,
               firstName: values.firstName,
               lastName: values.lastName,
               gender: values.gender,
            })
         )
      },
      validationSchema: RegisterSchema,
   })

   const isMale = formik.values.gender === 'male'
   const isFemale = formik.values.gender === 'female'

   const errorMessage = useAppSelector((state) => state.user.errorMessage)
   const isLoading = useAppSelector((state) => state.user.isLoading)
   const isLoggedIn = useAppSelector((state) => state.user.isLoggedIn)

   useEffect(() => {
      if (isLoggedIn) {
         router.push('/')
      }
   }, [isLoggedIn]) // eslint-disable-line

   useEffect(() => {
      return () => {
         dispatch(setErrorMessage(''))
         console.log('Reset error message on unmount')
      }
   }, []) // eslint-disable-line

   return (
      <form {...props} onSubmit={formik.handleSubmit} className="space-y-4">
         {/* Register with google account */}
         <AuthWithGoogleButton className="mb-4">
            Đăng ký bằng tài khoản Google
         </AuthWithGoogleButton>

         <span className="text-sm italic block text-center my-4">Hoặc</span>

         {/* Registration form fields would go here */}
         <Input
            type="email"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            name="email"
            value={formik.values.email}
            errorMessage={formik.touched.email ? formik.errors.email : ''}
            icon={MdAlternateEmail}
         >
            Email
         </Input>

         <Input
            type="password"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            name="password"
            errorMessage={formik.touched.password ? formik.errors.password : ''}
            value={formik.values.password}
            icon={LuLockKeyhole}
            togglePassword
         >
            Mật khẩu
         </Input>

         <Input
            type="password"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            name="confirmPassword"
            errorMessage={
               formik.touched.confirmPassword
                  ? formik.errors.confirmPassword
                  : ''
            }
            value={formik.values.confirmPassword}
            icon={LuLockKeyhole}
            togglePassword
         >
            Nhập lại mật khẩu
         </Input>

         <div className="flex align-center justify-between gap-6 mt-8">
            <Input
               className="flex-1 !mt-0"
               type="text"
               onChange={formik.handleChange}
               onBlur={formik.handleBlur}
               name="firstName"
               errorMessage={
                  formik.touched.firstName ? formik.errors.firstName : ''
               }
               value={formik.values.firstName}
               icon={FaRegAddressCard}
            >
               Họ
            </Input>

            <Input
               className="flex-1 !mt-0"
               type="text"
               onChange={formik.handleChange}
               onBlur={formik.handleBlur}
               name="lastName"
               errorMessage={
                  formik.touched.lastName ? formik.errors.lastName : ''
               }
               value={formik.values.lastName}
               icon={FaRegAddressCard}
            >
               Tên
            </Input>
         </div>

         <div className="flex items-center gap-6 mt-8 text-lg">
            <span>Giới tính: </span>

            <label
               className={
                  'flex-1 px-10 py-1 rounded-md text-center ring-2 ring-gray-300 ring-offset-2 flex items-center justify-center' +
                  (isMale ? ' bg-blue-200' : '')
               }
            >
               <input
                  className="size-4 mr-2"
                  type="radio"
                  name="gender"
                  title="Nam"
                  id="male"
                  value="male"
                  onChange={formik.handleChange}
                  checked={formik.values.gender === 'male'}
               />
               Nam
            </label>

            <label
               className={
                  'flex-1 px-10 py-1 rounded-md text-center ring-2 ring-gray-300 ring-offset-2 flex items-center justify-center' +
                  (isFemale ? ' bg-pink-200' : '')
               }
            >
               <input
                  className="size-4 mr-2"
                  type="radio"
                  name="gender"
                  title="Nữ"
                  id="female"
                  value="female"
                  onChange={formik.handleChange}
                  checked={formik.values.gender === 'female'}
               />
               Nữ
            </label>
         </div>

         <div className="flex items-center gap-2 mt-8">
            <input
               type="checkbox"
               id="confirmedTerms"
               name="confirmedTerms"
               checked={formik.values.confirmedTerms}
               onChange={formik.handleChange}
               className="size-4"
            />
            <label htmlFor="confirmedTerms" className="text-sm">
               Tôi đã đọc và đồng ý với{' '}
               <a href="/terms" className="text-blue-500 hover:underline">
                  Điều khoản dịch vụ
               </a>
            </label>
         </div>

         <p className="text-red-500 text-sm">
            {formik.touched.confirmedTerms && formik.errors.confirmedTerms}
         </p>

         <HideWithDelay isShow={isLoading} minDuration={2000}>
            {errorMessage && (
               <p className="text-red-500 text-sm">{errorMessage}</p>
            )}
         </HideWithDelay>

         <ShowWithDelay isShow={isLoading} minDuration={2000}>
            <HashLoader
               className="mx-auto my-4"
               color="#3b82f6"
               size={30}
               aria-label="Loading Spinner"
               data-testid="loader"
            />
         </ShowWithDelay>

         {/* Submit button */}
         <ButtonPrimary className="w-full mt-8" type="submit">
            Đăng ký tài khoản
         </ButtonPrimary>
      </form>
   )
}
