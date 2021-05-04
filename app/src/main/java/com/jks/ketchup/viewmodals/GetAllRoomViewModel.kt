package com.jks.ketchup.viewmodals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jks.ketchup.pagingSources.GetAllUsersPagingSource
import com.jks.ketchup.pagingSources.GetRoomPagingSources
import com.jks.ketchup.repository.RoomRepository


class GetAllRoomViewModel @ViewModelInject constructor(
    private val querygetAllRooms: Query,
private val q: RoomRepository
) :ViewModel(){

    val flow = Pager(
        PagingConfig(
            pageSize = 7
        )
    ) {
        GetRoomPagingSources(q)
    }.flow.cachedIn(viewModelScope)

    val flowuser = Pager(
        PagingConfig(
            pageSize = 7
        )
    ) {
        GetAllUsersPagingSource(querygetAllRooms)
    }.flow.cachedIn(viewModelScope)
}