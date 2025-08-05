import React from 'react'
import ButtonPrimary from './common/ButtonPrimary'
import ButtonSecondary from './common/ButtonSecondary'

export default function ProfileBox() {
    return (
        <div className="h-12 flex">
            <ButtonSecondary className="mr-2" href="/auth/login">
                <span>Log in</span>
            </ButtonSecondary>

            <ButtonPrimary href="/auth/register">
                <span>Sign up</span>
            </ButtonPrimary>
        </div>
    )
}
