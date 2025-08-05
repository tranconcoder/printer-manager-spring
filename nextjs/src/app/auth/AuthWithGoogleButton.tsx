'use client'

import Image from 'next/image'
import React from 'react'

export interface AuthWithGoogleButtonProps
   extends React.HTMLAttributes<HTMLButtonElement> {
   children?: string
}

export default function AuthWithGoogleButton({
   children = 'Xác thực bằng tài khoản Google',
   ...props
}: AuthWithGoogleButtonProps) {
   return (
      <button
         {...props}
         className={
            'w-full flex items-center justify-between shadow-sm rounded-full px-4 py-3 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-3 transition-colors duration-100 mt-4' +
            ' ' +
            (props.className || '')
         }
      >
         <Image src="/google.png" width={26} height={26} alt="google-icon" />

         <span className="flex-1 text-gray-500">{children}</span>
      </button>
   )
}
