import axios, { AxiosError, AxiosHeaders, AxiosRequestConfig } from 'axios'
import { API_URL } from '@/configs/axios.config'
import { RefreshTokenResponse } from '@/types/https/auth.response'

const axiosInstance = axios.create({
   baseURL: API_URL,
   headers: {
      'Content-Type': 'application/json',
   },
})

// Separate instance for token refresh to avoid interceptor recursion
const refreshClient = axios.create({
   baseURL: API_URL,
   headers: { 'Content-Type': 'application/json' },
})

// Normalize headers to AxiosHeaders without unsafe casts
const toAxiosHeaders = (headers: unknown): AxiosHeaders => {
   if (headers instanceof AxiosHeaders) return headers
   const ax = new AxiosHeaders()
   if (headers && typeof headers === 'object') {
      for (const [k, v] of Object.entries(headers as Record<string, unknown>)) {
         ax.set(k, v as string)
      }
   }
   return ax
}

// Concurrency control for refresh flow
let isRefreshing = false
let refreshPromise: Promise<RefreshTokenResponse> | null = null

type RetriableConfig = AxiosRequestConfig & { _retry?: boolean }

axiosInstance.interceptors.request.use(
   (config) => {
      const accessToken =
         typeof window !== 'undefined'
            ? localStorage.getItem('accessToken')
            : null

      if (accessToken) {
         const headers = toAxiosHeaders(config.headers)
         headers.set('Authorization', `Bearer ${accessToken}`)
         config.headers = headers
      }

      return config
   },
   (error) => Promise.reject(error)
)

axiosInstance.interceptors.response.use(
   (response) => {
      // Handle successful responses
      return response
   },
   async (error: AxiosError) => {
      const status = error.response?.status
      const originalRequest = (error.config || {}) as RetriableConfig

      // Do not attempt to refresh for the refresh endpoint itself
      const isRefreshCall = (originalRequest?.url || '').includes(
         '/auth/refresh-token'
      )

      if (status === 401 && !isRefreshCall) {
         const refreshToken =
            typeof window !== 'undefined'
               ? localStorage.getItem('refreshToken')
               : null
         if (!refreshToken) {
            // No refresh token -> clear and reject
            if (typeof window !== 'undefined') {
               localStorage.removeItem('accessToken')
               localStorage.removeItem('refreshToken')
            }
            return Promise.reject(error)
         }

         if (originalRequest._retry) {
            // Already retried once, avoid loops
            return Promise.reject(error)
         }
         originalRequest._retry = true

         try {
            if (!isRefreshing) {
               isRefreshing = true
               refreshPromise = refreshClient
                  .post<RefreshTokenResponse>('/auth/refresh-token', {
                     refreshToken,
                  })
                  .then((res) => res.data)
                  .then((tokens) => {
                     if (typeof window !== 'undefined') {
                        localStorage.setItem('accessToken', tokens.accessToken)
                        localStorage.setItem(
                           'refreshToken',
                           tokens.refreshToken
                        )
                     }
                     return tokens
                  })
                  .catch((refreshErr) => {
                     // On failure, clear auth and propagate
                     if (typeof window !== 'undefined') {
                        localStorage.removeItem('accessToken')
                        localStorage.removeItem('refreshToken')
                     }
                     throw refreshErr
                  })
                  .finally(() => {
                     isRefreshing = false
                  })
            }

            // Wait for refresh to resolve and then retry the original request
            const tokens =
               await (refreshPromise as Promise<RefreshTokenResponse>)

            const headers = toAxiosHeaders(originalRequest.headers)
            headers.set('Authorization', `Bearer ${tokens.accessToken}`)
            originalRequest.headers = headers
            return axiosInstance(originalRequest)
         } catch (refreshError) {
            return Promise.reject(refreshError)
         }
      }

      return Promise.reject(error)
   }
)

export default axiosInstance
