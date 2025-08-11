import { useAppDispatch } from '@/hooks/redux.hook'
import { resetUser } from '@/store/slices/user.slice'
import { checkIsUserLoggedIn } from '@/store/thunks/user.thunk'
import { useEffect } from 'react'

export default function Initial() {
   const dispatch = useAppDispatch()

   useEffect(() => {
      const accessToken = localStorage.getItem('accessToken')

      if (accessToken) {
         dispatch(checkIsUserLoggedIn())
      } else {
         // Remove information
         dispatch(resetUser())
      }
   }, []) // eslint-disable-line

   return null
}
