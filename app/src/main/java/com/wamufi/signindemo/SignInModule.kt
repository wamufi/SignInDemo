package com.wamufi.signindemo

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.wamufi.signindemo.signin.KakaoSignInManager
import com.wamufi.signindemo.signin.NaverSignInManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInModule {
    @Provides
    @Singleton
    fun providesNaverSignInManager(@ApplicationContext context: Context): NaverSignInManager {
        NaverIdLoginSDK.initialize(context, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, context.getString(R.string.app_name))
        NaverIdLoginSDK.showDevelopersLog(true)

        return NaverSignInManager(context)
    }

    @Provides
    @Singleton
    fun providesKakaoSignInManager(@ApplicationContext context: Context): KakaoSignInManager{
        KakaoSdk.init(context, BuildConfig.KAKAO_APP_KEY)
        KakaoSdk.loggingEnabled = true

        return KakaoSignInManager(context)
    }
}