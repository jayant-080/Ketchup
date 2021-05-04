package com.jks.ketchup.ui.setup.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.others.slideUpViews
import com.jks.ketchup.ui.alluser.AllUserScreen
import com.jks.ketchup.viewmodals.MainViewModal
import com.jks.xpost.other.EventObserver
import com.jks.xpost.other.snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_pic_fragments.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfilePicFragment : Fragment(R.layout.profile_pic_fragments) {

    @Inject
    lateinit var glide : RequestManager


    private  lateinit var viewmodal : MainViewModal
    private lateinit var mylauncher: ActivityResultLauncher<String>
    private val cropImageContract = object : ActivityResultContract<String, Uri?>(){
        override fun createIntent(context: Context, input: String?): Intent {
            return CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return  CropImage.getActivityResult(intent).uri
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mylauncher= registerForActivityResult(cropImageContract){
            viewmodal.setImage(it!!)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideUpViews(requireContext(),textView2,iv_profile,btn_profileupload)
        viewmodal = ViewModelProvider(requireActivity()).get(MainViewModal::class.java)
        subscribeToObserver()

        iv_profile.setOnClickListener {
            mylauncher.launch("image/*")
        }

        btn_profileupload.setOnClickListener {


            imageuri?.let {uri->
                if(imageuri!=null){
                    viewmodal.uploadProfilePic(uri)


                }else{
                    snackbar("choose image")
                }

            }
        }
    }

    private  var imageuri: Uri?= null

    fun subscribeToObserver(){

        viewmodal.imageStatus.observe(viewLifecycleOwner){

            imageuri=it
            glide.load(imageuri).into(iv_profile)
        }

        viewmodal.profileImageStatus.observe(viewLifecycleOwner,EventObserver(

            onError = {
                pbprofile.isVisible=false
                snackbar(it)
            },
            onLoading = {
                pbprofile.isVisible=true
            }

        ){

            val intent = Intent(requireActivity(),AllUserScreen::class.java).apply {
                startActivity(this)
                requireActivity().finish()
            }

        })


    }

    override fun onStart() {
        super.onStart()
        val value = FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().uid!!).get()
        value.addOnSuccessListener {

            if(it["profilepicurl"] !=null){
                val intent = Intent(requireActivity(),AllUserScreen::class.java).apply {
                    startActivity(this)
                    requireActivity().finish()
                }
            }
        }
    }
}