package com.example.huseyinoral_bilgisayarmuhendisligitez.view.ogrenciAntrenorListPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentOgrencilerimDetailsListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages.CreditCardPaymentPageActivityArgs
import com.google.firebase.firestore.FirebaseFirestore


class OgrencilerimDetailsListFragment : Fragment() {
    private var _binding: FragmentOgrencilerimDetailsListBinding? = null
    private val binding get() = _binding!!
    private val args : OgrencilerimDetailsListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOgrencilerimDetailsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ogrencilerimDetailsData()
    }

    private fun ogrencilerimDetailsData(){
        val ogrenci_username=args.ogrenciIsim
        val ogrencinin_yatirdigi_para=args.ogrenciYatirdigiPara
        val parayi_yatirdigi_tarih=args.ogrenciYatirdigiTarih
        binding.ogrencilerimDetailsListOgrenciIsim.text="Öğrenci : "+ogrenci_username.toUpperCase()
        binding.ogrencilerimDetailsListOgrenciYatirdigiPara.text="Yatırdığı Ücret : "+ogrencinin_yatirdigi_para
        binding.ogrencilerimDetailsListOgrenciYatirdigiParaninTarihi.text="Ücretin Yatırılıdığı Tarih : "+parayi_yatirdigi_tarih
    }

}