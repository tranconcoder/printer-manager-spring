'use client'

import React from 'react'
import ButtonPrimary from './common/ButtonPrimary'
import ButtonSecondary from './common/ButtonSecondary'
import { useAppSelector } from '@/hooks/redux.hook'

export default function ProfileBox() {
   const isLoggedIn = useAppSelector((state) => state.user.isLoggedIn)

   if (!isLoggedIn)
      return (
         <div className="h-12 flex">
            <ButtonSecondary className="mr-2" href="/auth/login">
               <span>Log in</span>
            </ButtonSecondary>

            <ButtonPrimary href="/auth/register">
               <span>Sign up</span>
            </ButtonPrimary>
         </div>
      )

      return <div>hello wolrd </div>
}
