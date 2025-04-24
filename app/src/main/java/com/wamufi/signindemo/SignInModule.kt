package com.wamufi.signindemo

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.wamufi.signindemo.signin.NaverSignInManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInModule {
    @Provides
    @Singleton
    fun provideNaverSignInManager(@ApplicationContext context: Context): NaverSignInManager {
        NaverIdLoginSDK.initialize(context, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, context.getString(R.string.app_name))
        NaverIdLoginSDK.showDevelopersLog(true)

        return NaverSignInManager(context)
    }
}