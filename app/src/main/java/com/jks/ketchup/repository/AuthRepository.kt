package com.jks.ketchup.repository

import com.google.firebase.auth.AuthResult
import com.jks.ketchup.others.Resource

interface AuthRepository {

suspend fun register(userName: String , email:String , password:String,date:Long) : Resource<AuthResult>
suspend fun login(email:String , password: String):Resource<AuthResult>
suspend fun verifyEmail():Resource<Any>
}