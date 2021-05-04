package com.jks.ketchup.viewmodals

import android.content.Context
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.jks.ketchup.R
import com.jks.ketchup.others.Resource
import com.jks.ketchup.repository.AuthRepository
import com.jks.xpost.other.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthViewModal  @ViewModelInject constructor(

    private  val authRepository: AuthRepository,
    private val context: Context,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main

): ViewModel(){


    private val _registerStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val registerStatus: LiveData<Event<Resource<AuthResult>>> = _registerStatus

    private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatus : LiveData<Event<Resource<AuthResult>>> = _loginStatus

    private val _emailVerifyStatus = MutableLiveData<Event<Resource<Any>>>()
    val emailVerifyStatus: LiveData<Event<Resource<Any>>> = _emailVerifyStatus

     fun register(userName: String , email:String , password:String,date:Long){

         val error = if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
             context.getString(R.string.error_input_empty)
         }
         else if (userName.length < 4) {
             context.getString(R.string.error_username_too_short)
         } else if (userName.length > 15) {
             context.getString(R.string.error_username_too_long)
         } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             context.getString(R.string.error_not_a_valid_email)
         } else null

         error?.let {
           _registerStatus.postValue(Event(Resource.Error(it)))
         }

         _registerStatus.postValue(Event(Resource.Loading()))
         viewModelScope.launch(dispatcher) {
             val result = authRepository.register(userName, email, password,date)
             _registerStatus.postValue(Event(result))
         }

     }
     fun login(email:String , password:String){

         if(email.isEmpty() || password.isEmpty()){
             val error= context.getString(R.string.error_input_empty)
             _loginStatus.postValue(Event(Resource.Error(error)))
         }

         _loginStatus.postValue(Event(Resource.Loading()))
         viewModelScope.launch (dispatcher){
             val result= authRepository.login(email,password)
             _loginStatus.postValue(Event(result))

         }


     }

    fun verifyEmail(){

        _emailVerifyStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {

            val result = authRepository.verifyEmail()
            _emailVerifyStatus.postValue(Event(result))
        }

    }


}