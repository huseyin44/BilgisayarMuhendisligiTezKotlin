package com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorSorgulaRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorPaymentBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AntrenorPaymentFragment : Fragment() {
    private var _binding: FragmentAntrenorPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private lateinit var recyclerAntrenorAdapter: AntrenorSorgulaRecyclerAdapter
    var antrenorListesi = ArrayList<AntrenorData>()

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
        //recycler işlemleri
        val layoutManager= LinearLayoutManager(context)
        binding.antrenorSorgulaPaymentRecycler.layoutManager=layoutManager
        recyclerAntrenorAdapter= AntrenorSorgulaRecyclerAdapter(antrenorListesi)
        binding.antrenorSorgulaPaymentRecycler.adapter=recyclerAntrenorAdapter
        antrenorProfileDataShow()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun antrenorProfileDataShow() {
        binding.antrenorSorgulaPaymentRecycler.visibility=View.GONE
        binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
        binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BULUNAMADI."

        binding.paymentPageAntrenorSorgulaButton.setOnClickListener {
            val antrenorIsim = binding.paymentPageAntrenorEmail.text.toString()
            antrenorListesi.clear()
            db.collection("UserDetailPost")
                .whereEqualTo("isim", antrenorIsim)
                .get()
                .addOnSuccessListener { documents ->
                    for (result in documents) {
                        if(result.data?.get("uyetipi").toString()=="antrenör"){
                            val isim = result.data?.get("isim") as String
                            val soyisim = result.data?.get("soyisim") as String
                            val uyetipi = result.data?.get("uyetipi") as String
                            val profirresimurl = result.data?.get("profilresmiurl") as String
                            val aylikucret=result.data?.get("aylikUcret") as String
                            val eposta=result.data?.get("email") as String
                            val kendinitanit =result.data?.get("antrenorTanit") as String
                            val userId =result.data?.get("userId") as String

                            val fitness =result.data?.get("fitness") as Boolean
                            val kickbox =result.data?.get("kickBox") as Boolean
                            val yoga =result.data?.get("yoga") as Boolean
                            val pilates =result.data?.get("pilates") as Boolean
                            val yuzme =result.data?.get("yuzme") as Boolean
                            val futbol =result.data?.get("futbol") as Boolean

                            val indirilenAntrenorList= AntrenorData(eposta,isim,soyisim,uyetipi,profirresimurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                            antrenorListesi.add(indirilenAntrenorList)

                            binding.antrenorSorgulaPaymentRecycler.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BİLGİLERİ"
                        }
                        else{
                            binding.antrenorSorgulaPaymentRecycler.visibility=View.GONE
                            binding.paymentPageAntrenorBilgileriText.visibility=View.VISIBLE
                            binding.paymentPageAntrenorBilgileriText.text="ANTRENÖR BULUNAMADI."
                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
}