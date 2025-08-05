'use client'

import { AppStore, makeStore } from '@/store/store'
import { useRef } from 'react'
import { Provider } from 'react-redux'

export default function StoreProvider({
   children,
}: {
   children: React.ReactNode
}) {
   const storeRef = useRef<AppStore | null>(null)

   if (!storeRef.current) {
      storeRef.current = makeStore()
   }

   // Check user is logged in
   

   return <Provider store={storeRef.current}>{children}</Provider>
}
