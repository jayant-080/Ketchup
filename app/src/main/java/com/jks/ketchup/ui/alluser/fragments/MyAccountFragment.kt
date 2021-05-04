package com.jks.ketchup.ui.alluser.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.jks.ketchup.R
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.ui.fragments.AuthActivity
import com.jks.ketchup.ui.setup.fragments.UpdateProfilePicFragment
import com.jks.ketchup.ui.setup.fragments.UpdateUsernameFragment
import com.jks.ketchup.viewmodals.MainViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_myaccount.*
import javax.inject.Inject

@AndroidEntryPoint
class MyAccountFragment : Fragment(R.layout.fragment_myaccount) {
    @Inject
    lateinit var glide : RequestManager
    private  lateinit var viewmodal : MainViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideUpViews(requireContext(),iv_my_profile_pic,tv_setting_username,tv_setting_change_profile_pic,tv_setting_change_username,tv_setting_room,tv_logout,tv_terms,tv_privacypolicy)
        viewmodal = ViewModelProvider(requireActivity()).get(MainViewModal::class.java)
        subscribeToObserver()
        viewmodal.getSingleUser()


        tv_terms.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://ketchup.flycricket.io/terms.html")
            )
            startActivity(viewIntent)
        }

        tv_privacypolicy.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://ketchup.flycricket.io/privacy.html")
            )
            startActivity(viewIntent)
        }

       tv_setting_change_profile_pic.setOnClickListener {
         val  frag = UpdateProfilePicFragment()
           val ft= requireActivity().supportFragmentManager.beginTransaction()
           ft.replace((requireView().parent as ViewGroup).id,frag)
           ft.addToBackStack(null)
           ft.commit()
       }

        tv_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(),AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        tv_setting_room.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle("Delete Room")
            alertDialog.setMessage("Do you want to delete your room?")
            alertDialog.setPositiveButton(
                "yes"
            ) { _, _ ->
                viewmodal.deleteRoom()
            }
            alertDialog.setNegativeButton(
                "No"
            ) { _, _ ->
            }
            val alert: AlertDialog = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()

        }

        tv_setting_change_username.setOnClickListener {
            val  frag = UpdateUsernameFragment()
            val ft= requireActivity().supportFragmentManager.beginTransaction()
            ft.replace((requireView().parent as ViewGroup).id,frag)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    private fun subscribeToObserver() {
        viewmodal.deleteroomstatus.observe(viewLifecycleOwner, EventObserver(

            onError = {
                snackbar(it)
            },
            onLoading = {
                snackbar("deleting...")
            }
        ){
           // snackbar("room deleted")
        })

        viewmodal.getsingleUserstatus.observe(viewLifecycleOwner, EventObserver(

            onError = {
                snackbar(it)
            },
            onLoading = {

            }
        ){

            val profilepic = it.profilepicurl!!
            val name = it.userName!!
            glide.load(profilepic).into(iv_my_profile_pic)
            tv_setting_username.text=name


        })



    }
}