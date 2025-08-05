import React, { PropsWithChildren, useEffect, useState } from 'react'

export interface LoadingIconProps extends PropsWithChildren {
   isShow: boolean
   minDuration?: number
}

export default function HideWithDelay({
   minDuration,
   isShow,
   children,
}: LoadingIconProps) {
   const [isShowIcon, setShowIcon] = useState(isShow)
   const [lastLoadingStartAt, setLastLoadingStartAt] = React.useState(
      Date.now()
   )

   useEffect(() => {
      if (minDuration) {
         if (isShow) {
            setLastLoadingStartAt(Date.now())
            setShowIcon(isShow)
         } else {
            const duration = Date.now() - lastLoadingStartAt

            if (duration < minDuration) {
               setTimeout(() => {
                  setShowIcon(false)
               }, minDuration - duration)
            }
         }
      }
   }, [isShow]) // eslint-disable-line

   return !isShowIcon && children
}
