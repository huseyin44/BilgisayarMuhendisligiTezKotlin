package com.example.huseyinoral_bilgisayarmuhendisligitez.model

data class PaymentData(
    val paymentid:String?=null,
    val fromUsername: String? = null,
    val toUsername: String? = null,
    val fromUserId: String? = null,
    val toUserId: String? = null,
    val fromKrediKartiIsimSoyisim: String? = null,
    val fromKrediAy: String? = null,
    val fromKrediYil: String? = null,
    val fromKrediCVV: String? = null,
    val odenenUcret : String? =null,
    val baslangicTarihi: String?=null,
    val bitisTarihi: String?=null
    )
