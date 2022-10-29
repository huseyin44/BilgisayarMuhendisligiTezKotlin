package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityRegisterPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList

class RegisterPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPageBinding

    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

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
        Log.d("RegisterPageActivity","Anasayfa Butonuna Tıklandı")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    //radiobutton check
    private fun registerRadioGrupFun():String{
        var uyetipi=""
        if(binding.radiobuttonSporcu.isChecked){
            binding.radiobuttonAntrenor.isChecked=false
            uyetipi="sporcu"
            Log.d("RegisterLog","sporcu")

            binding.registerAylikucret.isVisible=false
        }
        else if(binding.radiobuttonAntrenor.isChecked){
            binding.radiobuttonSporcu.isChecked=false
            uyetipi="antrenör"
            Log.d("RegisterLog","antrenör")

            binding.registerAylikucret.isVisible=true
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
        val aylikucret=binding.registerAylikucret.text.toString()

        //kullanıcı email şifre kaydetme
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener{ task ->
            //kayit başarıyla oluşturulduysa
            if(task.isSuccessful){
                Log.d("RegisterLog","Firebase Auth Başarılı Oluştu")

                val secilenGorsel= secilenGorsel
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
                            postHashMap["userId"]=userID
                            postHashMap["aylikUcret"]=aylikucret

                            db.collection("UserDetailPost").document(userID).set(postHashMap).addOnSuccessListener  {
                                Log.d("RegisterLog","Veriler FireStore Database Başarılı Gönderildi")
                                registerPageToHomePage()
                            }.addOnFailureListener {  exception ->
                                Log.d("RegisterLog","Veriler FireStore Kaydedilemedi")
                                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.d("RegisterLog","Firebase Auth Oluşturulamadı")
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
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

        val secilenGorsel= secilenGorsel
        if (secilenGorsel == null){
            Toast.makeText(this, "Profil resmi seçilmei", Toast.LENGTH_LONG).show()
            return false
        }
        if (email.isNullOrBlank()) {
            Toast.makeText(this, "Mail boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (email.toString().isValidEmail().not()) {
            Toast.makeText(this, "Gecerli Email Adresi Olmalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (!email.toString().contains("@hotmail.com", ignoreCase = true) && !email.toString().contains("@gmail.com", ignoreCase = true)) {
            Toast.makeText(this, "Email xxx@hotmail.com yada xxx@gmail.com  Formatında olmalıdır.", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.isNullOrBlank()) {
            Toast.makeText(this, "Şifre boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.toString().length < 6) {
            Toast.makeText(this, "Şifre 6 karekterden az olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (isim.isNullOrBlank()) {
            Toast.makeText(this, "İsim boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (soyisim.isNullOrBlank()) {
            Toast.makeText(this, "Soyisim boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (!binding.radiobuttonSporcu.isChecked && !binding.radiobuttonAntrenor.isChecked) {
            Toast.makeText(this, "Antrenör yada Sporcu işaretli olmalıdır", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    //GALERİ İZİNLERİYLE İLGİLİ
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    var secilenUserProfilGorsel : Uri? = null
    var secilenUserProfileBitmap2 : Bitmap? = null

    fun registerImageclick(view: View){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //izni almamışız
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //izin zaten varsa
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }
    fun userProfileImageclick(view: View){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //izni almamışız
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //izin zaten varsa
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,3)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
                startActivityForResult(galeriIntent,3)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    val toolbar: ImageView = findViewById<View>(R.id.imageView) as ImageView
                    toolbar.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    val toolbar: ImageView = findViewById<View>(R.id.imageView) as ImageView
                    toolbar.setImageBitmap(secilenBitmap)
                }
            }
        }
        //userprofile profil resmi güncellemesi için
        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            secilenUserProfilGorsel = data.data
            if (secilenUserProfilGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(this.contentResolver, secilenUserProfilGorsel!!)
                    secilenUserProfileBitmap2 = ImageDecoder.decodeBitmap(source)
                    val toolbar: ImageView =
                        findViewById<View>(R.id.userprofile_edit_image) as ImageView
                    toolbar.setImageBitmap(secilenUserProfileBitmap2)
                } else {
                    secilenUserProfileBitmap2 = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        secilenUserProfilGorsel
                    )
                    val toolbar: ImageView =
                        findViewById<View>(R.id.userprofile_edit_image) as ImageView
                    toolbar.setImageBitmap(secilenUserProfileBitmap2)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}