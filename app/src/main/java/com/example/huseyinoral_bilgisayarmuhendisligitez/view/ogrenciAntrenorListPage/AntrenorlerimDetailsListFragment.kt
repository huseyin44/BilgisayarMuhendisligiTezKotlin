package com.example.huseyinoral_bilgisayarmuhendisligitez.view.ogrenciAntrenorListPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorlerimDetailsListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentOgrencilerimDetailsListBinding

class AntrenorlerimDetailsListFragment : Fragment() {
    private var _binding: FragmentAntrenorlerimDetailsListBinding? = null
    private val binding get() = _binding!!
    private val args : AntrenorlerimDetailsListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrenorlerimDetailsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ogrencilerimDetailsData()
    }

    private fun ogrencilerimDetailsData(){
        val antrenor_username=args.antrenorUsername
        val ogrencinin_yatirdigi_para=args.ogrencininYatirdigiPara
        val parayi_yatirdigi_tarih=args.ogrencininParaYatirdigiTarih
        binding.ogrencilerimDetailsListOgrenciIsim.text="Antrenör : "+antrenor_username.toUpperCase()
        binding.ogrencilerimDetailsListOgrenciYatirdigiPara.text="Yatırdığım Ücret : "+ogrencinin_yatirdigi_para
        binding.ogrencilerimDetailsListOgrenciYatirdigiParaninTarihi.text="Ücretin Yatırılıdığı Tarih : "+parayi_yatirdigi_tarih
    }
}