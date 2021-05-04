package com.jks.ketchup.di

import com.jks.ketchup.repository.DefaultUserRepository
import com.jks.ketchup.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object UserModule {


    @ActivityScoped
    @Provides
    fun provideUserRepository() = DefaultUserRepository() as UserRepository
}