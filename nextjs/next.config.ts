import type { NextConfig } from 'next'

const nextConfig: NextConfig = {
   images: {
      remotePatterns: [
         {
            protocol: 'http',
            hostname: 'localhost',
            port: '4000',
            pathname: '/**',
         },
         {
            protocol: 'https',
            hostname: 'res.cloudinary.com',
            port: '',
            pathname: '/**',
         },
      ],
   },
}

export default nextConfig
