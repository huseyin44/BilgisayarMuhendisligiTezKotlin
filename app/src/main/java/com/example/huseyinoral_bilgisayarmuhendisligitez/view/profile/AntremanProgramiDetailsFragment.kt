package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntremanProgramiDetailsBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class AntremanProgramiDetailsFragment : Fragment() {
    private var _binding: FragmentAntremanProgramiDetailsBinding? = null
    private val binding get() = _binding!!

    val db= Firebase.firestore

    private val args : AntremanProgramiDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntremanProgramiDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readProgramDetailsData()
    }

    private fun readProgramDetailsData(){
        val antreman_program_id = args.antremanProgramiId
        val ref = FirebaseDatabase.getInstance().getReference("/ProgramWrittenByAntrenor")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val programData = p0.getValue(WriteProgramData::class.java)

                if (programData != null) {
                    if(antreman_program_id==programData.program_id){
                        val isim=programData.antrenor_username
                        val tarih=programData.programin_yazildigi_tarih
                        val antreman_programi=programData.program_text
                        binding.antremanDetayAntrenorIsmi.text=isim.toString()
                        binding.antremanDetayPrograminYazildigiTarih.text=tarih.toString()
                        binding.antremanDetayPrograminDetaylari.text=antreman_programi.toString()
                        Log.d("AntremanProgramDetailsFragment","RealtimeDatabase Veriler READ")
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

}