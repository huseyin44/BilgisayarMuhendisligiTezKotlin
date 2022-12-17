package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPaymentRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.PersonalListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUserProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorPaymentData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalListChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    val db= Firebase.firestore

    private lateinit var antrenorPaymentListAdapter: AntrenorPaymentRecyclerAdapter
    var antrenorPayList = ArrayList<AntrenorPaymentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        userProfileAntrenorPaymentDataShow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userprofileGuncelle.setOnClickListener{
            userProfileUpdatePage()
        }
        userProfileDataShow()
    }

    fun userProfileDataShow() {
        //guncel kullanıcı id alıp ona göre resimleri kaydetme
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val aylikucret=result.data?.get("aylikUcret")

                binding.userprofileTextviewIsim.text = isim.toString()
                binding.userprofileTextviewSoyisim.text = soyisim.toString()
                binding.userprofileTextviewUyetipi.text = uyetipi.toString()
                binding.userprofileTextviewAylikucret.text=aylikucret.toString()
                Glide.with(this).load(profirresimurl.toString()).into(binding.userprofileImage)
            }
    }

    fun userProfileAntrenorPaymentDataShow(){
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("/AntrenorPayment/$fromUserID")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val antrenorPaymentList = snapshot.getValue(AntrenorPaymentData::class.java)

                if (antrenorPaymentList != null) {
                    val isim=antrenorPaymentList.fromUsername
                    val odenenucret=antrenorPaymentList.odenenUcret
                    val baslangicTarihi=antrenorPaymentList.baslangicTarihi
                    val bitisTarihi=antrenorPaymentList.bitisTarihi

                    val veriler= AntrenorPaymentData(isim,odenenucret,baslangicTarihi,bitisTarihi)
                    Log.d("UserProfileFragment",veriler.toString())
                    antrenorPayList.add(veriler)
                }

                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.userprofileRecycler.layoutManager=layoutManager
                antrenorPaymentListAdapter= AntrenorPaymentRecyclerAdapter(antrenorPayList)
                binding.userprofileRecycler.adapter=antrenorPaymentListAdapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun userProfileUpdatePage(){
        val action=UserProfileFragmentDirections.actionUserProfileFragmentToUserProfileUpdateFragment()
        findNavController().navigate(action)
    }

}