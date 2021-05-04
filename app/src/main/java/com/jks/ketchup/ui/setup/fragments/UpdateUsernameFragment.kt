package com.jks.ketchup.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jks.ketchup.R
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.viewmodals.MainViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import kotlinx.android.synthetic.main.fragment_update_username.*

class UpdateUsernameFragment : Fragment(R.layout.fragment_update_username) {

    private lateinit var viewmodel:MainViewModal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideUpViews(requireActivity(),iv_main__image,tv_updatenamedummy,et_update_username,btn_update_username)
        viewmodel=ViewModelProvider(requireActivity()).get(MainViewModal::class.java)
        subscribeToObserver()

        btn_update_username.setOnClickListener {

            if(et_update_username.text.toString().isEmpty()){
                snackbar("username is empty")
            }else{
                viewmodel.updateUserName(et_update_username.text.toString())
            }
        }

    }

    private fun subscribeToObserver() {

        viewmodel.updateUsernamestatus.observe(requireActivity(),EventObserver(

            onError = {
                snackbar(it)
                pb_updateUsername.isVisible=false
            },
            onLoading = {
                pb_updateUsername.isVisible=true
            }

        ){
            snackbar("updated")
            pb_updateUsername.isVisible=false
        })

    }


}