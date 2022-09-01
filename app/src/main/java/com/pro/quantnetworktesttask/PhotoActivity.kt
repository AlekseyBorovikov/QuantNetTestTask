package com.pro.quantnetworktesttask

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pro.quantnetworktesttask.databinding.ActivityMainBinding

class PhotoActivity : AppCompatActivity() {

    private var photoUrl: String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoUrl = intent.getStringExtra("url")
        setContentView(R.layout.photo_activity)
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this)
            .load(photoUrl)
            .into(findViewById(R.id.img))
    }

}