package com.jks.ketchup.ui.chat

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jks.ketchup.R
import com.jks.ketchup.adapters.ChatHistoryAdapter
import com.jks.ketchup.entity.User
import com.jks.ketchup.ui.alluser.fragments.AllUserFragment
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat_history.*

@AndroidEntryPoint
class FragmentChatHistory : Fragment(R.layout.fragment_chat_history) {
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var statusviewmodel : UserStatusViewModal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusviewmodel= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()

    }


    private fun subscribeToObserver() {
        statusviewmodel.userStatus.observe(viewLifecycleOwner, EventObserver(

            onError = {
               snackbar(it)
            },
            onLoading = {

            }

        ){

        })

    }




    override fun onStart() {
        super.onStart()
        if(pb_chatHistory!=null)
        pb_chatHistory.isVisible=true
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val fromUid = firebaseUser.uid
            val rootRef = FirebaseFirestore.getInstance()
            val uidRef = rootRef.collection("users").document(fromUid)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(User::class.java)
                        val userRoomsRef = rootRef.collection("rooms").document(fromUid).collection("userRooms").orderBy("date",Query.Direction.DESCENDING)
                        userRoomsRef.get().addOnCompleteListener{ t ->
                            if (t.isSuccessful) {
                                val listOfToUserNames = ArrayList<String>()
                                val listofToUserAge = ArrayList<String>()
                                val listofToUserPic = ArrayList<String>()
                                val listofToUsergender = ArrayList<String>()
                                val listofToUserStatus = ArrayList<String>()
                                val listOfToUsers = ArrayList<User>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    val toUser = d.toObject(User::class.java)
                                    listOfToUserNames.add(toUser.userName!!)
                                    listofToUserAge.add(toUser.age!!)
                                    listofToUserPic.add(toUser.profilepicurl!!)
                                    listofToUsergender.add(toUser.gender!!)
                                    listofToUserStatus.add(toUser.status!!)
                                    listOfRooms.add(d.id)
                                    listOfToUsers.add(toUser)
                                    if(pb_chatHistory!=null)
                                    pb_chatHistory.isVisible=false
                                }

                                val adapter = ChatHistoryAdapter(listOfToUsers,requireContext(),listOfToUsers,fromUser!!,listOfRooms)
                                if(rv_chatted_users!=null)
                                rv_chatted_users.adapter=adapter
                                adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

