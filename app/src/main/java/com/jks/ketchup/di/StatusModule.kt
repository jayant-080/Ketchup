package com.jks.ketchup.di

import com.jks.ketchup.repository.DefaultUserRepository
import com.jks.ketchup.repository.DefultUserStatus
import com.jks.ketchup.repository.UserStatusRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object StatusModule {

    @ActivityScoped
    @Provides
    fun provideStatusRepository() = DefultUserStatus() as UserStatusRepository

}