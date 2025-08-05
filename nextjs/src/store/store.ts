import { combineReducers, configureStore } from '@reduxjs/toolkit'
import {
   FLUSH,
   PAUSE,
   PERSIST,
   persistReducer,
   PURGE,
   REGISTER,
   REHYDRATE,
} from 'redux-persist'
import storage from 'redux-persist/lib/storage'
import { userSlice } from './slices/user.slice'

const userPersistConfig = {
   key: 'user',
   storage,
}

const rootReducer = combineReducers({
   user: persistReducer(userPersistConfig, userSlice.reducer),
})

export const makeStore = () => {
   return configureStore({
      reducer: rootReducer,
      middleware: (getDefaultMiddleware) =>
         getDefaultMiddleware({
            thunk: true, // ✅ hỗ trợ dispatch function
            serializableCheck: {
               ignoredActions: [
                  FLUSH,
                  REHYDRATE,
                  PAUSE,
                  PERSIST,
                  PURGE,
                  REGISTER,
               ], // ✅ bỏ qua warning với redux-persist
            },
         }),
   })
}

// Infer the type of makeStore
export type AppStore = ReturnType<typeof makeStore>

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<AppStore['getState']>
export type AppDispatch = AppStore['dispatch']
