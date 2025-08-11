import axiosInstance from '@/services/axios.service'
import User from '@/types/base/user'
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
         localStorage.setItem('refreshToken', response.data.token.refreshToken)
         localStorage.setItem('accessToken', response.data.token.accessToken)

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
         localStorage.setItem('refreshToken', response.data.token.refreshToken)
         localStorage.setItem('accessToken', response.data.token.accessToken)

         return response.data.user
      } else {
         throw new Error('Đăng nhập thất bại')
      }
   }
)

export const fetchUserInformation = createAsyncThunk(
   'user/fetchUserInformation',
   async (_: void, {}) => {
      return await axiosInstance
         .get('/user/profile')
         .then((res) => res.data as User)
         .catch(() => {
            throw new Error('Lấy thông tin người dùng thất bại!')
         })
   }
)
