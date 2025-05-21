package com.wamufi.signindemo.signin

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.NidOAuthLoginState
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.wamufi.signindemo.LogHelper
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NaverSignInManager @Inject constructor(private val context: Context) : SignInManager {

    override suspend fun signIn(context: Context?): Result<String> = signInWithNaver(context!!)
    override suspend fun signOut(): Result<Unit> = signOutWithNaver()
    override suspend fun revokeAccess(): Result<Unit> = deleteNaverToken()
//    override suspend fun getProfile(): Result<NidProfileResponse> = callNaverProfile()

    /// 네이버 로그인
    /// FLAG_ACTIVITY_NEW_TASK 업데이트 되면 context 외부에서 전달받지 않고 사용
    /// https://github.com/naver/naveridlogin-sdk-android/issues/125
    private suspend fun signInWithNaver(context: Context): Result<String> = suspendCoroutine { continuation ->
        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode()
                val errorMessage = NaverIdLoginSDK.getLastErrorDescription()

                continuation.resume(Result.failure(Exception("$errorCode: $errorMessage")))
            }

            override fun onSuccess() {
                continuation.resume(Result.success(NaverIdLoginSDK.getAccessToken() ?: "Access token is null."))
            }
        }

        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)
    }

    /// 회원 네이버 프로필 조회
    suspend fun callNaverProfile(): Result<NidProfileResponse> = suspendCoroutine { continuation ->
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode()
                val errorMessage = NaverIdLoginSDK.getLastErrorDescription()

                continuation.resume(Result.failure(Exception("$errorCode: $errorMessage")))
            }

            override fun onSuccess(result: NidProfileResponse) {
                continuation.resume(Result.success(result))
            }
        }

        NidOAuthLogin().callProfileApi(profileCallback)
    }

    /// 네이버 로그아웃
    private fun signOutWithNaver(): Result<Unit> {
        NaverIdLoginSDK.logout()
        if (NaverIdLoginSDK.getState() == NidOAuthLoginState.NEED_LOGIN) {
            return Result.success(Unit)
        } else {
            return Result.failure(Exception("Logout failed."))
        }
    }

    /// 네이버 연동 해제 (토큰 삭제)
    private suspend fun deleteNaverToken(): Result<Unit> = suspendCoroutine { continuation ->
        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode()
                val errorMessage = NaverIdLoginSDK.getLastErrorDescription()

                continuation.resume(Result.failure(Exception("$errorCode: $errorMessage")))
            }

            override fun onSuccess() {
                continuation.resume(Result.success(Unit))
            }
        }

        NidOAuthLogin().callDeleteTokenApi(oAuthLoginCallback)
    }
}