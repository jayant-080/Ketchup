package com.jks.ketchup.viewmodals

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.entity.Room
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.ketchup.repository.RoomRepository
import com.jks.xpost.other.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModal @ViewModelInject constructor(

    private  val repository: RoomRepository,
    private val context: Context,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main
) : ViewModel(){

    private val _createRoomStatus= MutableLiveData<Event<Resource<Any>>>()
    val createRoomStatus : LiveData<Event<Resource<Any>>> = _createRoomStatus


    private val _getallROomStatus = MutableLiveData<Event<Resource<ArrayList<Room>>>>()
    val getallRoomstatus: LiveData<Event<Resource<ArrayList<Room>>>> = _getallROomStatus

    private val _getallROomBoyStatus = MutableLiveData<Event<Resource<ArrayList<Room>>>>()
    val getallRoomBoystatus: LiveData<Event<Resource<ArrayList<Room>>>> = _getallROomBoyStatus

    private val _getallROomGirlsStatus = MutableLiveData<Event<Resource<ArrayList<Room>>>>()
    val getallRoomGirlstatus: LiveData<Event<Resource<ArrayList<Room>>>> = _getallROomGirlsStatus


    private val _searchRoom = MutableLiveData<Event<Resource<List<Room>>>>()
    val searchRoom:LiveData<Event<Resource<List<Room>>>> = _searchRoom

    private val _searchUser = MutableLiveData<Event<Resource<List<User>>>>()
    val searchUser:LiveData<Event<Resource<List<User>>>> = _searchUser












    fun createRoom(uid:String, picurl:String , roomName:String, roomDesc:String, roomCode:String,roomType:String,roomStatus:String,joinList:List<String>,gender:String){

        if(roomName.isEmpty() || roomDesc.isEmpty() || roomCode.isEmpty()){

            val error = context.getString(R.string.error_input_empty)
            _createRoomStatus.postValue(Event(Resource.Error(error)))
        }

        _createRoomStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {

            val result = repository.createRoom(uid, picurl, roomName, roomDesc, roomCode,roomType,roomStatus, listOf(),gender)
            _createRoomStatus.postValue(Event(result))
        }


    }

    fun getAllRooms(){

        _getallROomStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {

            val results = repository.getAllRooms()
            _getallROomStatus.postValue(Event(results))

        }
    }
    fun getAllBoysRooms(){

        _getallROomStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {

            val results = repository.getAllBoysRooms()
            _getallROomStatus.postValue(Event(results))

        }
    }
    fun getAllGirlsRooms(){

        _getallROomStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {

            val results = repository.getAllGirlsRooms()
            _getallROomStatus.postValue(Event(results))

        }
    }

    fun searchRoom(query:String){
     if(query.isEmpty())
     {
         return
     }
      _searchRoom.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result= repository.searchRoom(query)
            _searchRoom.postValue(Event(result))
        }
    }

    fun searchUser(query:String){
        if(query.isEmpty())
        {
            return
        }
        _searchUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result= repository.searchUser(query)
            _searchUser.postValue(Event(result))
        }
    }
}