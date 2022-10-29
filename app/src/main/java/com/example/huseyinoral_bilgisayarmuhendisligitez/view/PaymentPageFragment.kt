package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentPaymentPageBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PaymentData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.card.payment.CardIOActivity
import io.card.payment.DataEntryActivity
import org.checkerframework.checker.units.qual.s
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


class PaymentPageFragment : Fragment() {
    private var _binding: FragmentPaymentPageBinding? = null
    private val binding get() = _binding!!

    private val db= Firebase.firestore
    private val args : PaymentPageFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toUserUcret=args.toUserUcret
        val toUserName=args.toUserName
        binding.paymentPageTextUcret.text="Antrenör $toUserName İçin Ödenmesi Gereken Aylık Ücret : $toUserUcret"

        binding.paymentPageDevamEt.setOnClickListener {
            if(isInputCorrect()){
                activity?.let { it -> hideSoftKeyboard(it) }
                writePayment()
            }
        }
        crediCardType()
    }

    private fun writePayment(){
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val toUserId=args.toUserId
        val toUserUcret=args.toUserUcret
        val toUserName=args.toUserName

        val fromReference = FirebaseDatabase.getInstance().getReference("/AntrenorPayment/$fromUserID").push()

        db.collection("UserDetailPost").document(fromUserID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val fromIsimsoyisim= "$isim $soyisim"
                val fromKrediIsim=binding.paymentPageUsername.text.toString()
                val fromKrediiAy=binding.paymentPageKartAy.text.toString()
                val fromKrediiYil=binding.paymentPageKartYil.text.toString()
                val fromKrediiCvv=binding.paymentPageKartCVV.text.toString()
                //tarih
                val dateCurrent = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val baslangicTarihi = dateCurrent.format(formatter)

                val bitisTarihFormatter=(dateCurrent.plusDays(30))
                val bitisTarihi=bitisTarihFormatter.format(formatter)

                //write RealtTimeDatabse Antrenor Ucretleri
                val fromPaymentAntrenor = PaymentData(fromReference.key,fromIsimsoyisim,toUserName,fromUserID,toUserId,fromKrediIsim,fromKrediiAy,
                    fromKrediiYil,fromKrediiCvv,toUserUcret,baslangicTarihi,bitisTarihi)

                fromReference.setValue(fromPaymentAntrenor).addOnSuccessListener {
                    Log.d("PaymentPageFragment","PaymentPageFragment PaymentData RealtimeDatabase Gönderildi.")
                    val action=PaymentPageFragmentDirections.actionPaymentPageFragmentToPersonalChatFragment(toUserId,toUserUcret,toUserName)
                    findNavController().navigate(action)
                }.
                addOnFailureListener { exception ->
                    Log.d("PaymentPageFragment","PaymentPageFragment PaymentData Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }.
            addOnFailureListener{ exception ->
                Log.d("PaymentPageFragment","PaymentPageFragment Profil Bilgileri Alınamadı")
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    enum class CardType {
        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$"),
        MAESTRO("^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d+\$"),
        MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$"),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
        DINERS_CLUB("^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{12,15}$"),
        DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"),
        JCB("^(?:2131|1800|35\\d{3})\\d{11}$"),
        CHINA_UNION_PAY("^62[0-9]{14,17}$");

        private var pattern: Pattern?

        constructor() {
            pattern = null
        }

        constructor(pattern: String) {
            this.pattern = Pattern.compile(pattern)
        }

        companion object {
            fun detect(cardNumber: String?): CardType {
                for (cardType in values()) {
                    if (null == cardType.pattern) continue
                    if (cardType.pattern!!.matcher(cardNumber).matches()) return cardType
                }
                return UNKNOWN
            }
        }
    }

    private fun crediCardType(){
        val kartNumarasi=binding.paymentPageKartNumarasi
        kartNumarasi.addTextChangedListener {
            val kredikartitipi=CardType.detect(kartNumarasi.text.toString())
            binding.paymentPageKrediKartTipi.text=kredikartitipi.toString()
            Log.d("PaymentPageFragmentKrediKartiTipi", kredikartitipi.toString())
        }
    }


    private fun isInputCorrect(): Boolean{
        if (binding.paymentPageUsername.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kredi Kartı Ad Soyad Boş Olmamalı.", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.paymentPageKartNumarasi.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kredi Kartı Kart Numarası Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.paymentPageKartYil.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kredi Kartı Kart Yılı Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.paymentPageKartAy.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kredi Kartı Kart Ay Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.paymentPageKartCVV.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kredi Kartı Kart CVV Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (!binding.paymentPageCheckBox.isChecked){
            Toast.makeText(activity, "Ön Bilgilendirme Formu Okuyup İşaretlenmeli", Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.paymentPageKartNumarasi.text.length!=16){
            Toast.makeText(activity, "Kart Numarasını Yanlış Girdiniz.", Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.paymentPageKartAy.text.length!=2){
            Toast.makeText(activity, "Ay Bilgisini Yanlış Girdiniz.", Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.paymentPageKartYil.text.length!=4){
            Toast.makeText(activity, "Yıl Bilgisini Yanlış Girdiniz.", Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.paymentPageKartCVV.text.length!=3){
            Toast.makeText(activity, "CVV Bilgisini Yanlış Girdiniz.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}