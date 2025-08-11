import { useAppDispatch } from '@/hooks/redux.hook'
import { resetUser } from '@/store/slices/user.slice'
import { fetchUserInformation } from '@/store/thunks/user.thunk'
import { useEffect } from 'react'

export default function Initial() {
   const dispatch = useAppDispatch()

   useEffect(() => {
      const accessToken = localStorage.getItem('accessToken')

      if (accessToken) {
         dispatch(fetchUserInformation())
         return
      }

      const refreshToken = localStorage.getItem('refreshToken')

      if (refreshToken) {
      } else {
         dispatch(resetUser())
      }
   }, []) // eslint-disable-line

   return null
}
