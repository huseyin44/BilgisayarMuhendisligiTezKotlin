package com.example.huseyinoral_bilgisayarmuhendisligitez.view.sports_exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentSportsExerciseBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SportsExerciseFragment : Fragment() {

    private var _binding: FragmentSportsExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseSportsExcerciseImages()

        binding.sportsexerciseGermeimage.setOnClickListener {
            sportExercisetoFreeExercise()
        }
        binding.sportsexerciseGermetext.setOnClickListener {
            sportExercisetoFreeExercise()
        }

        binding.sportsexerciseJimnastikimage.setOnClickListener {
            sportExercisetoGymnasticExercise()
        }
        binding.sportsexerciseJimnastiktext.setOnClickListener {
            sportExercisetoGymnasticExercise()
        }

        binding.sportsexerciseEkipmanyokimage.setOnClickListener {
            sportExercisetoCalisthenicsExercise()
        }
        binding.sportsexerciseEkipmanyoktext.setOnClickListener {
            sportExercisetoCalisthenicsExercise()
        }
    }

    private fun firebaseSportsExcerciseImages() {
        val storage = Firebase.storage
        val listRef =storage.reference.child("SportsExercise").child("Germe.jpg")
        listRef.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.sportsexerciseGermeimage)
        }
        val listRef2 =storage.reference.child("SportsExercise").child("jimnastik.jpg")
        listRef2.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.sportsexerciseJimnastikimage)
        }
        val listRef3 =storage.reference.child("SportsExercise").child("Ekipmansız.jpg")
        listRef3.downloadUrl.addOnSuccessListener { uri->
            val url=uri.toString()
            Glide.with(this).load(url).into(binding.sportsexerciseEkipmanyokimage)
        }
    }

    private fun sportExercisetoFreeExercise(){
        val action= SportsExerciseFragmentDirections.actionSportsExerciseFragmentToFreeExerciseFragment()
        findNavController().navigate(action)
    }

    private fun sportExercisetoGymnasticExercise(){
        val action= SportsExerciseFragmentDirections.actionSportsExerciseFragmentToGymnasticExercisesFragment()
        findNavController().navigate(action)
    }

    private fun sportExercisetoCalisthenicsExercise(){
        val action= SportsExerciseFragmentDirections.actionSportsExerciseFragmentToCalisthenicsExerciseFragment()
        findNavController().navigate(action)
    }
}