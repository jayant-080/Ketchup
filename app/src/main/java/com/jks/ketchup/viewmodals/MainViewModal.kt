package com.jks.ketchup.viewmodals

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jks.ketchup.R
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.ketchup.repository.MainRepository
import com.jks.xpost.other.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModal @ViewModelInject constructor(

    private  val mainrepository:MainRepository,
    private val context: Context,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main

):ViewModel() {



    private val _ageStatus = MutableLiveData<Event<Resource<Any>>>()
    val ageStatus: LiveData<Event<Resource<Any>>> = _ageStatus

    private val _profileImageStatus = MutableLiveData<Event<Resource<Any>>>()
    val profileImageStatus :LiveData<Event<Resource<Any>>> = _profileImageStatus

    private val _imageStatus = MutableLiveData<Uri>()
    val imageStatus:LiveData<Uri> = _imageStatus
    private val _profileImageStatus2 = MutableLiveData<Event<Resource<Any>>>()
    val profileImageStatus2 :LiveData<Event<Resource<Any>>> = _profileImageStatus2

    private val _deleteroomstatus = MutableLiveData<Event<Resource<Any>>>()
    val deleteroomstatus:LiveData<Event<Resource<Any>>> = _deleteroomstatus

    private val _getsingleUserstatus = MutableLiveData<Event<Resource<User>>>()
    val getsingleUserstatus:LiveData<Event<Resource<User>>> = _getsingleUserstatus

    private val _updateUsernamestatus = MutableLiveData<Event<Resource<Any>>>()
    val updateUsernamestatus:LiveData<Event<Resource<Any>>> = _updateUsernamestatus

    fun setImage(uri:Uri){
        _imageStatus.postValue(uri)
    }
    fun uploadProfilePic(uri: Uri){
        if(uri.equals("")){
            val error = "no image choose"
            _profileImageStatus.postValue(Event(Resource.Error(error)))
        }

        _profileImageStatus.postValue(Event(Resource.Loading()))
         viewModelScope.launch(dispatcher) {
         val result = mainrepository.uploadProfilePic(uri)
         _profileImageStatus.postValue(Event(result))
     }

    }
    fun uploadRoomPic(uri: Uri){
        if(uri.equals("")){
            val error = "no image choose"
            _profileImageStatus2.postValue(Event(Resource.Error(error)))
        }

        _profileImageStatus2.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = mainrepository.uploadRoomPic(uri)
            _profileImageStatus2.postValue(Event(result))
        }

    }
    fun uploadAgeAndGender(gender:String,age:String){
        if(gender.isEmpty() || age.equals("")){
            val error=context.getString(R.string.error_input_empty)
            _ageStatus.postValue(Event(Resource.Error(error)))
        }
        _ageStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch (dispatcher){
            val result =  mainrepository.uploadAgeAndGender(gender,age)
            _ageStatus.postValue(Event(result))
        }
    }
    fun deleteRoom(){
        _deleteroomstatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch (dispatcher){
                val result = mainrepository.deleteRoom()
                _deleteroomstatus.postValue(Event(result))
            }

    }
    fun getSingleUser(){

        _getsingleUserstatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result= mainrepository.getSingleUser()
            _getsingleUserstatus.postValue(Event(result))
        }
    }
    fun updateUserName(name:String){

        _updateUsernamestatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result= mainrepository.updateUserName(name)
            _updateUsernamestatus.postValue(Event(result))

        }
    }


}