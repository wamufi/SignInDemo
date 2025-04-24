package com.wamufi.signindemo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wamufi.signindemo.LogHelper
import com.wamufi.signindemo.signin.LoginType
import com.wamufi.signindemo.signin.NaverSignInManager
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
class SignInViewModel @Inject constructor(private val naverSignInManager: NaverSignInManager) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun signIn(type: LoginType, context: Context) {
        viewModelScope.launch {
//            val result = naverSignInManager.signIn()
            val result = naverSignInManager.signInWithNaver(context)
            result.onSuccess {
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false, isLoggedIn = true, token = it)
                }

                getProfile()
            }
            result.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
        }
    }

    private suspend fun getProfile() {
        naverSignInManager.callNaverProfile().onSuccess {
            LogHelper.d(it)
            _uiState.update { uiState ->
                uiState.copy(nickName = it.profile?.nickname,
                    profileImage = it.profile?.profileImage,
                    name = it.profile?.name)
            }
        }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
    }

    fun signOut(type: LoginType) {
        naverSignInManager.signOutWithNaver()
    }

    fun deleteToken(type: LoginType) {
        viewModelScope.launch {
            naverSignInManager.deleteNaverToken().onSuccess {
                    _uiState.value = LoginUiState()
                }.onFailure { _uiState.value = LoginUiState(errorMessage = it.message.toString()) }
        }
    }
}