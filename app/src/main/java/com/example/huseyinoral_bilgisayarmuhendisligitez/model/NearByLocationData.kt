package com.example.huseyinoral_bilgisayarmuhendisligitez.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NearByLocationData(
    val placesid:String?=null,
    val placesname:String?=null,
    val placesadress:String?=null,
    val placeslat:Double?=null,
    val placeslng:Double?=null,
    val placesdistance:Int?=null,
    val placephone:String?=null,
    val placewebsite:String?=null
): Parcelable
