package com.example.huseyinoral_bilgisayarmuhendisligitez.view.sports_exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentFreeExerciseBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentGymnasticExercisesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class GymnasticExercisesFragment : Fragment() {
    private var _binding: FragmentGymnasticExercisesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGymnasticExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseGymnasticExerciseImages()
    }

    fun firebaseGymnasticExerciseImages() {
        val storage = Firebase.storage
        val db = Firebase.firestore

        val listRef = storage.reference.child("SportsExercise/JimnastikExercise").child("takla.jpg")
        db.collection("JimnastikExercise").document("onevegeriyetakla").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.gymnasticexerciseTaklatext.text = text
                listRef.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.gymnasticexerciseTaklaimage)
                }
            }

        val listRef2 = storage.reference.child("SportsExercise/JimnastikExercise").child("amut.jpg")
        db.collection("JimnastikExercise").document("amut").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.gymnasticexerciseAmuttext.text = text
                listRef2.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.gymnasticexerciseAmutimage)
                }
            }

        val listRef3 = storage.reference.child("SportsExercise/JimnastikExercise").child("split.jpg")
        db.collection("JimnastikExercise").document("split").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.gymnasticexerciseSplittext.text = text
                listRef3.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.gymnasticexerciseSplitimage)
                }
            }

        val listRef4 = storage.reference.child("SportsExercise/JimnastikExercise").child("onekopru.jpg")
        db.collection("JimnastikExercise").document("onedogrukopru").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.gymnasticexerciseOnekoprutext.text = text
                listRef4.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.gymnasticexerciseOnekopruimage)
                }
            }
    }
}