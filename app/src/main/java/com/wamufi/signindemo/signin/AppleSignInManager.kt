package com.wamufi.signindemo.signin

import android.content.Context

class AppleSignInManager : SignInManager{
    override suspend fun signIn(context: Context?): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun revokeAccess(): Result<Unit> {
        TODO("Not yet implemented")
    }
}