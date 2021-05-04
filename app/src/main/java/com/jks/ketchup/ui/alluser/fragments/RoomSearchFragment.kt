package com.jks.ketchup.ui.alluser.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jks.ketchup.R
import com.jks.ketchup.adapters.RoomAdapter
import com.jks.ketchup.adapters.RoomAdaptertwo
import com.jks.ketchup.viewmodals.RoomViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomSearchFragment : Fragment(R.layout.fragment_room_search) {
    private  lateinit var viewmodal : RoomViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodal = ViewModelProvider(requireActivity()).get(RoomViewModal::class.java)
        subscribeToObserver()

        var job: Job?=null
        et_search_room.addTextChangedListener {
        job?.cancel()
            job= lifecycleScope.launch {
                delay(1000L)
                it?.let {qm->
                     viewmodal.searchRoom(qm.toString())
                }
            }

        }
    }

    fun  subscribeToObserver(){
           viewmodal.searchRoom.observe(viewLifecycleOwner, EventObserver(
           onError = {
               pb_search_room.isVisible=false
               snackbar(it)

           },
           onLoading = {
              pb_search_room.isVisible=true
           }
       ){

               if(it.isEmpty()){
                   snackbar("not found")
               }
               pb_search_room.isVisible = false
               val adapter = RoomAdaptertwo(it, requireActivity())
               rv_search_room.adapter = adapter


       })
    }
}