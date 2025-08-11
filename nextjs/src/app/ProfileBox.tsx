'use client'

import React from 'react'
import ButtonPrimary from '@/components/common/ButtonPrimary'
import ButtonSecondary from '@/components/common/ButtonSecondary'
import { useAppSelector } from '@/hooks/redux.hook'
import Image from '@/components/common/Image'

export default function ProfileBox() {
   const isLoggedIn = useAppSelector((state) => state.user.isLoggedIn)
   const avatars = useAppSelector((state) => state.user.avatars)
   const firstName = useAppSelector((state) => state.user.firstName)
   const lastName = useAppSelector((state) => state.user.lastName)

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

   console.log(avatars)

   return (
      <div
         className={
            'h-12 flex items-center gap-2 rounded-full bg-gray-50 flex-row-reverse p-1 pl-4 ring-2 ring-gray-100 ring-offset-1'
         }
      >
         <Image
            className="rounded-full"
            src={avatars['200x200']}
            width={40}
            height={40}
            fallback={'user.png'}
            alt={'avatar'}
         />

         <span className="mr-2">
            {firstName} {lastName}
         </span>
      </div>
   )
}
