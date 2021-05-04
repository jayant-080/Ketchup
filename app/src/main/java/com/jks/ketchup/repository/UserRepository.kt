package com.jks.ketchup.repository

import com.google.firebase.firestore.Query
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource

interface UserRepository {

    suspend fun getAllUsers() : Resource<ArrayList<User>>



}