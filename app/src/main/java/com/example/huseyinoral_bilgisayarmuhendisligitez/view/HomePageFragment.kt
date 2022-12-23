package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.PhotosShareRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.UserPublicRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentHomePageBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.UserPublicChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    private lateinit var photoAdapter: PhotosShareRecyclerAdapter
    var photoList = ArrayList<PhotoSharedByAntrenorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val guncelkullanici=auth.currentUser
        if(guncelkullanici==null){
            val action = HomePageFragmentDirections.actionHomePageFragmentToLoginPageActivity2()
            findNavController().navigate(action)
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homePageFotografPaylas.setOnClickListener{
            userAntrenorData()
            Log.d("HomePageFragment","tiklandi")
        }
        //antrenorlerin paylaştığı fotoğraflar
        readSharedPhotoData()
    }

    private fun homePageToPhotoSharePage(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToPhotosShareFragment()
        findNavController().navigate(action)
    }

    //antrenör olup olmadığını kontrol ediyior ona göre paylaşım yaptırıyıor
    private fun userAntrenorData(){
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val uyetipi = result.data?.get("uyetipi").toString()
                if (uyetipi=="antrenör"){
                    homePageToPhotoSharePage()
                    Log.d("HomePageFragment","FotoğrafPaylaşma Sayfasına Gidildi")
                }
                else{
                    Toast.makeText(requireActivity(), "Sadece Antrenörler Paylaşım Yapabilir.", Toast.LENGTH_LONG).show()
                    Log.d("HomePageFragment","FotoğrafPaylaşma Sayfasına Gidilemedi !!")
                }
            }
    }

    private fun readSharedPhotoData(){
        val ref = FirebaseDatabase.getInstance().getReference("/PhotoSharedByAntrenor")
        photoList.clear()
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val photoData = p0.getValue(PhotoSharedByAntrenorData::class.java)

                if (photoData != null) {
                    val isim=photoData.username
                    val userid=photoData.id
                    val profilurl=photoData.userphotourl
                    val tarih=photoData.timestamp
                    val id=photoData.id
                    val yorumtext=photoData.yorumtext
                    val paylasilanFotoUrl=photoData.sharedphotourl
                    val veriler= PhotoSharedByAntrenorData(id,isim,userid,yorumtext, profilurl,paylasilanFotoUrl,tarih)

                    photoList.add(veriler)

                    Log.d("HomePageFragment","RealtimeDatabase Veriler READ")
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.homePageRecyclerviewPaylasilanFoto.layoutManager=layoutManager
                photoAdapter= PhotosShareRecyclerAdapter(photoList)
                binding.homePageRecyclerviewPaylasilanFoto.adapter=photoAdapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                photoList.clear()
            }
        })
    }

}