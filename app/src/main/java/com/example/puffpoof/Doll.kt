package com.example.puffpoof

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doll(
    val id: Int,
    val name: String,
    val size: String,
    val rating: Float,
    val price: Int,
    val image: String,
    val description: String
) : Parcelable
