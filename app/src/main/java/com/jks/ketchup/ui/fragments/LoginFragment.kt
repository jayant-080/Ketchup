package com.jks.ketchup.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jks.ketchup.R
import com.jks.ketchup.ui.alluser.AllUserScreen
import com.jks.ketchup.viewmodals.AuthViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var viewmodal : AuthViewModal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodal = ViewModelProvider(requireActivity()).get(AuthViewModal::class.java)
        subscribeToObserver()
        btnlogin.setOnClickListener {

            viewmodal.login(etloginemail.text.toString(),etloginpassword.text.toString())
        }

        termsandconditions.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://ketchup.flycricket.io/terms.html")
            )
            startActivity(viewIntent)
        }

        tv_createnewaccount.setOnClickListener {

            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

       tv_createnewaccount.setOnClickListener {

            if(findNavController().previousBackStackEntry != null){
                findNavController().popBackStack()
            }else {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
            }
        }

    }

    fun subscribeToObserver(){
        viewmodal.loginStatus.observe(viewLifecycleOwner,EventObserver(

            onError = {
                loginprogress.isVisible = false
                snackbar(it)
            },
            onLoading = {
                loginprogress.isVisible = true
            }

        ){

            loginprogress.isVisible = false
            val user = FirebaseAuth.getInstance().currentUser!!
            if(user.isEmailVerified){
                val intent = Intent(requireActivity(),AllUserScreen::class.java).apply {
                    startActivity(this)
                    requireActivity().finish()
                }
            }else{
               snackbar("email is not verified")
            }
        })
    }
    }
