package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentBodyMassIndexBinding

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
            if(isInputCorrect()){
                bmiHesapla()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bmiHesapla(){
        val kilo=(binding.bmiKilotext.text.toString()).toInt()
        val boy=(binding.bmiBoytext.text.toString()).toInt()

        val bmi =((kilo*100) / (boy+boy)).toDouble()

        if(bmi.toDouble() < 18.49){
            binding.bmiSonuctext.text = "Vücut Kitle İndeksi : $bmi  Kilonuz İdeal Kilonun Altındadır"
        }
        else if(18.5 < bmi.toDouble() && bmi.toDouble() < 24.99){
            binding.bmiSonuctext.text = "Vücut Kitle İndeksi : $bmi  Kilonuz İdealdir"
        }
        else if(25 < bmi.toDouble() && bmi.toDouble() < 29.99){
            binding.bmiSonuctext.text =
                "Vücut Kitle İndeksi : $bmi  Kilonuz İdeal Kilonun Üzerindedir"
        }
        else if(bmi.toDouble() > 30.0){
            binding.bmiSonuctext.text = "Vücut Kitle İndeksi : $bmi  Kilonuz İdeal Kilonun Üzerindedir"
        }
        activity?.let { it -> hideSoftKeyboard(it) }
    }

    private fun isInputCorrect(): Boolean{
        if (binding.bmiKilotext.text.isNullOrBlank()) {
            Toast.makeText(activity, "Kilo Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.bmiBoytext.text.isNullOrBlank()) {
            Toast.makeText(activity, "Boy Boş Olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}