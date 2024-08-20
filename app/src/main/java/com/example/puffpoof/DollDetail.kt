package com.example.puffpoof

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DollDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doll_detail)

        val doll = intent.getParcelableExtra<Doll>("doll") ?: return

        val nameTextView: TextView = findViewById(R.id.doll_name)
        val sizeTextView: TextView = findViewById(R.id.doll_size)
        val ratingTextView: TextView = findViewById(R.id.doll_rating)
        val priceTextView: TextView = findViewById(R.id.doll_price)
        val descriptionTextView: TextView = findViewById(R.id.doll_description)
        val imageView: ImageView = findViewById(R.id.doll_image)

        nameTextView.text = doll.name
        sizeTextView.text = doll.size
        ratingTextView.text = doll.rating.toString()
        priceTextView.text = doll.price.toString()
        descriptionTextView.text = doll.description
        Glide.with(this).load(doll.image).into(imageView)
    }
}
