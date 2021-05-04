package com.jks.ketchup.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.others.Resource
import com.jks.xpost.other.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefultUserStatus : UserStatusRepository {

    val cuuruser = FirebaseAuth.getInstance().uid
    val firstore = FirebaseFirestore.getInstance().collection("users")
    val firstore2 = FirebaseFirestore.getInstance().collection("rooms")


    override suspend fun UserStatus(status: String) = withContext(Dispatchers.IO){
        safeCall {

            cuuruser?.let {

                val result= firstore.document(cuuruser).get().await()
                if(result.exists()) {
                    firstore.document(cuuruser).update("status", status).await()
                     val result = firstore2.document(cuuruser).get().await()
                    if(result.exists()){
                        firstore2.document(cuuruser).update("roomStatus",status)

                    }
                }
            }
            Resource.Success(Any())
        }

    }
}