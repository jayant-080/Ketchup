package com.jks.ketchup.repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.xpost.other.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultAuthRepository : AuthRepository {

    private val auth= FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance().collection("users")


    override suspend fun register(
        userName: String,
        email: String,
        password: String,
        date:Long
    )= withContext(Dispatchers.IO){
        safeCall {

            val result = auth.createUserWithEmailAndPassword(email,password).await()
            val uid = result.user?.uid!!
            val user = User(uid,userName,date = date)
            firestore.document(uid).set(user).await()
            Resource.Success(result)

        }
    }

    override suspend fun login(email: String, password: String)= withContext(Dispatchers.IO){
        safeCall {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(result)
        }


    }

    override suspend fun verifyEmail()= withContext(Dispatchers.IO){

        safeCall {

           val user= auth.currentUser!!
           user.sendEmailVerification().await()
            Resource.Success(Any())
        }

    }
}