package com.jks.ketchup.ui.alluser.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.jks.ketchup.R
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_all_user.*
import kotlinx.android.synthetic.main.fragment_join_with_code.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

@AndroidEntryPoint
class JoinWithCodeFragment : Fragment(R.layout.fragment_join_with_code) {

    private lateinit var viewmodal : UserStatusViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideUpViews(requireActivity(),iv_main_room_image_frag,tv_main_roomName_frag,tv_main_room_private_hint_frag,editTextTextPersonName_frag,button_join)
        viewmodal= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        viewmodal.userStatus("online")
        val url = URL("https://meet.jit.si")
        val option: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(url)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(option)

        button_join.setOnClickListener {
            val option = JitsiMeetConferenceOptions.Builder()
                .setRoom(editTextTextPersonName_frag.text.toString())
                .setWelcomePageEnabled(false)
                .build()
            viewmodal.userStatus("online")
            JitsiMeetActivity.launch(requireActivity(), option)
        }
    }

    private fun subscribeToObserver() {
        viewmodal.userStatus.observe(requireActivity(), EventObserver(
            onError = {
                Snackbar.make(rootLayout,it, Snackbar.LENGTH_LONG).show()
            },
            onLoading = {
                //Todo
            }
        ){
            //Todo
        })

    }

    override fun onStart() {
        super.onStart()
        viewmodal.userStatus("online")
    }

}