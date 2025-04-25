package com.wamufi.signindemo.signin

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.wamufi.signindemo.LogHelper
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoSignInManager @Inject constructor(private val context: Context): SignInManager {

    override suspend fun signIn(context: Context?): Result<String> = signInWithKakao()
    override suspend fun signOut(): Result<Unit> = signOutwithKakao()
    override suspend fun revokeAccess(): Result<Unit> = unlinkWithKakao()
//    override suspend fun getProfile(): Result<User> = fetchUserInfo()

    /// 카카오 로그인
    private suspend fun signInWithKakao(): Result<String> = suspendCoroutine { continuation ->
        val accountCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            LogHelper.i("token: $token, error: $error")
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else if (token != null) {
                continuation.resume(Result.success(token.accessToken))
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) { // 카카오톡이 설치되어 있으면 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
//                    continuation.resume(Result.failure(error))

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoTalk(context, callback = accountCallback)
                } else if (token != null) {
                    continuation.resume(Result.success(token.accessToken))
                }
            }
        } else { // 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(context, callback = accountCallback)
        }
    }

    suspend fun fetchUserInfo(): Result<User> = suspendCoroutine { continuation ->
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else if (user != null) {
                continuation.resume(Result.success(user))
            } else{
                continuation.resume(Result.failure(Exception("User info is null")))
            }
        }
    }

    private suspend fun signOutwithKakao(): Result<Unit> = suspendCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else {
                continuation.resume(Result.success(Unit))
            }
        }
    }

    suspend fun unlinkWithKakao(): Result<Unit> = suspendCoroutine { continuation ->
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else {
                continuation.resume(Result.success(Unit))
            }
        }
    }
}