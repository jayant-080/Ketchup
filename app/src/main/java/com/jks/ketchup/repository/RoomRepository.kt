package com.jks.ketchup.repository

import com.google.firebase.firestore.Query
import com.jks.ketchup.entity.Room
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource

interface RoomRepository {
    //creating room
    suspend fun createRoom(uid:String, picurl:String , roomName:String, roomDesc:String, roomCode:String,roomType: String,roomStatus:String,joinlist:List<String>,gender:String): Resource<Any>

    suspend fun getAllRooms(): Resource<ArrayList<Room>>
    suspend fun getAllBoysRooms(): Resource<ArrayList<Room>>
    suspend fun getAllGirlsRooms(): Resource<ArrayList<Room>>
    suspend fun searchRoom(query:String):Resource<List<Room>>
    suspend fun searchUser(query:String):Resource<List<User>>
     fun getU(): Query

}