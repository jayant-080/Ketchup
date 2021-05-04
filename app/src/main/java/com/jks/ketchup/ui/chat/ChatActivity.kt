package com.jks.ketchup.ui.chat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.jks.ketchup.R
import com.jks.ketchup.entity.Message
import com.jks.ketchup.entity.User
import com.jks.ketchup.ui.setup.fragments.UpdateProfilePicFragment
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.user_main_layout.view.*


@AndroidEntryPoint
class ChatActivity :AppCompatActivity() {

    private var rootRef: FirebaseFirestore? = null
    private var fromUid: String? = ""
    private var adapter: MessageAdapter? = null

    private lateinit var statusviewmodel : UserStatusViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        statusviewmodel= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        statusviewmodel.userStatus("online")
        rootRef = FirebaseFirestore.getInstance()

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val fromUser = intent.extras!!.get("fromUser") as User
        fromUid = fromUser.uid
        var fromRooms = fromUser.rooms
        val toUser = intent.extras!!.get("toUser") as User
        val toUid = toUser.uid!!
        var toRooms = toUser.rooms

        var roomId = intent.extras!!.get("roomId") as String

        tv_user_chat_name.text=toUser.userName.toString()
        Glide.with(this).load(toUser.profilepicurl).into(iv_chat_pic)
        if(toUser.status=="online"){
            iv_chatstatus.isVisible= true
        }else{
            iv_chatstatus.isVisible= false
        }

        if (roomId == "noRoomId") {
            roomId = rootRef!!.collection("messages").document().id
            if (fromRooms != null) {
                for ((key, _) in fromRooms) {
                    if (toRooms != null) {
                        if (toRooms.contains(key)) {
                            roomId = key
                        }
                    }
                }
            }
        }




        iv_deleteRoom.setOnClickListener {

            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle("Delete Chat")
            alertDialog.setMessage("Do you want to delete chat?")
            alertDialog.setPositiveButton(
                "yes"
            ) { _, _ ->

                rootRef!!.collection("contacts").document(toUid).collection("userContacts").document(fromUid!!).delete().addOnCompleteListener {
                    if(it.isSuccessful){
                        rootRef!!.collection("contacts").document(fromUid!!).collection("userContacts").document(toUid).delete().addOnCompleteListener {
                            if(it.isSuccessful){
                                rootRef!!.collection("rooms").document(toUid).collection("userRooms").document(roomId).delete().addOnCompleteListener {
                                    if(it.isSuccessful){
                                        rootRef!!.collection("rooms").document(fromUid!!).collection("userRooms").document(roomId).delete().addOnCompleteListener {
                                          if(it.isSuccessful) {

                                              rootRef!!.collection("messages").document(roomId)
                                                  .delete().addOnCompleteListener {
                                                  if (it.isSuccessful) {
                                                      Toast.makeText(
                                                          this,
                                                          "chat and room deleted",
                                                          Toast.LENGTH_LONG
                                                      ).show()
                                                      finish()
                                                  }
                                              }
                                          }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            alertDialog.setNegativeButton(
                "No"
            ) { _, _ ->
            }
            val alert: AlertDialog = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()

















        }


        btn_chat_send.setOnClickListener {
            if (fromRooms == null) {
                fromRooms = mutableMapOf()
            }
            fromRooms!![roomId] = true
            fromUser.rooms = fromRooms
            rootRef!!.collection("users").document(fromUid!!).set(fromUser, SetOptions.merge())
            rootRef!!.collection("contacts").document(toUid).collection("userContacts").document(fromUid!!).set(fromUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(toUid).collection("userRooms").document(roomId).set(fromUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(toUid).collection("userRooms").document(roomId).update("date",System.currentTimeMillis())

            if (toRooms == null) {
                toRooms = mutableMapOf()
            }
            toRooms!![roomId] = true
            toUser.rooms = toRooms
            rootRef!!.collection("users").document(toUid).set(toUser, SetOptions.merge())
            rootRef!!.collection("contacts").document(fromUid!!).collection("userContacts").document(toUid).set(toUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(fromUid!!).collection("userRooms").document(roomId).set(toUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(fromUid!!).collection("userRooms").document(roomId).update("date",System.currentTimeMillis())

            val messageText = et_chat_message.text.toString()
            val message = Message(messageText, fromUid!!)
            rootRef!!.collection("messages").document(roomId).collection("roomMessages").add(message)
            et_chat_message.text.clear()


    }

        val query = rootRef!!.collection("messages").document(roomId).collection("roomMessages").orderBy("sentAt", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java).build()
        adapter = MessageAdapter(options)
        rv_chat.adapter = adapter

        title = toUser.userName

    }


    private fun subscribeToObserver() {
        statusviewmodel.userStatus.observe(this,EventObserver(

            onError = {
              // snackbar(it)
            },
            onLoading = {

            }
        ){

        })

    }

    inner class MessageViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setMessage(message: Message) {
            val textView = view.findViewById<TextView>(R.id.tv_chat_messg)
            textView.text = message.messageText
        }
    }

    inner class MessageAdapter internal constructor(options: FirestoreRecyclerOptions<Message>) : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            return if (viewType == R.layout.chat_right_layout) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right_layout, parent, false)
                MessageViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left_layout, parent, false)
                MessageViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
            holder.setMessage(model)
        }

        override fun getItemViewType(position: Int): Int {
            return if (fromUid != getItem(position).fromUid) {
                R.layout.chat_right_layout
            } else {
                R.layout.chat_left_layout
            }
        }

        override fun onDataChanged() {
           rv_chat.layoutManager!!.scrollToPosition(itemCount - 1)
        }
    }



    override fun onStart() {
        super.onStart()
        if (adapter != null) {
            adapter!!.startListening()
        }

        Handler(Looper.myLooper()!!).postDelayed( {
            statusviewmodel.userStatus("online")
        },1000)

    }

    override fun onStop() {
        super.onStop()
        statusviewmodel.userStatus("offline")
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val  frag = FragmentChatHistory()
        val ft= this.supportFragmentManager.beginTransaction()
        ft.replace(R.id.root_chat,frag)
        ft.addToBackStack(null)
        ft.commit()
    }




}





