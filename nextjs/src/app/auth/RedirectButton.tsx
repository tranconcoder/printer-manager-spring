import Link from 'next/link'
import React from 'react'

export interface RedirectButtonProps extends React.HTMLAttributes<Element> {
   children?: React.ReactNode
   isActive?: boolean
   href: string
}

export default function RedirectButton({
   isActive = false,
   children,
   href,
   ...props
}: RedirectButtonProps) {
   let className = 'px-4 py-1 text-gray-500 h-full cursor-pointer rounded-full'

   if (props.className) {
      className += ` ${props.className}`
   }

   if (isActive) {
      className +=
         ' font-semibold bg-gray-200 hover:bg-gray-300 transition-colors duration-200'
   }

   return (
      <Link {...props} href={href} className={className}>
         {children}
      </Link>
   )
}
