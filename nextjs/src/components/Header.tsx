import React from 'react'
import Logo from './Logo'
import ProfileBox from './ProfileBox'

export default function Header() {
    return (
        <header className="bg-white text-black fixed top-0 left-0 right-0 z-50 p-6">
            <nav className="h-10 flex items-center justify-between gap-4 px-4">
                {/* Logo */}
                <Logo />

                {/* Profile box */}
                <ProfileBox />
            </nav>
        </header>
    )
}
