package com.jks.ketchup.ui.alluser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.BuildConfig
import com.jks.ketchup.R
import com.jks.ketchup.ui.fragments.AuthActivity

import com.jks.ketchup.ui.setup.ProfileScreen
import com.jks.ketchup.viewmodals.UserStatusViewModal
import com.jks.xpost.other.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_all_user.*


@AndroidEntryPoint
class AllUserScreen : AppCompatActivity() {

    private lateinit var viewmodal: UserStatusViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)
        viewmodal = ViewModelProvider(this).get(UserStatusViewModal::class.java)
        subscribeToObserver()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.alluserhost) as NavHostFragment
        bottomNavigationView.apply {
            menu.getItem(2).isEnabled = false
            setupWithNavController(navHostFragment.findNavController())
            setOnNavigationItemReselectedListener { Unit }
        }
        fabNewPost.setOnClickListener {
            navHostFragment.findNavController().navigate(
                R.id.setGlobalActionToCreateRoom
            )
        }
    }

    private fun subscribeToObserver() {
        viewmodal.userStatus.observe(this, EventObserver(
            onError = {
                Snackbar.make(rootLayout, it, Snackbar.LENGTH_LONG).show()
            },
            onLoading = {
                //Todo
            }
        ) {
            //Todo
            // Snackbar.make(rootLayout,"updated",Snackbar.LENGTH_LONG).show()

        })
    }

    override fun onStop() {
        super.onStop()
        viewmodal.userStatus("offline")
    }

    

    override fun onDestroy() {
        super.onDestroy()
        Handler(Looper.myLooper()!!).postDelayed({
            viewmodal.userStatus("offline")
        },1000L)

    }

    override fun onPause() {
        super.onPause()
        Handler(Looper.myLooper()!!).postDelayed({
            viewmodal.userStatus("offline")
        },1000L)
    }





    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        val user = FirebaseAuth.getInstance().currentUser!!
        if (!user.isEmailVerified) {
            Toast.makeText(this, "login again to verify you email", Toast.LENGTH_LONG).show()
            val intent = Intent(this, AuthActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        viewmodal.userStatus("online")
        val value = FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().uid!!).get()
        value.addOnSuccessListener {
            if (it["age"] == "" || it["gender"] == "" || it["profilepicurl"] == null) {
                val intent = Intent(this, ProfileScreen::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
        //managing versions for updates
        val versionCode = BuildConfig.VERSION_CODE.toString()
        val versionName = BuildConfig.VERSION_NAME
        var v_name: String? = null
        var v_code: String? = null
        var v_url: String? = null

        val firesoteUpdate = FirebaseFirestore.getInstance().collection("update")
        firesoteUpdate.document("ZAb9luGM1OpMLToHJUp9").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result!!
                v_name = result.get("versionName").toString()
                v_code = result.get("versionCode").toString()
                v_url = result.get("url").toString()
                if (versionCode == v_code && versionName.equals(v_name)) {
                   // Toast.makeText(this, "updated", Toast.LENGTH_LONG).show()
                } else {
                    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
                    alertDialog.setTitle("Update Ketchup")
                    alertDialog.setMessage("This app requires a update")
                    alertDialog.setPositiveButton(
                        "yes"
                    ) { _, _ ->
                        val viewIntent = Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(v_url)
                        )
                        startActivity(viewIntent)
                        Toast.makeText(this, "Thanks for updating", Toast.LENGTH_LONG).show()
                    }
                    alertDialog.setNegativeButton(
                        "No"
                    ) { _, _ ->
                        Toast.makeText(this, "you have to update buddy", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    val alert: AlertDialog = alertDialog.create()
                    alert.setCanceledOnTouchOutside(false)
                    alert.show()
                }
            }
        }
    }
}