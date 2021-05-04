package com.jks.ketchup.viewmodals

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.jks.ketchup.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers



class ChatViewModal @ViewModelInject constructor(
    private val repository : ChatRepository,
    private val context: Context,
    private val dispathcer: CoroutineDispatcher = Dispatchers.Main

): ViewModel(){




}