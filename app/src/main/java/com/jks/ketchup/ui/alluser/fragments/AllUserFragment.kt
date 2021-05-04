package com.jks.ketchup.ui.alluser.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.jks.ketchup.R
import com.jks.ketchup.adapters.AllUsersAdapter
import com.jks.ketchup.viewmodals.GetAllRoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_all_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllUserFragment : Fragment(R.layout.fragment_all_user) {

  //  private lateinit var viewmodal: UserViewModal

    private val viewModel by viewModels<GetAllRoomViewModel>()
    lateinit var adapters : AllUsersAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // viewmodal = ViewModelProvider(requireActivity()).get(UserViewModal::class.java)
        //subscribeToObserver()
        //viewmodal.getAllUsers()

        adapters= AllUsersAdapter(requireActivity())
        setRoomAdapter()
        getAllRooms()
        setProgressBarAccordingToLoadState()




        iv_user_search.setOnClickListener {
            findNavController().navigate(
                AllUserFragmentDirections.actionAllUserFragmentToUserSearchFragment()
            )
        }


        main_chat_icon.setOnClickListener {


            findNavController().navigate(
                AllUserFragmentDirections.actionAllUserFragmentToFragmentChatHistory()
            )
        }

        main_chat_list.setOnClickListener {
            findNavController().navigate(
                AllUserFragmentDirections.actionAllUserFragmentToFragmentFriends()
            )
        }


    }

//    fun subscribeToObserver(){
//
//        viewmodal.getuserStatus.observe(viewLifecycleOwner,EventObserver(
//
//            onError = {
//               snackbar(it)
//                progress_main_user.isVisible= false
//                rv_allusers.isVisible = true
//            },
//            onLoading = {
//                rv_allusers.isVisible = false
//                progress_main_user.isVisible= true
//            }
//
//        ){
//            progress_main_user.isVisible= false
//            rv_allusers.isVisible = true
//            val adapter = AllUserAdapter(it,requireActivity())
//            rv_allusers.adapter=adapter
//            adapter.notifyDataSetChanged()
//        })
//
//    }

    private fun setRoomAdapter() {
        rv_allusers.adapter = adapters
    }

    private fun getAllRooms() {
        lifecycleScope.launch {
            viewModel.flowuser.collectLatest {
                adapters.submitData(it)

            }
        }
    }

    private fun setProgressBarAccordingToLoadState() {
        lifecycleScope.launch {
            adapters.loadStateFlow.collectLatest {
                if(progress_main_user!=null)
                progress_main_user.isVisible = it.append is LoadState.Loading
            }
        }
    }


}