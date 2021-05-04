package com.jks.ketchup.di
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jks.ketchup.repository.DeafaultCreateRoomRepository
import com.jks.ketchup.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped



@Module
@InstallIn(ActivityComponent::class)
object GetAllUsers {

   @Provides
    @ActivityScoped
    fun providUser()= FirebaseFirestore.getInstance().collection("users").orderBy("date",Query.Direction.DESCENDING).limit(7)



}