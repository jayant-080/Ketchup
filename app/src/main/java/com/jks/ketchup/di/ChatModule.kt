package com.jks.ketchup.di

import com.jks.ketchup.repository.ChatRepository
import com.jks.ketchup.repository.DefaultChatRepsitory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ChatModule {

    @ActivityScoped
    @Provides
    fun provideChatRepositroy()= DefaultChatRepsitory() as ChatRepository

}