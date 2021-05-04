package com.jks.ketchup.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.viewmodals.MainViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.age_and_gender_fragment.*


@AndroidEntryPoint
class GenderAndAgeFragment : Fragment(R.layout.age_and_gender_fragment) {

 private lateinit var viewmodal : MainViewModal

    var genders= "male"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideUpViews(requireContext(),textView3,imageView4,imageView5,radioGroup,et_age,btn_gender_and_age_next )
        viewmodal = ViewModelProvider(requireActivity()).get(MainViewModal::class.java)
        subscribeToObserver()


        radioGroup.setOnCheckedChangeListener { radioGroup, i ->


           if(i==R.id.rb_male){
               genders="male"
           }
            if(i==R.id.rb_female){
                genders="female"
            }
        }


        btn_gender_and_age_next.setOnClickListener {

            if(!et_age.text.isEmpty()) {
                viewmodal.uploadAgeAndGender(genders, et_age.text.toString())
            }else{
                snackbar("enter the age please")
            }
        }


    }
    fun subscribeToObserver(){

        viewmodal.ageStatus.observe(viewLifecycleOwner,EventObserver(

            onError = {
                pbgender.isVisible=false
                snackbar(it)
            },
            onLoading = {
                pbgender.isVisible=true
            }

        ){
            pbgender.isVisible=false
            findNavController().navigate(
                GenderAndAgeFragmentDirections.actionGenderAndAgeFragmentToProfilePicFragment()
            )

        })

    }

    override fun onStart() {
        super.onStart()
        val value = FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().uid!!).get()
        value.addOnSuccessListener {

            if(it["age"] != null || it["gender"] !=null){
                findNavController().navigate(
                    GenderAndAgeFragmentDirections.actionGenderAndAgeFragmentToProfilePicFragment()
                )
            }
        }
    }

}