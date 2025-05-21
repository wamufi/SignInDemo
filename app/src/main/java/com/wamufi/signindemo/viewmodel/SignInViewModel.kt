package com.wamufi.signindemo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wamufi.signindemo.LogHelper
import com.wamufi.signindemo.signin.GoogleSignInManager
import com.wamufi.signindemo.signin.KakaoSignInManager
import com.wamufi.signindemo.signin.LoginType
import com.wamufi.signindemo.signin.NaverSignInManager
import com.wamufi.signindemo.signin.SignInManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(var isLoading: Boolean = false,
    var isLoggedIn: Boolean = false,
    var isRememberedMe: Boolean = false,
    var token: String = "",
    var name: String? = null,
    var nickName: String? = null,
    var profileImage: String? = null,
    var errorMessage: String = "")

@HiltViewModel
class SignInViewModel @Inject constructor(private val naverSignInManager: NaverSignInManager,
    private val kakaoSignInManager: KakaoSignInManager, private val googleSignInManager: GoogleSignInManager) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val managerMap = mapOf(
        LoginType.GOOGLE to googleSignInManager,
//        LoginType.APPLE to appleSignInManager,
        LoginType.NAVER to naverSignInManager, LoginType.KAKAO to kakaoSignInManager)

    private fun getManager(type: LoginType): SignInManager {
        return managerMap[type] ?: throw IllegalArgumentException("Invalid login type")
    }

    /**
     * 로그인 (네이버는 context 필수)
     */
    fun signIn(type: LoginType, context: Context? = null) {
        viewModelScope.launch {
            getManager(type).signIn(context).onSuccess { token ->
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false, isLoggedIn = true, token = token)
                }

                getProfile(type)
            }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
        }
    }

    private suspend fun getProfile(type: LoginType) {
        when (type) {
            LoginType.GOOGLE -> getGoogleUserProfile()
            LoginType.APPLE -> getAppleUserProfile()
            LoginType.NAVER -> getNaverUserProfile()
            LoginType.KAKAO -> getKakaoUserProfile()
        }
    }

    private suspend fun getNaverUserProfile() {
        naverSignInManager.callNaverProfile().onSuccess {
            _uiState.update { uiState ->
                uiState.copy(nickName = it.profile?.nickname,
                    profileImage = it.profile?.profileImage,
                    name = it.profile?.name)
            }
        }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
    }

    private suspend fun getKakaoUserProfile() {
        kakaoSignInManager.fetchUserInfo().onSuccess {
            _uiState.update { uiState ->
                uiState.copy(nickName = it.kakaoAccount?.profile?.nickname,
                    profileImage = it.kakaoAccount?.profile?.profileImageUrl,
                    name = it.kakaoAccount?.name)
            }
        }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
    }

    private suspend fun getGoogleUserProfile() {

    }

    private suspend fun getAppleUserProfile() {
        
    }

    /**
     * 로그아웃
     */
    fun signOut(type: LoginType) {
        viewModelScope.launch {
            getManager(type).signOut().onSuccess {
                _uiState.update { uiState ->
                    uiState.copy(isLoggedIn = false, token = "")
                }
            }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
        }
    }

    /**
     * 연동 해제
     */
    fun revokeAccess(type: LoginType) {
        viewModelScope.launch {
            getManager(type).revokeAccess().onSuccess {
                _uiState.value = LoginUiState()
            }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
        }
    }
}