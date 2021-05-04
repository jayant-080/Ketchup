package com.jks.ketchup.ui.chat


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jks.ketchup.R
import com.jks.ketchup.adapters.FriendsAdapter
import com.jks.ketchup.entity.User
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.friend_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class FragmentFriends : Fragment(R.layout.friend_fragment) {

    private lateinit var statusviewmodel : UserStatusViewModal



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusviewmodel= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        pb_main_chat_room.isVisible=true
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val fromUid = firebaseUser.uid
            val rootRef = FirebaseFirestore.getInstance()
            val uidRef = rootRef.collection("users").document(fromUid)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result!!
                    if (document.exists()) {
                        val fromUser = document.toObject(User::class.java)
                        val userContactsRef = rootRef.collection("contacts").document(fromUid).collection("userContacts").orderBy("date",
                            Query.Direction.DESCENDING)
                        userContactsRef.get().addOnCompleteListener{ t ->
                            if (t.isSuccessful) {
                                val listOfToUserNames = ArrayList<String>()
                                val listOfToUsers = ArrayList<User>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    val toUser = d.toObject(User::class.java)
                                    listOfToUserNames.add(toUser.userName!!)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)
                                    pb_main_chat_room.isVisible=false
                                }

                                CoroutineScope(Dispatchers.IO).launch {
                                 val   list= FirebaseFirestore.getInstance().collection("users").get().await().toObjects(User::class.java)
                                 withContext(Dispatchers.Main){
                                     val adapter = FriendsAdapter(listOfToUsers,requireActivity(),listOfToUsers,fromUser!!)
                                     rv_main_chat_room.adapter=adapter
                                     adapter.notifyDataSetChanged()
                                 }
                                }


                            }
                        }
                    }
                }
            }
        }
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







}