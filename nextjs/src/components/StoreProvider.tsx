'use client'

import { AppStore, makeStore } from '@/store/store'
import { Roboto } from 'next/font/google'
import { useRef } from 'react'
import { Provider } from 'react-redux'
import { Persistor, persistStore } from 'redux-persist'
import { PersistGate } from 'redux-persist/integration/react'
import Initial from './Initial'

const roboto = Roboto({
   variable: '--font-roboto',
   subsets: ['latin'],
   display: 'swap',
})

export default function StoreProvider({
   children,
}: {
   children: React.ReactNode
}) {
   const storeRef = useRef<AppStore | null>(null)
   const persistorRef = useRef<Persistor | null>(null)

   if (!storeRef.current) {
      storeRef.current = makeStore()
      persistorRef.current = persistStore(storeRef.current)
   }

   return (
      <html lang="en">
         <body className={`${roboto.variable} antialiased`}>
            <Provider store={storeRef.current}>
               <PersistGate
                  loading={null}
                  persistor={persistorRef.current as Persistor}
               >
                  <Initial />

                  {children}
               </PersistGate>
            </Provider>
         </body>
      </html>
   )
}
