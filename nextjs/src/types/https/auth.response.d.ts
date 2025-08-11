import JwtTokenPair from '../base/jwt'
import User from '../base/user'

interface AuthResponse {
   user: User
   token: JwtTokenPair
}

export type RegisterResponse = AuthResponse

export type LoginResponse = AuthResponse

export type RefreshTokenResponse = JwtTokenPair
