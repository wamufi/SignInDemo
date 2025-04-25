package com.wamufi.signindemo.signin

import android.content.Context

interface SignInManager {
    suspend fun signIn(context: Context? = null): Result<String>
    suspend fun signOut(): Result<Unit>
    suspend fun revokeAccess(): Result<Unit>
//    suspend fun getProfile(): Result<Any>
//    suspend fun getAccessToken(): String?
}