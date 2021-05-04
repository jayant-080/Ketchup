package com.jks.xpost.other

import androidx.lifecycle.Observer
import com.jks.ketchup.others.Resource

class Event<out T>(private val content: T) {

    var hasbeeenHadled = false
        private set

    fun getContentIfNotHanded(): T? {

        return if (!hasbeeenHadled) {
            hasbeeenHadled = true
            content
        } else null
    }

    fun peekContent() = content


}
class EventObserver<T>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : Observer<Event<Resource<T>>> {
    override fun onChanged(t: Event<Resource<T>>?) {
        when (val content = t?.peekContent()) {
            is Resource.Success -> {
                content.data?.let(onSuccess)
            }
            is Resource.Error -> {
                t.getContentIfNotHanded()?.let {
                    onError?.let { error ->
                        error(it.message!!)
                    }
                }
            }
            is Resource.Loading -> {
                onLoading?.let { loading ->
                    loading()
                }
            }
        }
    }
}