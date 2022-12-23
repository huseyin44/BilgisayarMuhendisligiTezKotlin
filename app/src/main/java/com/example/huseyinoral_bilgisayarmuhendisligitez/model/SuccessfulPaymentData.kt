package com.example.huseyinoral_bilgisayarmuhendisligitez.model

data class SuccessfulPaymentData(
    val payment_id:String?=null,
    val antrenor_username: String? = null,
    val antrenor_email: String? = null,
    val antrenor_ucret : String? =null,
    val odeyen_kullanıcı_id: String?=null,
    val odeyen_kullanıcı_username: String?=null,
    val programin_yazildigi_tarih: String?=null
)
