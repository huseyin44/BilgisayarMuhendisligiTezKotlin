package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //kullanici giriş yaptıysa anasayfadan başlasın diye
        val guncelkullanici=auth.currentUser
        if(guncelkullanici!=null){
            loginPageToHomePage()
        }

        binding.loginHesabinizyoksakayitol.setOnClickListener{
            loginPageToRegisterPage()
        }
        binding.loginGirisbutton.setOnClickListener {
            if(isInputCorrect()){
                signIn()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun loginPageToRegisterPage(){
        Log.d("RegisterPageActivity","Anasayfa Butonuna Tıklandı")
        val intent = Intent(this, RegisterPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginPageToHomePage(){
        Log.d("RegisterPageActivity","Anasayfa Butonuna Tıklandı")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signIn() {
        auth.signInWithEmailAndPassword(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginPageToHomePage()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    //email kontrol
    private fun String.isValidEmail() = !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    //giriş ol kontrol
    private fun isInputCorrect(): Boolean {
        val email=binding.loginEmail.text
        val sifre =binding.loginPassword.text

        if (email.isNullOrBlank()) {
            Toast.makeText(this, "Mail boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (email.toString().isValidEmail().not()) {
            Toast.makeText(this, "Gecerli Email Adresi Olmalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.isNullOrBlank()) {
            Toast.makeText(this, "Şifre boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}