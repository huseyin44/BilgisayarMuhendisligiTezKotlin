package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentBodyMassIndexBinding
import java.text.DecimalFormat

class BodyMassIndexFragment : Fragment() {
    private var _binding: FragmentBodyMassIndexBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodyMassIndexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bmiHesapla.setOnClickListener{
            bmiHesapla()
        }
    }
    private fun bmiHesapla(){
        val kilo=(binding.bmiKilotext.text.toString()).toFloat()
        val boy=(binding.bmiBoytext.text.toString()).toFloat()
        var bmi =((kilo*100) / (boy+boy))
        val df = DecimalFormat("#.##")
        val value: Float = java.lang.Float.valueOf(df.format(bmi))
        if(value < 18.49){
            binding.bmiSonuctext.text="Vücüt Kitle İndeksi : $value  Kilonuz İdeal Kilonun Altındadır"
        }
        else if(18.5<value && value<24.99){
            binding.bmiSonuctext.text="Vücüt Kitle İndeksi : $value  Kilonuz İdealdir"
        }
        else if(25<value && value<29.99){
            binding.bmiSonuctext.text="Vücüt Kitle İndeksi : $value  Kilonuz İdeal Kilonun Üzerindedir"
        }
        else if(value > 30.0){
            binding.bmiSonuctext.text="Vücüt Kitle İndeksi : $value  Kilonuz İdeal Kilonun Üzerindedir"
        }
    }
}