package com.jks.ketchup.entity

data class Room (
    var uid:String?=null,
    var picurl :String?=null,
    var roomName:String?=null,
    var roomDesc:String?=null,
    var roomCode:String?=null,
    var roomType: String?=null,
    var roomStatus: String?=null,
    var joinList:List<String> = listOf(),
    var joinSize:Int?=null,
    var gender:String?=null



)