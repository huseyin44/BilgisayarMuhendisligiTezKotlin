package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentLoginBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentTipsAndAdviceBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class TipsAndAdviceFragment : Fragment() {
    private var _binding: FragmentTipsAndAdviceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseTipsAndAdviceImages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsAndAdviceBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    fun firebaseTipsAndAdviceImages() {
        val storage = Firebase.storage
        val listRef =storage.reference.child("TipsAndAdvicesImages").child("porsiyonresmi.jpg")
        listRef.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentPorsiyonimage)
        }
        val listRef2 =storage.reference.child("TipsAndAdvicesImages").child("kahvaltiresim.jpg")
        listRef2.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentKahvaltiimage)
        }
        val listRef3 =storage.reference.child("TipsAndAdvicesImages").child("araogun.jpg")
        listRef3.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentAraogunimage)
        }
        val listRef4 =storage.reference.child("TipsAndAdvicesImages").child("proteinresmi.jpg")
        listRef4.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentProteinimage)
        }
        val listRef5 =storage.reference.child("TipsAndAdvicesImages").child("healtyfood.jpg")
        listRef5.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentHealtyfoodimage)
        }
        val listRef6 =storage.reference.child("TipsAndAdvicesImages").child("lifliyigecekler.jpg")
        listRef6.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentLiflifoodimage)
        }
        val listRef7 =storage.reference.child("TipsAndAdvicesImages").child("yagliyiyecek.jpg")
        listRef7.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentYaglifoodimage)
        }
        val listRef8 =storage.reference.child("TipsAndAdvicesImages").child("sekeryok.jpg")
        listRef8.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentSekerimage)
        }
        val listRef9 =storage.reference.child("TipsAndAdvicesImages").child("dahacokyurumek.jpg")
        listRef9.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.tipsAndAdviceFragmentSporimage)
        }
    }


}