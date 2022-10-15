package com.example.huseyinoral_bilgisayarmuhendisligitez.view.sports_exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentFreeExerciseBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.HomePageFragmentDirections
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FreeExerciseFragment : Fragment() {
    private var _binding: FragmentFreeExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreeExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFreeExerciseImages()
    }

    fun firebaseFreeExerciseImages() {
        val storage = Firebase.storage
        val db =Firebase.firestore

        val listRef = storage.reference.child("SportsExercise/FreeExercise").child("isinmahareketlerikacdksurmeli.jpg")
        db.collection("FreeExercise").document("isinmahareketleri").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseIsinmatext.text=text
            listRef.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseIsinmaimage)
            }
        }
        val listRef2 = storage.reference.child("SportsExercise/FreeExercise").child("boyuncevirme.jpg")
        db.collection("FreeExercise").document("boyuncevirme").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseBoyuncevirmetext.text=text
            listRef2.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseBoyunmaimage)
            }
        }
        val listRef3 = storage.reference.child("SportsExercise/FreeExercise").child("arm.jpg")
        db.collection("FreeExercise").document("kolgerme").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseKolgermetext.text=text
            listRef3.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseKolgermemaimage)
            }
        }
        val listRef4 = storage.reference.child("SportsExercise/FreeExercise").child("omuzdondurme.jpg")
        db.collection("FreeExercise").document("omuzdondurme").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseOmuzdondurmetext.text=text
            listRef4.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseOmuzdondurmeimage)
            }
        }
        val listRef5 = storage.reference.child("SportsExercise/FreeExercise").child("Hamstring.jpg")
        db.collection("FreeExercise").document("hamstring").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseHamstringtext.text=text
            listRef5.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseHamstringimaimage)
            }
        }
        val listRef6 = storage.reference.child("SportsExercise/FreeExercise").child("dizkaldirma.jpg")
        db.collection("FreeExercise").document("dizkaldirma").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseDizkaldirmatext.text=text
            listRef6.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseDizkaldirmaimage)
            }
        }
        val listRef7 = storage.reference.child("SportsExercise/FreeExercise").child("jumpingjack.jpg")
        db.collection("FreeExercise").document("jumpkingjack").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseJumpingjacktext.text=text
            listRef7.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseJumpingjackimage)
            }
        }
        val listRef8 = storage.reference.child("SportsExercise/FreeExercise").child("comelme.jpg")
        db.collection("FreeExercise").document("comelme").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseComelmetext.text=text
            listRef8.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseComelmeimage)
            }
        }
        val listRef9 = storage.reference.child("SportsExercise/FreeExercise").child("ipatlama.jpg")
        db.collection("FreeExercise").document("ipatlama").get().addOnSuccessListener { result ->
            val text= result.data?.get("aciklama").toString()
            binding.freeexerciseIpatlamatext.text=text
            listRef9.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Glide.with(this).load(url).into(binding.freeexerciseIpatlamaimage)
            }
        }
    }
}