package com.jks.ketchup.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jks.ketchup.R
import com.jks.ketchup.viewmodals.AuthViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.register_fragment.*

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var viewmodal: AuthViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodal = ViewModelProvider(requireActivity()).get(AuthViewModal::class.java)
        subscribeToObserver()


        btnreg.setOnClickListener {

            viewmodal.register(etregusername.text.toString(),etregemail.text.toString(),etregpassword.text.toString(),System.currentTimeMillis())



        }

      tv_login.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }

       tv_login.setOnClickListener {

            if(findNavController().previousBackStackEntry != null){
                findNavController().popBackStack()
            }else{
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
            }

        }
    }



    fun subscribeToObserver(){

        viewmodal.registerStatus.observe(viewLifecycleOwner,EventObserver(

            onError = {
                snackbar(it)
                registerprogress.isVisible= false
            },
            onLoading = {
                registerprogress.isVisible = true

            }

        ){
            registerprogress.isVisible= false
            snackbar("registered")
            viewmodal.verifyEmail()
        })


        viewmodal.emailVerifyStatus.observe(viewLifecycleOwner,EventObserver(
            onError = {
                snackbar(it)
            },
            onLoading = {

            }

        ){
            snackbar("email has been sent for verification")
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )

        })

    }
}