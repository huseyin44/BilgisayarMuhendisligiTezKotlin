package com.example.huseyinoral_bilgisayarmuhendisligitez.api

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class CheckOutApiClient {
    private val httpClient = OkHttpClient()

    fun createPaymentIntent(amount:Int?=0,completion: (paymentIntentClientSecret: String?, error: String?) -> Unit) {
        val BACKEND_URL = "http://10.0.2.2:4242/"
        val mediaType = "application/json; charset=utf-8".toMediaType()

        //val amount=123
        val payMap = hashMapOf<String, Any>()
        val itemMap = hashMapOf<String, Any>()
        var itemList = java.util.ArrayList<HashMap<String,Any>>()
        payMap["currency"]="usd"
        itemMap["id"]="1"
        itemMap["amount"]=(amount!!.toInt()*54*100)/1000 //TL -> Dolar
        itemList.add(itemMap)
        payMap["items"] = itemList

        val json = Gson().toJson(payMap).toString()
        val body2=RequestBody.create(mediaType,json)
        val request = Request.Builder()
            .url(BACKEND_URL + "create-payment-intent")
            .post(body2)
            .build()
        httpClient.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    completion(null, "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        completion(null, "$response")
                    } else {
                        val responseData = response.body?.string()
                        val responseJson =
                            responseData?.let { JSONObject(it) } ?: JSONObject()

                        // The response from the server contains the PaymentIntent's client_secret
                        var paymentIntentClientSecret : String = responseJson.getString("clientSecret")
                        completion(paymentIntentClientSecret, null)
                    }
                }
            })
    }
}