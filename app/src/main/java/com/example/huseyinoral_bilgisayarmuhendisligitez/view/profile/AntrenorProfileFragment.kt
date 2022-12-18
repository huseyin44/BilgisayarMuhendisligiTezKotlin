package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage.PersonalChatFragmentArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class AntrenorProfileFragment : Fragment() {

    private var _binding: FragmentAntrenorProfileBinding? = null
    private val binding get() = _binding!!
    val db= Firebase.firestore
    private val args : AntrenorProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        antrenorProfileDataShow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrenorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun antrenorProfileDataShow() {
        val antrenorId = args.antrenorId

        db.collection("UserDetailPost").document(antrenorId).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val aylikucret=result.data?.get("aylikUcret")
                val eposta=result.data?.get("email")
                val kendinitanit =result.data?.get("antrenorTanit")

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

                binding.antrenorListBranslar.text = "BRANŞLARI : "+ fitnessString+" "+kickboxString+" "+yogaString+" "+pilatesString+" "+yuzmeString+" "+futbolString
                binding.antrenorProfileIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(Locale.getDefault())
                binding.antrenorProfileUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.antrenorProfileEposta.text = eposta.toString()
                binding.antrenorProfileAylikucret.text = "Aylık Ücreti : "+aylikucret.toString()+" TL"
                binding.antrenorListAntrenorDetayliBilgi.text = "Antrenör Hakkında Detaylı Bilgi : "+kendinitanit.toString()
                Glide.with(this).load(profirresimurl.toString()).into(binding.antrenorProfileProfilResmi)

            }
    }

}