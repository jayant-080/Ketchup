package com.jks.ketchup.pagingSources

import androidx.paging.PagingSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jks.ketchup.entity.User
import kotlinx.coroutines.tasks.await

class GetAllUsersPagingSource(
    private val querygetAllUser: Query
):PagingSource<QuerySnapshot,User>() {
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, User> {
        return try {
            val currentPage = params.key ?: querygetAllUser.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = querygetAllUser.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(User::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}