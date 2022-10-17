package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
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

        binding.userprofileIptalbutton.setOnClickListener{
            userProfilePage()
        }
        binding.userprofileKaydet.setOnClickListener {
            if(isInputCorrect()){
                firebaseDataUpdate()
            }
        }
    }

    fun firebaseDataUpdate() {
        val uyetipi=binding.userprofileEdittextUyetipi.text.toString()
        val isim =binding.userprofileEdittextIsim.text.toString()
        val soyisim=binding.userprofileEdittextSoyisim.text.toString()

        val storage = Firebase.storage
        val db = Firebase.firestore

        //Resmi Firebase Cloud Stora yükleme
        val secilenGorsel= (activity as MainActivity).secilenUserProfilGorsel
        if (secilenGorsel != null) {
            Log.d("UserProfileUpdate","UserProfile Resmi Seçildi")
            //guncel kullanıcı id alıp ona göre resimleri kaydetme
            val userID = FirebaseAuth.getInstance().currentUser!!.uid
            val reference = storage.reference
            val gorselIsmi = "${userID}.jpg"
            val gorselReference = reference.child("ProfileImages").child(gorselIsmi)

            gorselReference.putFile(secilenGorsel).addOnSuccessListener {
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

        val secilenGorsel= (activity as MainActivity).secilenUserProfilGorsel
        if (secilenGorsel == null){
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
}