package com.jks.ketchup.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource

interface MainRepository {

    suspend fun uploadAgeAndGender(gender:String, age:String):Resource<Any>
    suspend fun  uploadProfilePic(profilepicUrl:Uri): Resource<Any>
    suspend fun  uploadRoomPic(profilepicUrl:Uri): Resource<Any>
    suspend fun deleteRoom(): Resource<Any>
    suspend fun getSingleUser():Resource<User>
    suspend fun updateUserName(name:String):Resource<Any>
}