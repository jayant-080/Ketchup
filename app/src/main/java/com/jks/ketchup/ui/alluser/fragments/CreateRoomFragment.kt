package com.jks.ketchup.ui.alluser.fragments


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.entity.User
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.viewmodals.RoomViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private lateinit var viewmodal : RoomViewModal
    var url =""
    var roomtype="public"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slideUpViews(requireContext(), imageView6, textView4,et_room_name, et_room_description, et_room_password,radio_createroom,btn_room_create)
        viewmodal = ViewModelProvider(requireActivity()).get(RoomViewModal::class.java)
        subscribeToObserver()

        radio_createroom.setOnCheckedChangeListener { radioGroup, i ->

            if(i==R.id.rb_private){
                roomtype="private"
            }
            if(i==R.id.rb_public){
                roomtype="public"
            }


        }



        val uid = FirebaseAuth.getInstance().uid!!.toString()
        val value = FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener {
               url= it["profilepicurl"].toString()
            }


        btn_room_create.setOnClickListener {


            CoroutineScope(Dispatchers.IO).launch {
                val firestore = FirebaseFirestore.getInstance().collection("users")
                val res= firestore.document(FirebaseAuth.getInstance().uid!!).get().await().toObject(User::class.java)!!
                val gender = res.gender.toString()
                viewmodal.createRoom(uid,url,et_room_name.text.toString(),et_room_description.text.toString(),et_room_password.text.toString(),roomtype,"online",
                    listOf(),gender)
            }




        }
    }


    fun subscribeToObserver(){


        viewmodal.createRoomStatus.observe(viewLifecycleOwner,EventObserver(
            onError = {
                pbcreateroom.isVisible = false
                snackbar(it)

            },

            onLoading = {
                pbcreateroom.isVisible = true
            }

        ){
            pbcreateroom.isVisible = false
            snackbar("room created")

        })
    }
}