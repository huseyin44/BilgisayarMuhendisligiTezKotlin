package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityAntrenorWriteProgramBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.android.material.navigation.NavigationView
import java.util.*


class AntrenorWriteProgramActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAntrenorWriteProgramBinding
    private val db= Firebase.firestore
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAntrenorWriteProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeAntrenorProgramEkleButton.setOnClickListener {
            Log.d("AntrenorWriteProgramActivityLog","Program Ekle butonuna tıklandı")
            writeProgramForSporcu()
        }
        binding.writeAntrenorProgramTextSpeakButton.setOnClickListener {
            speakProgramWriteButton()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun antrenorWriteProgramToChatPage(){
        val intent = Intent(this@AntrenorWriteProgramActivity, MainActivity::class.java)
        intent.putExtra("AntrenorWriteToAntrenorProfile","AntrenorWriteToAntrenorProfileSucces")
        finish()
        startActivity(intent)
        Log.d("AntrenorWriteProgramActivityLog","Profile Dönüldü")
    }

    private fun isInputCorrect(): Boolean {
        val sporcu_mail=binding.writeAntrenorMail.text.toString()
        val programText=binding.writeAntrenorProgramText.text.toString()

        if(sporcu_mail.isNullOrBlank()) {
            Toast.makeText(this, "Sporcu Mail boş olmamalı.", Toast.LENGTH_LONG).show()
            return false
        }
        if(programText.isNullOrBlank()) {
            Toast.makeText(this, "Program kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun writeProgramForSporcu() {
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val writeProgramReference = FirebaseDatabase.getInstance().getReference("/ProgramWrittenByAntrenor").push()
        val sporcu_mail = binding.writeAntrenorMail.text.toString()
        val programText = binding.writeAntrenorProgramText.text.toString()
        //tarih
        val dateCurrent = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val programinYazildigiTarih = dateCurrent.format(formatter)

        db.collection("UserDetailPost").document(fromUserID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val isimsoyisim = "$isim $soyisim"
                if (isInputCorrect()) {
                    val sharedPhotoFirebase = WriteProgramData(
                        writeProgramReference.key,
                        isimsoyisim,
                        fromUserID,
                        programText,
                        sporcu_mail,
                        programinYazildigiTarih
                    )
                    writeProgramReference.setValue(sharedPhotoFirebase).addOnSuccessListener {
                        Log.d("AntrenorWriteProgramActivity", "AntrenorWriteProgramActivity Paylaşılan Veriler RealtimeDatabase Gönderildi.")
                        //veriler başarılı gitti profile dönüş
                        antrenorWriteProgramToChatPage()
                    }.addOnFailureListener { exception ->
                        Log.d("AntrenorWriteProgramActivity", "AntrenorWriteProgramActivity Paylaşılan Veriler Gönderilemedi !!")
                        Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }.
            addOnFailureListener{ exception ->
                Log.d("AntrenorWriteProgramActivity","Kullanıcı Profil Bilgileri Alınamadı")
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    //Ses izin
    fun speakProgramWriteButton(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bir şey söyleyiniz.")
        startActivityForResult(intent,455)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //SpeakButtonPRogramYaz
        if (requestCode == 455 && data != null) {
            val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            binding.writeAntrenorProgramText.setText(res[0])
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}