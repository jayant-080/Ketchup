package com.jks.ketchup.ui.alluser.fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.jks.ketchup.R
import com.jks.ketchup.adapters.RoomAdapter
import com.jks.ketchup.adapters.RoomAdaptertwo
import com.jks.ketchup.viewmodals.GetAllRoomViewModel
import com.jks.ketchup.viewmodals.RoomViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_room.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainRoomFragment : Fragment(R.layout.fragment_main_room) {

   // private  lateinit var viewmodal : RoomViewModal
    private val viewModel by viewModels<GetAllRoomViewModel>()
    lateinit var adapters :RoomAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      // viewmodal = ViewModelProvider(requireActivity()).get(RoomViewModal::class.java)
      //  subscribeToObserver()
      //  viewmodal.getAllRooms()

        adapters= RoomAdapter(requireActivity())
        setRoomAdapter()
        getAllRooms()
        setProgressBarAccordingToLoadState()


        iv_search.setOnClickListener {
            findNavController().navigate(
                MainRoomFragmentDirections.actionMainRoomFragmentToRoomSearchFragment()
            )
        }


    }


//    fun subscribeToObserver(){
//        viewmodal.getallRoomstatus.observe(viewLifecycleOwner,EventObserver(
//
//         onError = {
//            snackbar(it)
//             progress_main_room.isVisible= false
//             rv_main_room.isVisible=true
//
//         },
//         onLoading = {
//           progress_main_room.isVisible= true
//             rv_main_room.isVisible=false
//
//
//         }
//
//     ){
//
//            val roomAdapter= RoomAdaptertwo(it,requireActivity())
//            progress_main_room.isVisible = false
//            rv_main_room.isVisible = true
//            rv_main_room.setHasFixedSize(true)
//            rv_main_room.adapter=roomAdapter
//            roomAdapter.notifyDataSetChanged()
//
//
//     })
//
//
//    }


    private fun setRoomAdapter() {
        rv_main_room.adapter = adapters
    }

    private fun getAllRooms() {
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                adapters.submitData(it)
            }
        }
    }

    private fun setProgressBarAccordingToLoadState() {
        lifecycleScope.launch {
            adapters.loadStateFlow.collectLatest {

                if(progress_main_room !=null)
                progress_main_room.isVisible = it.append is LoadState.Loading
            }
        }
    }




}