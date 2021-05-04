package com.jks.ketchup.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jks.ketchup.entity.Room
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.xpost.other.Event
import com.jks.xpost.other.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class DeafaultCreateRoomRepository : RoomRepository {

    val auth = FirebaseAuth.getInstance()
    val firestore= FirebaseFirestore.getInstance().collection("videorooms")
    val firestore2= FirebaseFirestore.getInstance().collection("users")



    override suspend fun createRoom(
        uid: String,
        picurl: String,
        roomName: String,
        roomDesc: String,
        roomCode: String,
        roomType:String,
        roomStatus:String,
        joinlist:List<String>,
        gender:String

    )= withContext(Dispatchers.IO){

        safeCall {

            val uid = auth.uid!!
            val room = Room(uid=uid, picurl = picurl, roomName = roomName, roomDesc = roomDesc, roomCode = roomCode,roomType = roomType,roomStatus = roomStatus,joinList = joinlist,gender =gender )
            firestore.document(uid).set(room).await()
            Resource.Success(Any())
        }

    }

    override suspend fun getAllRooms() = withContext(Dispatchers.IO){
        safeCall {

            val allList = ArrayList<Room>()
            val allrooms=firestore.whereEqualTo("roomStatus","online")
                .orderBy("joinSize",Query.Direction.DESCENDING).get().await()
            for(document in allrooms.documents){

                val uid = document.get("uid").toString()
                val roomName = document.get("roomName").toString()
                val picUrl = document.get("picurl").toString()
                val roomdesc= document.get("roomDesc").toString()
                val roomCode = document.get("roomCode").toString()
                val roomType= document.get("roomType").toString()
                val roomJoined= document.get("joinList") as List<String>
                val gender = document.get("gender").toString()
                val room = Room(uid = uid,picurl = picUrl,roomName = roomName,roomDesc = roomdesc,roomCode = roomCode, roomType = roomType,joinList =roomJoined,gender = gender)
                 allList.add(room)

            }
            Resource.Success(allList)
        }
    }

    override suspend fun getAllBoysRooms()= withContext(Dispatchers.IO){
        safeCall {

            val allList = ArrayList<Room>()
            val allrooms=firestore.whereEqualTo("roomStatus","online").whereEqualTo("gender","male")
                .orderBy("joinSize",Query.Direction.DESCENDING).get().await()
            for(document in allrooms.documents){

                val uid = document.get("uid").toString()
                val roomName = document.get("roomName").toString()
                val picUrl = document.get("picurl").toString()
                val roomdesc= document.get("roomDesc").toString()
                val roomCode = document.get("roomCode").toString()
                val roomType= document.get("roomType").toString()
                val roomJoined= document.get("joinList") as List<String>
                val gender = document.get("gender").toString()
                val room = Room(uid = uid,picurl = picUrl,roomName = roomName,roomDesc = roomdesc,roomCode = roomCode, roomType = roomType,joinList =roomJoined,gender = gender)
                allList.add(room)

            }
            Resource.Success(allList)
        }
    }

    override suspend fun getAllGirlsRooms()= withContext(Dispatchers.IO){
        safeCall {

            val allList = ArrayList<Room>()
            val allrooms=firestore.whereEqualTo("roomStatus","online").whereEqualTo("gender","female")
                .orderBy("joinSize",Query.Direction.DESCENDING).get().await()
            for(document in allrooms.documents){

                val uid = document.get("uid").toString()
                val roomName = document.get("roomName").toString()
                val picUrl = document.get("picurl").toString()
                val roomdesc= document.get("roomDesc").toString()
                val roomCode = document.get("roomCode").toString()
                val roomType= document.get("roomType").toString()
                val roomJoined= document.get("joinList") as List<String>
                val gender = document.get("gender").toString()
                val room = Room(uid = uid,picurl = picUrl,roomName = roomName,roomDesc = roomdesc,roomCode = roomCode, roomType = roomType,joinList =roomJoined,gender = gender)
                allList.add(room)

            }
            Resource.Success(allList)
        }
    }

    override suspend fun searchRoom(query: String)= withContext(Dispatchers.IO){

        safeCall {
            val search= firestore.whereEqualTo("roomName",query).whereEqualTo("roomStatus","online").get().await().toObjects(Room::class.java)
            Resource.Success(search)
        }

    }

    override suspend fun searchUser(query: String) = withContext(Dispatchers.IO){
        safeCall {
            val search= firestore2.whereEqualTo("userName",query).get().await().toObjects(User::class.java)
            Resource.Success(search)
        }
    }

    override fun getU(): Query {
            val q= FirebaseFirestore.getInstance().collection("videorooms").whereEqualTo("roomStatus","online")
            .orderBy("joinSize", Query.Direction.DESCENDING).limit(7)
            return  q
    }


}