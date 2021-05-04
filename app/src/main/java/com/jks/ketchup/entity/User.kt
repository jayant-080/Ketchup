package com.jks.ketchup.entity
import java.io.Serializable
import java.util.*
import kotlin.time.microseconds

data class User (
    val uid:String?=null,
    val userName: String?=null,
    val profilepicurl : String?=null,
    val gender:String?=null,
    val age:String ?=null,
    val status:String?=null,

    val date:Long=System.currentTimeMillis(),
    var rooms: MutableMap<String, Any>? = null

):Serializable