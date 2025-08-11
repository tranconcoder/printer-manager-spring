import Gender from '@/enums/gender.enum'
import type { MediaSize } from '@/enums/media.enum'

export default interface User {
   userId: number
   email: string
   firstName: string
   lastName: string
   gender: Gender
   avatars: {
      [key in `${MediaSize}x${MediaSize}`]: string
   }
}
