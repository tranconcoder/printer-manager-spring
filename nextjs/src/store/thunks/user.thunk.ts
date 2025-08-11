import axiosInstance from '@/services/axios.service'
import { LoginPayload, RegisterPayload } from '@/types/https/auth.request'
import { LoginResponse, RegisterResponse } from '@/types/https/auth.response'
import { createAsyncThunk } from '@reduxjs/toolkit'
import { HttpStatusCode } from 'axios'

export const fetchRegisterUser = createAsyncThunk(
   'user/register',
   async (userData: RegisterPayload) => {
      const response = await axiosInstance
         .post<RegisterResponse>('/auth/register', userData)
         .catch((error) => {
            const errorMessage =
               error?.response?.data?.errorMessage || 'Đăng ký thất bại'

            throw new Error(errorMessage)
         })

      if (response.status === HttpStatusCode.Created) {
         return response.data.user
      } else {
         console.log('Register failed:', response.data)

         throw new Error('Đăng ký thất bại')
      }
   }
)

export const fetchLoginUser = createAsyncThunk(
   'user/login',
   async (payload: LoginPayload) => {
      const response = await axiosInstance
         .post<LoginResponse>('/auth/login', payload)
         .catch((error) => {
            const errorMessage =
               error?.response?.data?.errorMessage || 'Đăng nhập thất bại'

            throw new Error(errorMessage)
         })

      if (response.status === HttpStatusCode.Accepted) {
         return response.data.user
      } else {
         throw new Error('Đăng nhập thất bại')
      }
   }
)
