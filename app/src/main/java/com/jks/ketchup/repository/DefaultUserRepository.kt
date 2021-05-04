package com.jks.ketchup.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.xpost.other.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultUserRepository : UserRepository {
    private val auth= FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance().collection("users")

    override suspend fun getAllUsers() = withContext(Dispatchers.IO){

        safeCall {
            val personlist = ArrayList<User>()
           val getuser = firestore.get().await()
            for(documents in getuser.documents){
                val username=documents.get("userName").toString()
                val age=documents.get("age").toString()
                val gender=documents.get("gender").toString()
                val picurl = documents.get("profilepicurl").toString()
                val uid= documents.get("uid").toString()
                val status =documents.get("status").toString()
                val personnewList = User(uid = uid,userName = username,profilepicurl = picurl,gender = gender,age = age,status = status)
                if(!uid.equals(FirebaseAuth.getInstance().uid!!)) {
                   personlist.add(personnewList)
               }

            }

Resource.Success(personlist)
        }

    }




}