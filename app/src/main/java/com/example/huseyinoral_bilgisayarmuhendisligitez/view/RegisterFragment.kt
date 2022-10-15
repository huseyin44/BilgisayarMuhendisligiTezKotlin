package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class RegisterFragment : Fragment() {
    //data binding ile ilgili
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    //firebase ile ilgili
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerRadiogroup.setOnCheckedChangeListener(){ _, _ ->
            registerRadioGrupFun()
        }
        binding.registerButton.setOnClickListener{
            if(isInputCorrect()){
                registerPageFun()
            }
        }
    }
    private fun registerPageToHomePage(){
        val action=RegisterFragmentDirections.actionRegisterFragment2ToHomePageFragment()
        findNavController().navigate(action)
    }
    //radiobutton check
    private fun registerRadioGrupFun():String{
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

    fun registerPageFun() {
        //radiobutton kontrol
        val uyetipi=binding.radiobuttonText.text.toString()
        val email=binding.registerEmail.text.toString()
        val sifre=binding.registerPassword.text.toString()
        val isim =binding.registerNameText.text.toString()
        val soyisim=binding.registerSurnameText.text.toString()

        //kullanıcı email şifre kaydetme
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener{ task ->
            //kayit başarıyla oluşturulduysa
            if(task.isSuccessful){
                Log.d("RegisterLog","Firebase Auth Başarılı Oluştu")

                val secilenGorsel= (activity as MainActivity).secilenGorsel
                if (secilenGorsel != null) {
                    Log.d("RegisterLog","RegisterLog Profil Resmi Seçildi")
                    //Kayıt Olduktan Sonra Giriş
                    signIn()

                    //guncel kullanıcı id alıp ona göre resimleri kaydetme
                    val userID = FirebaseAuth.getInstance().currentUser!!.uid
                    Log.d("RegisterLog",userID+" Güncel kullanıcı id")
                    //Profil resmi için id oluşturma ve kaydedilecegi yeri belirleme
                    val reference = storage.reference
                    val gorselIsmi = "${userID}.jpg"
                    val gorselReference = reference.child("ProfileImages").child(gorselIsmi)

                    //Resmi Firebase Cloud Stora yükleme
                    gorselReference.putFile(secilenGorsel).addOnSuccessListener {
                        //Resim Cloud Stora gönderildikten sonra onun urlni alma
                        val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("ProfileImages").child(gorselIsmi)
                        yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            //veritabanına diğer verileri kaydetme
                            val postHashMap = hashMapOf<String, Any>()
                            postHashMap["profilresmiurl"] = downloadUrl
                            postHashMap["email"] = email
                            postHashMap["isim"] = isim
                            postHashMap["soyisim"] = soyisim
                            postHashMap["uyetipi"] = uyetipi

                            db.collection("UserDetailPost").document(userID).set(postHashMap).addOnSuccessListener  {
                                Log.d("RegisterLog","Veriler FireStore Database Başarılı Gönderildi")
                                registerPageToHomePage()
                            }.addOnFailureListener {  exception ->
                                Log.d("RegisterLog","Veriler FireStore Kaydedilemedi")
                                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }
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

        val secilenGorsel= (activity as MainActivity).secilenGorsel
        if (secilenGorsel == null){
            Toast.makeText(activity, "Profil resmi seçilmei", Toast.LENGTH_LONG).show()
            return false
        }
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
            Toast.makeText(activity, "Şifre 6 karekterden az olmamalı", Toast.LENGTH_LONG).show()
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
                Log.d("RegisterLog","Firebase Giriş Yapıldı")
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

}
