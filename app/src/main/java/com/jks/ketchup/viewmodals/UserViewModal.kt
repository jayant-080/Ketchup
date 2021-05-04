package com.jks.ketchup.viewmodals

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.Resource
import com.jks.ketchup.repository.UserRepository
import com.jks.xpost.other.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModal @ViewModelInject constructor(
    private val repository: UserRepository,
    private val context: Context,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main


) : ViewModel(){

    private val _getuserStatus = MutableLiveData<Event<Resource<ArrayList<User>>>>()
    val getuserStatus:LiveData<Event<Resource<ArrayList<User>>>> = _getuserStatus



    fun getAllUsers(){

     _getuserStatus.postValue(Event(Resource.Loading()))
      viewModelScope.launch(dispatcher) {

         val result= repository.getAllUsers()
          _getuserStatus.postValue(Event(result))

      }

    }



}