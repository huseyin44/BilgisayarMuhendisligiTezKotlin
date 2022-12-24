package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntremanProgramlariListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.OgrencilerListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUpdateUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.SuccessfulPaymentData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages.AntrenorPaymentFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class UpdateUserAntrenorProfileFragment : Fragment() {
    private var _binding: FragmentUpdateUserAntrenorProfileBinding? = null
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
        _binding = FragmentUpdateUserAntrenorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        antrenorProfileDataShow()

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
        binding.updateUserAntrenorProfilAylikUcretDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilAylikUcret")
        }
        binding.updateUserAntrenorProfilCinsiyetDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilCinsiyet")
        }
        binding.updateUserAntrenorProfilAntrenorHakkindaDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilHakkinda")
        }
        binding.updateUserAntrenorProfilBranslarDegistir.setOnClickListener {
            updateProfileToUpdateDetails("ProfilBranslar")
        }

    }

    private fun updateProfileToUpdateDetails(yollananVeri : String){
        val action= UpdateUserAntrenorProfileFragmentDirections.actionUpdateUserAntrenorProfileFragmentToUpdateUserAntrenorUpdateDetailsFragment(yollananVeri)
        findNavController().navigate(action)
    }

    private fun antrenorProfileDataShow() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val aylikucret=result.data?.get("aylikUcret")
                val cinsiyet =result.data?.get("cinsiyet")
                val boy =result.data?.get("boy")
                val kilo =result.data?.get("kilo")
                val antrenorTanit =result.data?.get("antrenorTanit")
                val fitness =result.data?.get("fitness")
                val kickbox =result.data?.get("kickBox")
                val yoga =result.data?.get("yoga")
                val pilates =result.data?.get("pilates")
                val yuzme =result.data?.get("yuzme")
                val futbol =result.data?.get("futbol")
                val fitnessString=if(fitness==true) "fitness" else ""
                val kickboxString=if(kickbox==true) "kickbox" else ""
                val yogaString=if(yoga==true) "yoga" else ""
                val pilatesString=if(pilates==true) "pilates" else ""
                val yuzmeString=if(yuzme==true) "yuzme" else ""
                val futbolString=if(futbol==true) "futbol" else ""

                binding.updateUserAntrenorProfilBranslarText.text =  fitnessString+" "+kickboxString+" "+yogaString+" "+pilatesString+" "+yuzmeString+" "+futbolString
                binding.updateUserAntrenorProfilIsimText.text = isim.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilSoyisimText.text = soyisim.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilUyeTipiText.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilCinsiyetText.text = cinsiyet.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilBoyText.text = boy.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilKiloText.text = kilo.toString().uppercase(Locale.getDefault())
                binding.updateUserAntrenorProfilAylKUcretText.text =aylikucret.toString()+" TL"
                binding.updateUserAntrenorProfilHakkindaText.text = "Antrenör Hakkında : "+antrenorTanit.toString()
                Glide.with(this).load(profirresimurl.toString()).into(binding.antrenorUserProfileProfilResmi)

            }
    }
}