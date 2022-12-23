package com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorPaymentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AntrenorPaymentFragment : Fragment() {
    private var _binding: FragmentAntrenorPaymentBinding? = null
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
        _binding = FragmentAntrenorPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        antrenorProfileDataShow()
        binding.paymentPageAntrenorUcretOdeButton.setOnClickListener {
            paymentPageToCreditCardPage()
        }
    }

    private var antrenorAylikUcret=""

    private fun paymentPageToCreditCardPage(){
        val antrenor_eposta=binding.paymentPageAntrenorEposta.text.toString()
        val antrenor_ucret=antrenorAylikUcret
        val antrenor_isim=binding.paymentPageAntrenorIsim.text.toString()
        val action= AntrenorPaymentFragmentDirections.actionAntrenorPaymentFragmentToCreditCardPaymentPageActivity(antrenor_eposta,antrenor_isim,antrenor_ucret)
        findNavController().navigate(action)
    }

    private fun antrenorProfileDataShow() {
        binding.paymentPageAntrenorBilgileriVisibilityLinearlayout.visibility=View.GONE
        binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
        binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BULUNAMADI."

        binding.paymentPageAntrenorSorgulaButton.setOnClickListener {
            val antrenorEposta = binding.paymentPageAntrenorEmail.text.toString()
            db.collection("UserDetailPost")
                .whereEqualTo("email", antrenorEposta)
                .get()
                .addOnSuccessListener { documents ->
                    for (result in documents) {
                        if(result.data?.get("uyetipi").toString()=="antrenör"){
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

                            binding.paymentPageAntrenorBranslar.text = "BRANŞLARI : "+ fitnessString+" "+kickboxString+" "+yogaString+" "+pilatesString+" "+yuzmeString+" "+futbolString
                            binding.paymentPageAntrenorIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(
                                Locale.getDefault())
                            binding.paymentPageUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                            binding.paymentPageAntrenorEposta.text = eposta.toString()
                            binding.paymentPageAntrenorAylikucret.text = "Aylık Ücreti : "+aylikucret.toString()+" TL"
                            antrenorAylikUcret=aylikucret.toString()
                            binding.paymentPageAntrenorDetayliBilgi.text = "Antrenör Hakkında Detaylı Bilgi : "+kendinitanit.toString()
                            Glide.with(this).load(profirresimurl.toString()).into(binding.paymentPageProfilResmi)

                            binding.paymentPageAntrenorBilgileriVisibilityLinearlayout.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BİLGİLERİ"
                        }
                        else{
                            binding.paymentPageAntrenorBilgileriVisibilityLinearlayout.visibility=View.GONE
                            binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BULUNAMADI."
                        }
                    }
                }
        }
    }
}