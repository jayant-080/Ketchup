package com.jks.ketchup.others

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.jks.ketchup.R


fun View.slideUpAnimation(context: Context, animTime:Long , startOffset: Long){

    val slideup = AnimationUtils.loadAnimation(context, R.anim.slide_up_animation).apply {

        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideup)



}


fun slideUpViews(context: Context , vararg views: View , animTime:Long = 300L , delay:Long = 150){


    for(i in views.indices){
        views[i].slideUpAnimation(context,animTime,delay * i)
    }
}