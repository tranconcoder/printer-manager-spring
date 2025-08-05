import type { Metadata } from 'next'

import Header from '@/components/Header'
import React from 'react'

export const metadata: Metadata = {
   title: 'tvconss - Quản lý tài liệu in ấn',
   description: 'Welcome to the home page',
}

export default function Home() {
   return (
      <div>
         <Header />
      </div>
   )
}
