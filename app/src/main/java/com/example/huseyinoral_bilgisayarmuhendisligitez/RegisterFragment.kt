package com.example.huseyinoral_bilgisayarmuhendisligitez

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class RegisterFragment : Fragment() {
    //data binding ile ilgili
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    //firebase ile ilgili
    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerRadiogroup.setOnCheckedChangeListener(){group, checkedId ->
            RegisterRadioGrupFun()
        }
        binding.registerButton.setOnClickListener{
            if(isInputCorrect()){
                RegisterPageFun(it)
            }
        }
    }
    private fun registerPageToAntrenorPage(){
        val action=RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }
    //radiobutton check
    private fun RegisterRadioGrupFun():String{
        var uyetipi=""
        if(binding.radiobuttonSporcu.isChecked){
            binding.radiobuttonAntrenor.isChecked=false
            uyetipi="sporcu"
            Log.d("RegisterLog","sporcu")
        }
        else if(binding.radiobuttonAntrenor.isChecked){
            binding.radiobuttonSporcu.isChecked=false
            uyetipi="antrenör"
            Log.d("RegisterLog","antrenör")
        }
        binding.radiobuttonText.text=uyetipi
        return uyetipi
    }

    fun RegisterPageFun(view: View){
        //radiobutton kontrol
        val uyetipi=binding.radiobuttonText.text.toString()
        val email=binding.registerEmail.text.toString()
        val sifre=binding.registerPassword.text.toString()
        val isim =binding.registerNameText.text.toString()
        val soyisim=binding.registerSurnameText.text.toString()

        val postHashMap = hashMapOf<String, Any>()

        //kullanıcı email şifre kaydetme
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener{ task ->
            //kayit başarıyla oluşturulduysa
            if(task.isSuccessful){
                Log.d("RegisterLog","Firebase Auth Başarılı Oluştu")
                //veritabanına diğer verileri kaydetme
                postHashMap.put("email",email)
                postHashMap.put("isim",isim)
                postHashMap.put("soyisim",soyisim)
                postHashMap.put("uyetipi",uyetipi)
                //Kayıt Olduktan Sonra Giriş
                signIn()
                //Firestore a gönderme
                db.collection("UserDetailPost").add(postHashMap).addOnSuccessListener  {
                    Log.d("RegisterLog","Veriler Cloud Store Başarılı Gönderildi")

                    registerPageToAntrenorPage()
                }.addOnFailureListener {  exception ->
                    Log.d("RegisterLog","CollectionFail Veriler Cloud Store Kaydedilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener { exception ->
            Log.d("RegisterLog","Firebase Auth Oluşturulamadı")
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    //email kontrol
    private fun String.isValidEmail() = !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    //kayit ol kontrol
    private fun isInputCorrect(): Boolean {
        val isim = binding.registerNameText.text
        val soyisim =binding.registerSurnameText.text
        val email=binding.registerEmail.text
        val sifre =binding.registerPassword.text

        if (email.isNullOrBlank()) {
            Toast.makeText(activity, "Mail boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (email.toString().isValidEmail().not()) {
            Toast.makeText(activity, "Gecerli Email Adresi Olmalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (!email.toString().contains("@hotmail.com", ignoreCase = true) && !email.toString().contains("@gmail.com", ignoreCase = true)) {
            Toast.makeText(activity, "Email xxx@hotmail.com yada xxx@gmail.com  Formatında olmalıdır.", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.isNullOrBlank()) {
            Toast.makeText(activity, "Şifre boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.toString().length < 6) {
            Toast.makeText(activity, "Şifre 8 karekterden az olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (isim.isNullOrBlank()) {
            Toast.makeText(activity, "İsim boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (soyisim.isNullOrBlank()) {
            Toast.makeText(activity, "Soyisim boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (!binding.radiobuttonSporcu.isChecked && !binding.radiobuttonAntrenor.isChecked) {
            Toast.makeText(activity, "Antrenör yada Sporcu işaretli olmalıdır", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    //Kayıt Olduktan Sonra Direk Giriş Yapması için
    private fun signIn() {
        auth.signInWithEmailAndPassword(
            binding.registerEmail.text.toString(),
            binding.registerPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                registerPageToAntrenorPage()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
