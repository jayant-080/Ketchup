package com.jks.xpost.other

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(text:String){

    Snackbar.make(
        requireView(),
        text,
        Snackbar.LENGTH_LONG
    ).show()

}







