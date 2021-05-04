package com.jks.ketchup.viewmodals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jks.ketchup.others.Resource
import com.jks.ketchup.repository.UserStatusRepository
import com.jks.xpost.other.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserStatusViewModal @ViewModelInject constructor(

    private val repository : UserStatusRepository,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main
): ViewModel() {
    private val _userStatus = MutableLiveData<Event<Resource<Any>>>()
    val userStatus:LiveData<Event<Resource<Any>>> = _userStatus

    fun userStatus(status:String){

        _userStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = repository.UserStatus(status)
            _userStatus.postValue(Event(result))
        }
    }

}