import Gender from '@/enums/gender.enum'
import User from '@/types/base/user'
import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'
import {
   fetchLoginUser,
   fetchRegisterUser,
   checkIsUserLoggedIn,
} from '../thunks/user.thunk'

export interface UserState extends User {
   isLoggedIn: boolean
   isLoading: boolean
   errorMessage: string
}

const initialState: UserState = {
   userId: -1,
   firstName: '',
   lastName: '',
   email: '',
   gender: Gender.MALE,
   avatars: {} as User['avatars'],

   isLoggedIn: false,
   isLoading: false,
   errorMessage: '',
}

export const userSlice = createSlice({
   name: 'user',
   initialState,
   reducers: {
      setUser: (state, action: PayloadAction<Partial<UserState>>) => {
         return {
            ...state,
            ...action.payload,
         }
      },

      setErrorMessage: (state, action: PayloadAction<string>) => {
         state.errorMessage = action.payload
         console.error('Error message set:', action.payload)
      },

      resetUser: () => {
         return initialState
      },
   },
   extraReducers: (builder) => {
      builder
         //
         // Register thunk
         //
         .addCase(fetchRegisterUser.pending, (state) => {
            state.isLoading = true
         })
         .addCase(fetchRegisterUser.fulfilled, (state, action) => {
            state.isLoading = false
            state.isLoggedIn = true
            state.errorMessage = ''

            state.userId = action.payload.userId
            state.firstName = action.payload.firstName
            state.lastName = action.payload.lastName
            state.email = action.payload.email
            state.avatars = action.payload.avatars || {}
         })
         .addCase(fetchRegisterUser.rejected, (state, action) => {
            state.isLoading = false
            state.errorMessage = action.error.message || 'Đăng ký thất bại'
         })

         //
         // Login thunk
         //
         .addCase(fetchLoginUser.pending, (state) => {
            state.isLoading = true
         })
         .addCase(fetchLoginUser.fulfilled, (state, action) => {
            state.isLoading = false
            state.isLoggedIn = true

            state.userId = action.payload.userId
            state.firstName = action.payload.firstName
            state.lastName = action.payload.lastName
            state.email = action.payload.email
            state.avatars = action.payload.avatars || {}

            state.errorMessage = ''
         })
         .addCase(fetchLoginUser.rejected, (state, action) => {
            state.isLoading = false
            state.errorMessage = action.error.message || 'Đăng nhập thất bại'
         })

         //
         // Check is logged in
         //
         .addCase(checkIsUserLoggedIn.pending, (state) => {
            state.isLoading = true

            state.errorMessage = initialState.errorMessage
         })
         .addCase(checkIsUserLoggedIn.fulfilled, (state, action) => {
            console.log('payload' + action.payload)
            if (!action.payload) {
               return initialState
            }

            state.isLoading = false
         })
         .addCase(checkIsUserLoggedIn.rejected, (state) => {
            state.isLoading = false
            state.isLoggedIn = false
            state.errorMessage = 'Không thể xác định trạng thái đăng nhập'
         })
   },
})

// Action creators are generated for each case reducer function
export const { setUser, setErrorMessage, resetUser } = userSlice.actions

export default userSlice.reducer
