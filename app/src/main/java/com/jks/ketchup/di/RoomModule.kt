package com.jks.ketchup.di

import com.jks.ketchup.repository.DeafaultCreateRoomRepository
import com.jks.ketchup.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object RoomModule {

    @ActivityScoped
    @Provides
    fun provideRoomRepository()= DeafaultCreateRoomRepository() as RoomRepository
}