package com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityCreditCardPaymentPageBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.classes.CheckOutApiClient
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.SuccessfulPaymentData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.AntrenorProfileDetailFragmentArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import kotlinx.coroutines.launch
import okhttp3.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreditCardPaymentPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreditCardPaymentPageBinding
    private val db= Firebase.firestore

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher

    private val args : CreditCardPaymentPageActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditCardPaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51MHUwEJ051n0ryxw5fMxkP7NNeaJyxklXcbxDlvCyyAvnviuNbimsQa19vW0rEsYTNVTPMu3qspxLUqYaaM7kR6K00w0PYS9Xn"
        )
        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )
        startCheckout()

        antrenorDataShow()
    }

    private fun antrenorDataShow(){
        val antrenor_isim=args.antrenorUsername
        val antrenor_ucret=args.antrenorUcret
        val antrenor_email=args.antrenorEmail
        binding.activityCreditCardPaymentIsim.text="ANTRENÖR : "+antrenor_isim.uppercase()
        binding.activityCreditCardPaymentEposta.text=antrenor_email
        binding.activityCreditCardPaymentAylikucret.text="Antrenörün Ücreti : "+antrenor_ucret+" TL"
        binding.activityCreditCardPaymentOdenecektutar.text="Ödenecek Tutar : "+antrenor_ucret+" TL"
    }

    private fun startCheckout() {
        val antrenor_ucret=args.antrenorUcret.toInt()
        Log.d("CreditCardPaymentPageActivity",antrenor_ucret.toString())
        // Create a PaymentIntent by calling the sample server's /create-payment-intent endpoint.
        CheckOutApiClient().createPaymentIntent( antrenor_ucret,completion =  {
                 paymentIntentClientSecret, error ->
            run {
                paymentIntentClientSecret?.let {
                    this.paymentIntentClientSecret = it
                }
                error?.let {
                    displayAlert(
                        "Failed to load page",
                        "Error: $error"
                    )
                }
            }
        })

        // Confirm the PaymentIntent with the card widget
        binding.payButton.setOnClickListener {
            binding.stripeCardMultiLine.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                lifecycleScope.launch {
                    paymentLauncher.confirm(confirmParams)
                }
                Log.d("CreditCardPaymentPageActivity","ÖdemeButonunaTıklandı")
            }
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                "Completed!"
                Log.d("CreditCardPaymentPageActivity","Complete")
                //verileri firabese gönder
                writesuccessfullPaymentData()
            }
            is PaymentResult.Canceled -> {
                "Canceled!"
                Log.d("CreditCardPaymentPageActivity","Canceled")
            }
            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable.message
                Log.d("CreditCardPaymentPageActivity","Failed")
            }
        }
    }

    private fun displayAlert(
        title: String,
        message: String,
        restartDemo: Boolean = false
    ) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
            if (restartDemo) {
                builder.setPositiveButton("Restart demo") { _, _ ->
                    startCheckout()
                }
            }
            else {
                builder.setPositiveButton("Ok", null)
            }
            builder
                .create()
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun creditCardPageToUserProfile(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("CreditCardPageToUserProfil","CreditCardPageToUserProfilSucces")
        finish()
        startActivity(intent)
        Log.d("CreditCardPaymentPageActivity","Profile Gidildi")
    }

    private fun writesuccessfullPaymentData(){
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val antrenor_isim=args.antrenorUsername
        val antrenor_ucret=args.antrenorUcret
        val antrenor_email=args.antrenorEmail

        val writePaymentReference = FirebaseDatabase.getInstance().getReference("/SuccessfulPayment").push()

        //guncel tarih
        val dateCurrent = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val odemeninGonderildigiTarih = dateCurrent.format(formatter)

        db.collection("UserDetailPost").document(fromUserID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val odeyen_isimsoyisim = "$isim $soyisim"

                val paymentFirebase = SuccessfulPaymentData(
                    writePaymentReference.key,
                    antrenor_isim,
                    antrenor_email,
                    antrenor_ucret,
                    fromUserID,
                    odeyen_isimsoyisim,
                    odemeninGonderildigiTarih
                )
                writePaymentReference.setValue(paymentFirebase).addOnSuccessListener {
                    Log.d("CreditCardPaymentPageActivity", "CreditCardPaymentPageActivity Paylaşılan Veriler RealtimeDatabase Gönderildi.")
                    //veriler başarılı gitti kullanıcı profiline gitme
                    creditCardPageToUserProfile()
                }.addOnFailureListener { exception ->
                    Log.d("CreditCardPaymentPageActivity", "CreditCardPaymentPageActivity Paylaşılan Veriler Gönderilemedi !!")
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }.
            addOnFailureListener{ exception ->
                Log.d("CreditCardPaymentPageActivity","Kullanıcı Profil Bilgileri Alınamadı")
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

}


