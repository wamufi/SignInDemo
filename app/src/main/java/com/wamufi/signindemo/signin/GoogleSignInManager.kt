package com.wamufi.signindemo.signin

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.wamufi.signindemo.BuildConfig
import com.wamufi.signindemo.LogHelper
import javax.inject.Inject

class GoogleSignInManager @Inject constructor(context: Context) : SignInManager {

    override suspend fun signIn(context: Context?): Result<String> = signInWithGoogle(context!!)
    override suspend fun signOut(): Result<Unit> = signOutWithGoogle()
    override suspend fun revokeAccess(): Result<Unit> {
        TODO("Not yet implemented")
    }

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    private suspend fun signInWithGoogle(context: Context): Result<String> {
//        val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true).setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID).setAutoSelectEnabled(true).build()
        val googleIdOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        try {
            val credentialManager: CredentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request)
            return handleSignIn(result)
        } catch (e: GetCredentialException) {
            return Result.failure(e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): Result<String> {
        val credential = result.credential
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        return Result.success(idToken)
                    } catch (e: Exception) {
                        return Result.failure(e)
                    }
                }
            }
        }

        return Result.success("nothing")
    }

    private suspend fun signOutWithGoogle(): Result<Unit> {
        try {
            val request = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(request)
            return Result.success(Unit)
        } catch (e: ClearCredentialException) {
            return Result.failure(e)
        }
    }

//    private suspend fun deleteToken(): Result<Unit> {
//        credentialManager.
//    }
}