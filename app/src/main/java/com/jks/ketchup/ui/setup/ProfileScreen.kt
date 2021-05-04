package com.jks.ketchup.ui.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.ui.alluser.AllUserScreen
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileScreen : AppCompatActivity() {

    private lateinit var viewmodal: UserStatusViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_profile)
        viewmodal= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        susbcribeToObserver()
        viewmodal.userStatus("online")



    }

    private fun susbcribeToObserver() {
        viewmodal.userStatus.observe(this,EventObserver(
            onError = {
               Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            },
            onLoading = {

            }

        ){

        })

    }

    override fun onStart() {
        super.onStart()
        val value = FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().uid!!).get()
        value.addOnSuccessListener {

            if(it["age"] != null && it["gender"] !=null && it["profilepicurl"] !=null){

                val intent = Intent(this@ProfileScreen, AllUserScreen::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
        viewmodal.userStatus("online")
    }


    override fun onStop() {
        super.onStop()
        viewmodal.userStatus("offline")
    }


}