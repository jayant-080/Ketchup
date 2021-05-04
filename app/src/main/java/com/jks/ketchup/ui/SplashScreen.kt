package com.jks.ketchup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.ui.alluser.AllUserScreen
import com.jks.ketchup.ui.fragments.AuthActivity
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_all_user.*

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var viewmodal : UserStatusViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewmodal= ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        viewmodal.userStatus("online")
        val firestore = FirebaseFirestore.getInstance().collection("users")


        Handler(Looper.myLooper()!!).postDelayed({

            if(FirebaseAuth.getInstance().currentUser == null){
                val intent = Intent(this,AuthActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }else{


                val intent = Intent(this, AllUserScreen::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }, 2000L)
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


}