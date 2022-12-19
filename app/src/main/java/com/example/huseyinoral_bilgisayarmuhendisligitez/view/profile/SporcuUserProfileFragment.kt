package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentSporcuUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class SporcuUserProfileFragment : Fragment() {

    private var _binding: FragmentSporcuUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        antrenorProfileDataShow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSporcuUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun antrenorProfileDataShow() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val eposta=result.data?.get("email")
                val boy=result.data?.get("boy")
                val kilo=result.data?.get("kilo")
                val cinsiyet=result.data?.get("cinsiyet")

                binding.sporcuUserProfileIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileCinsiyet.text = cinsiyet.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileEposta.text = eposta.toString()
                binding.sporcuUserProfileBoy.text = "BOY : "+boy.toString()+" CM"
                binding.sporcuUserProfileKilo.text = "Kilo : "+kilo.toString()+" Kg"
                Glide.with(this).load(profirresimurl.toString()).into(binding.sporcuUserProfileProfilResmi)
            }
    }

}