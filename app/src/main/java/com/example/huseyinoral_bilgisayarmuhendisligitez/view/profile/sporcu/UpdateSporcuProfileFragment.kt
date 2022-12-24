package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.sporcu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUpdateSporcuProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUpdateUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor.UpdateUserAntrenorProfileFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class UpdateSporcuProfileFragment : Fragment() {
    private var _binding: FragmentUpdateSporcuProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateSporcuProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sporcuProfileDataShow()

        binding.updateUserAntrenorProfilResmiDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilResmi")
        }
        binding.updateUserAntrenorProfilIsimDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilIsim")
        }
        binding.updateUserAntrenorProfilSoyIsimDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilSoyisim")
        }
        binding.updateUserAntrenorProfilKiloDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilKilo")
        }
        binding.updateUserAntrenorProfilBoyDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilBoy")
        }
        binding.updateUserAntrenorProfilUyeTipiDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilUyeTipi")
        }
        binding.updateUserAntrenorProfilCinsiyetDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilCinsiyet")
        }
    }

    private fun updateProfileToUpdateDetails(yollananVeri : String){
        val action= UpdateSporcuProfileFragmentDirections.actionUpdateSporcuProfileFragmentToUpdateSporcuProfileDetailsFragment(yollananVeri)
        findNavController().navigate(action)
    }

    private fun sporcuProfileDataShow() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val cinsiyet =result.data?.get("cinsiyet")
                val boy =result.data?.get("boy")
                val kilo =result.data?.get("kilo")

                binding.updateUserAntrenorProfilIsimText.text = isim.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilSoyisimText.text = soyisim.toString().uppercase(
                    Locale.getDefault())
                binding.updateUserAntrenorProfilUyeTipiText.text = uyetipi.toString().uppercase(
                    Locale.getDefault())
                binding.updateUserAntrenorProfilCinsiyetText.text = cinsiyet.toString().uppercase(
                    Locale.getDefault())
                binding.updateUserAntrenorProfilBoyText.text = boy.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilKiloText.text = kilo.toString().uppercase(Locale.getDefault())
                Glide.with(this).load(profirresimurl.toString()).into(binding.antrenorUserProfileProfilResmi)
            }
    }
}