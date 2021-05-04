package com.jks.ketchup.repository

import com.jks.ketchup.others.Resource

interface UserStatusRepository {
    suspend fun UserStatus(status:String) : Resource<Any>
}