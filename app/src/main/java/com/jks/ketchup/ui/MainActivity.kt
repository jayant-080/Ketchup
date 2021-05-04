package com.jks.ketchup.ui


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.react.modules.core.PermissionListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.entity.Room
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_all_user.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jitsi.meet.sdk.*
import java.net.URL


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), JitsiMeetActivityInterface  {

    private lateinit var viewmodal : UserStatusViewModal
    var roomurl=""
    var currentcount = listOf<String>()
    var currentcount2:List<String> = listOf()
    var currentcount3:List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        slideUpViews(this,iv_main_room_image,tv_main_roomName,tv_main_room_private_hint,editTextTextPersonName,button)
        viewmodal= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        viewmodal.userStatus("offline")
        val code = intent.getStringExtra("code").toString()
        val name = intent.getStringExtra("name").toString()
        val roomimageurl = intent.getStringExtra("url").toString()
         roomurl = intent.getStringExtra("roomurl").toString()
        val roomType = intent.getStringExtra("roomType").toString()

        if(roomType.equals("public")){
        editTextTextPersonName.setText(code)
        tv_main_room_private_hint.isVisible=false
            editTextTextPersonName.isVisible=false
        }else{
            editTextTextPersonName.setText("")
            tv_main_room_private_hint.isVisible=true
            editTextTextPersonName.isVisible=true
        }
        Glide.with(this).load(roomimageurl).into(iv_main_room_image)
        tv_main_roomName.setText(name)


        val url = URL("https://meet.jit.si")
        val option: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(url)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(option)

        button.setOnClickListener {
            val option = JitsiMeetConferenceOptions.Builder()
                .setRoom(editTextTextPersonName.text.toString())
                .setWelcomePageEnabled(false)
                .build()
            viewmodal.userStatus("online")

            if(editTextTextPersonName.text.toString().equals(code) && roomType.equals("private") || roomType.equals("public")){
                JitsiMeetActivity.launch(this, option)
            }else{
                Toast.makeText(this,"no room founded for this code",Toast.LENGTH_LONG).show()
            }

            val firestore = FirebaseFirestore.getInstance()
            val firestore3 = FirebaseFirestore.getInstance().collection("videorooms")
            val room= Room(uid=roomurl)

             firestore.runTransaction {
                 val uid = FirebaseAuth.getInstance().uid!!

                 val roomResult = it.get(firestore3.document(room.uid!!))
                 currentcount = roomResult.toObject(Room::class.java)?.joinList ?: listOf()

                 it.update(
                     firestore3.document(room.uid!!),
                     "joinList",
                     currentcount + uid

                 )
             }.addOnCompleteListener {

                 if(it.isSuccessful){
                     val room= Room(uid=roomurl)

                     CoroutineScope(Dispatchers.IO).launch {
                         val res = firestore3.document(roomurl).get().await().toObject(Room::class.java)!!
                         withContext(Dispatchers.Main){


                             firestore3.document(roomurl).update("joinSize",res.joinList.size).await()
                         }

                     }

                 }
             }


        }

    }
    private fun subscribeToObserver() {
        viewmodal.userStatus.observe(this, EventObserver(
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


    override fun onDestroy() {
        super.onDestroy()

        viewmodal.userStatus("offline")
    }



    override fun onStart() {
        super.onStart()
        viewmodal.userStatus("offline")
        val firestore = FirebaseFirestore.getInstance()
        val firestore3 = FirebaseFirestore.getInstance().collection("videorooms")
        val room= Room(uid=roomurl)


        firestore.runTransaction {
            val uid = FirebaseAuth.getInstance().uid!!

            val roomResult = it.get(firestore3.document(room.uid!!))
             currentcount2 = roomResult.toObject(Room::class.java)?.joinList ?: listOf()
            it.update(
                firestore3.document(room.uid!!),
                "joinList",
                currentcount2 - uid
            )

        }.addOnCompleteListener {

            if(it.isSuccessful){
                val room= Room(uid=roomurl)

                CoroutineScope(Dispatchers.IO).launch {
                    val res = firestore3.document(roomurl).get().await().toObject(Room::class.java)!!
                    withContext(Dispatchers.Main){
                        firestore3.document(roomurl).update("joinSize",res.joinList.size).await()
                    }

                }

            }
        }

    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        TODO("Not yet implemented")
    }


}