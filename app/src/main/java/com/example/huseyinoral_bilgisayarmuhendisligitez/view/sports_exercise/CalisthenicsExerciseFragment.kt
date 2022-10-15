package com.example.huseyinoral_bilgisayarmuhendisligitez.view.sports_exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentCalisthenicsExerciseBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class CalisthenicsExerciseFragment : Fragment() {
    private var _binding: FragmentCalisthenicsExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalisthenicsExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseCalisthenicsExerciseImages()
    }

    fun firebaseCalisthenicsExerciseImages() {
        val storage = Firebase.storage
        val db = Firebase.firestore

        val listRef = storage.reference.child("SportsExercise/CalisthenicsExercise").child("pushup.gif")
        db.collection("CalisthenicsExercise").document("pushup").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExercisePushuptext.text = text
                listRef.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExercisePushupimage)
                }
            }

        val listRef2 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("pikepushup.gif")
        db.collection("CalisthenicsExercise").document("pikepushup").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExercisePikepushuptext.text = text
                listRef2.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExercisePikepushupimage)
                }
            }

        val listRef3 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("bodyweightbentoverrow.gif")
        db.collection("CalisthenicsExercise").document("bodyweightbentoverrow").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseBentovertext.text = text
                listRef3.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseBentoverimage)
                }
            }

        val listRef4 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("Doorway-curl.gif")
        db.collection("CalisthenicsExercise").document("Doorway-curl").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseDoorwaycurltext.text = text
                listRef4.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseDoorwaycurlimage)
                }
            }

        val listRef5 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("chair-triceps-dips.gif")
        db.collection("CalisthenicsExercise").document("chair-triceps-dips").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseTricepstext.text = text
                listRef5.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseTricepsimage)
                }
            }

        val listRef6 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("agirliksiz-squat.gif")
        db.collection("CalisthenicsExercise").document("agirliksiz-squat").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseSquattext.text = text
                listRef6.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseSquatimage)
                }
            }

        val listRef66 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("chair-hip-thrust.gif")
        db.collection("CalisthenicsExercise").document("chair-hip-thrust").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseHipthrusttext.text = text
                listRef66.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseHipthrustimage)
                }
            }

        val listRef7 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("chair-calf-raises.gif")
        db.collection("CalisthenicsExercise").document("chair-calf-raises").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseCallraisestext.text = text
                listRef7.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseCallraisesimage)
                }
            }

        val listRef8 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("mekik-gif.gif")
        db.collection("CalisthenicsExercise").document("mekik-gif").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseCrunchtext.text = text
                listRef8.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseCrunchimage)
                }
            }

        val listRef9 = storage.reference.child("SportsExercise/CalisthenicsExercise").child("lying-side.gif")
        db.collection("CalisthenicsExercise").document("lying-side").get()
            .addOnSuccessListener { result ->
                val text = result.data?.get("aciklama").toString()
                binding.calisthenicsExerciseSidebendstext.text = text
                listRef9.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Glide.with(this).load(url).into(binding.calisthenicsExerciseSidebendsimage)
                }
            }


    }
}