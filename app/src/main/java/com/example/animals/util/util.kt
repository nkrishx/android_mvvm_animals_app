package com.example.animals.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.animals.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {  //to draw a circular progress bar when the image is loading
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

//here we are using ImageView and adding our own function loadImage to it, though ImageView is an android class we can extend it using kotlin extension
//using glide to load the image and spinner while the image is loading
//glide takes care of loading the image from the backend and also error handling, does this in a background thread
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options : RequestOptions = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher_round)
    Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
}