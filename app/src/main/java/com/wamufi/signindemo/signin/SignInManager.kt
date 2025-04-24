package com.wamufi.signindemo.signin

interface SignInManager {
    suspend fun signIn(): Result<String>
}