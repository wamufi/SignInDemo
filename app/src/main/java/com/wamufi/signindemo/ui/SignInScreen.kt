package com.wamufi.signindemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.wamufi.signindemo.signin.LoginType
import com.wamufi.signindemo.viewmodel.SignInViewModel

@Composable
fun SignInScreen(viewModel: SignInViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(model = uiState.profileImage,
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(48.dp))
                Text("name: ${uiState.name}")
                Text("nickname: ${uiState.nickName}")
                Text("token: ${uiState.token}")
            }

            HorizontalDivider(modifier = Modifier.padding(16.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("네이버", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
//                viewModel.signIn(LoginType.NAVER)
                        viewModel.signIn(LoginType.NAVER, context)
                    }) {
                        Text("로그인")
                    }

                    Button(onClick = {
                        viewModel.signOut(LoginType.NAVER)
                    }) {
                        Text("로그아웃")
                    }

                    Button(onClick = {
                        viewModel.revokeAccess(LoginType.NAVER)
                    }) {
                        Text("연동 해제")
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("카카오", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
//                viewModel.signIn(LoginType.NAVER)
                        viewModel.signIn(LoginType.KAKAO, context)
                    }) {
                        Text("로그인")
                    }

                    Button(onClick = {
                        viewModel.signOut(LoginType.KAKAO)
                    }) {
                        Text("로그아웃")
                    }

                    Button(onClick = {
                        viewModel.revokeAccess(LoginType.KAKAO)
                    }) {
                        Text("연동 해제")
                    }
                }
            }
        }
    }
}