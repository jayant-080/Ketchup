package com.jks.ketchup.others

sealed class Resource<T>(val data : T?= null, val message : String?=null) {
    //for success
    class Success<T>(data: T?) : Resource<T>(data)

    //for loading
    class Loading<T>(data: T?= null) : Resource<T>(data)

    //for error
    class Error<T>(message:String , data: T?=null): Resource<T>(data,message)
}