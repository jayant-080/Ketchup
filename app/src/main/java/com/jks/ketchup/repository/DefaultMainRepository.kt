package com.jks.ketchup.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.xpost.other.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultMainRepository : MainRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore= FirebaseFirestore.getInstance().collection("users")
    private val firestore2= FirebaseFirestore.getInstance().collection("videorooms")
    private val storage = FirebaseStorage.getInstance()

    override suspend fun uploadAgeAndGender( gender: String, age: String):Resource<Any> = withContext(Dispatchers.IO){

        safeCall {
             val uid = auth.uid!!
            firestore.document(uid).update("gender",gender).await()
            firestore.document(uid).update("age",age).await()
            Resource.Success(Any())
        }
    }


    override suspend fun uploadProfilePic(profilepicUrl: Uri)= withContext(Dispatchers.IO){
        safeCall {
            val uid = auth.uid!!
            val imageresult = storage.getReference(uid).putFile(profilepicUrl).await()
            val downloadurl=imageresult.metadata?.reference?.downloadUrl?.await().toString()
            firestore.document(uid).update("profilepicurl",downloadurl).await()
            Resource.Success(Any())
        }


    }

    override suspend fun uploadRoomPic(profilepicUrl: Uri)= withContext(Dispatchers.IO){
        safeCall {
            val uid = auth.uid!!
            val imageresult = storage.getReference(uid).putFile(profilepicUrl).await()
            val downloadurl=imageresult.metadata?.reference?.downloadUrl?.await().toString()
            firestore2.document(uid).update("picurl",downloadurl).await()
            Resource.Success(Any())
        }




    }

    override suspend fun deleteRoom() = withContext(Dispatchers.IO){
        safeCall {
            val uid = auth.uid!!
            val result = firestore2.document(uid).delete().await()
            Resource.Success(Any())
        }
    }


    override suspend fun getSingleUser() = withContext(Dispatchers.IO){

        safeCall {
            val uid = auth.uid!!
            val result=firestore.document(uid).get().await().toObject(User::class.java)
            Resource.Success(result)
        }
    }

    override suspend fun updateUserName(name: String)= withContext(Dispatchers.IO){
        safeCall {

            val res= firestore.document(FirebaseAuth.getInstance().uid!!).get().await()
            if(res.exists()){
                firestore.document(FirebaseAuth.getInstance().uid!!).update("userName",name).await()
            }
            Resource.Success(Any())
        }

    }
}