package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentLoginBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AntrenorListFragment : Fragment() {
    private var _binding: FragmentAntrenorListBinding? = null
    private val binding get() = _binding!!

    private lateinit var database:FirebaseFirestore

    private lateinit var recyclerAntrenorAdapter:AntrenorListRecyclerAdapter
    var antrenorListesi = ArrayList<AntrenorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database=FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrenorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readAntrenorListToFirebase()
        //recycler işlemleri
        var layoutManager=LinearLayoutManager(context)
        binding.antrenorListRecyclerView.layoutManager=layoutManager
        recyclerAntrenorAdapter= AntrenorListRecyclerAdapter(antrenorListesi)
        binding.antrenorListRecyclerView.adapter=recyclerAntrenorAdapter
    }
    fun readAntrenorListToFirebase(){
        database.collection("UserDetailPost").addSnapshotListener{ snapshot,exception ->
            //hata varsa
            if (exception !=null){
                Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                //snaphsot null gelebilir onu kontrol
                if(snapshot!=null){
                    //snaphsot boş olabilir boş olmadıgının kontrolu
                    if (!snapshot.isEmpty){
                        val antrenorsnaplist=snapshot.documents
                        antrenorListesi.clear()
                        for(document in antrenorsnaplist){
                            val email =document.get("email") as String
                            val isim =document.get("isim") as String
                            val soyisim =document.get("soyisim") as String
                            val uyetipi =document.get("uyetipi") as String
                            val indirilenAntrenorList=AntrenorData(email,isim,soyisim,uyetipi)
                            antrenorListesi.add(indirilenAntrenorList)
                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }
}