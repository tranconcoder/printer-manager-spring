import type { ImageProps as NextImageProps } from 'next/image'
import NextImage from 'next/image'
import React, { useEffect } from 'react'

export interface ImageProps extends NextImageProps {
   fallback?: NextImageProps['src']
}

export default function Image({ fallback, onError, src, ...rest }: ImageProps) {
   const [imgSrc, setImgSrc] = React.useState<NextImageProps['src']>(src)

   useEffect(() => {
      setImgSrc(src)
   }, [src, fallback])

   const handleError: React.ReactEventHandler<HTMLImageElement> = (e) => {
      if (fallback && imgSrc !== fallback) {
         setImgSrc(fallback)
      } else {
         onError?.(e)
      }
   }

   return <NextImage {...rest} src={imgSrc} onError={handleError} />
}
