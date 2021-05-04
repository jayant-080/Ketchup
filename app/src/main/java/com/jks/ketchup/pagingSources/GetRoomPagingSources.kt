package com.jks.ketchup.pagingSources

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import com.jks.ketchup.entity.Room
import com.jks.ketchup.repository.RoomRepository
import kotlinx.coroutines.tasks.await


class GetRoomPagingSources(

    private val q: RoomRepository
):PagingSource<QuerySnapshot,Room>() {
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Room> {
        return try {
            val currentPage = params.key ?: q.getU().get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = q.getU().startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Room::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}