package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUserProfileUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class UserProfileUpdateFragment : Fragment() {
    private var _binding: FragmentUserProfileUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    var secilenProfileUpdateGorsel : Uri? = null
    var secilenProfileUpdateBitmap : Bitmap? = null

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
        _binding = FragmentUserProfileUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //galeri izinleri kontrol
        binding.userprofileEditImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //izni almamışız
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),4)
            } else {
                //izin zaten varsa
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,3)
            }
        }

        binding.userprofileIptalbutton.setOnClickListener{
            userProfilePage()
        }
        binding.userprofileKaydet.setOnClickListener {
            if(isInputCorrect()){
                firebaseDataUpdate()
            }
        }
    }

    private fun firebaseDataUpdate() {
        val uyetipi=binding.userprofileEdittextUyetipi.text.toString()
        val isim =binding.userprofileEdittextIsim.text.toString()
        val soyisim=binding.userprofileEdittextSoyisim.text.toString()
        val aylikucret =binding.userprofileEdittextAylikucret.text

        val storage = Firebase.storage
        val db = Firebase.firestore

        //Resmi Firebase Cloud Stora yükleme
        if (secilenProfileUpdateGorsel != null) {
            Log.d("UserProfileUpdate","UserProfile Resmi Seçildi")
            //guncel kullanıcı id alıp ona göre resimleri kaydetme
            val userID = FirebaseAuth.getInstance().currentUser!!.uid
            val reference = storage.reference
            val gorselIsmi = "${userID}.jpg"
            val gorselReference = reference.child("ProfileImages").child(gorselIsmi)

            gorselReference.putFile(secilenProfileUpdateGorsel!!).addOnSuccessListener {
                //Resim Cloud Stora gönderildikten sonra onun urlni alma
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("ProfileImages").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    //veritabanına diğer verileri kaydetme
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap["profilresmiurl"] = downloadUrl
                    postHashMap["isim"] = isim
                    postHashMap["soyisim"] = soyisim
                    postHashMap["uyetipi"] = uyetipi
                    postHashMap["aylikUcret"] = aylikucret

                    db.collection("UserDetailPost").document(userID).update(postHashMap).addOnSuccessListener  {
                        Log.d("UserProfileUpdate","Veriler FireStore Database Başarılı Gönderildi")
                        userProfilePage()
                        Log.d("UserProfileUpdate","UserProfileFragment e Başarılı Gidildi")
                    }.addOnFailureListener {  exception ->
                        Log.d("UserProfileUpdate","Veriler FireStore Kaydedilemedi")
                        Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isInputCorrect(): Boolean {
        val isim = binding.userprofileEdittextIsim.text
        val soyisim =binding.userprofileEdittextSoyisim.text
        val uyetipi =binding.userprofileEdittextUyetipi.text
        val aylikucret =binding.userprofileEdittextAylikucret.text

        if (secilenProfileUpdateGorsel == null){
            Toast.makeText(activity, "Profil resmi seçilmei", Toast.LENGTH_LONG).show()
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
        if (uyetipi.isNullOrBlank()) {
            Toast.makeText(activity, "Uyetipi boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (aylikucret.isNullOrBlank()) {
            Toast.makeText(activity, "Aylık Ücret boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (uyetipi.toString()!="antrenör" && uyetipi.toString()!="sporcu"){
            Toast.makeText(activity, "Üyetipi antrenör yada sporcu yazmalıdır.", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun userProfilePage(){
        val action=UserProfileUpdateFragmentDirections.actionUserProfileUpdateFragmentToUserProfileFragment( )
        findNavController().navigate(action)
    }

    //GALERİ İZİNLERİ

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 4){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,3)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {
            secilenProfileUpdateGorsel = data.data
            if (secilenProfileUpdateGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, secilenProfileUpdateGorsel!!)
                    secilenProfileUpdateBitmap = ImageDecoder.decodeBitmap(source)
                    binding.userprofileEditImage.setImageBitmap(secilenProfileUpdateBitmap)
                } else {
                    secilenProfileUpdateBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, secilenProfileUpdateGorsel)
                    binding.userprofileEditImage.setImageBitmap(secilenProfileUpdateBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}