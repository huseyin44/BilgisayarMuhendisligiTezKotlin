package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.google.firebase.firestore.FirebaseFirestore

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
        binding.antrenorListFiltre.setOnClickListener {
            filtre()
        }
        binding.antrenorListFiltreSifirla.setOnClickListener {
            binding.antrenorListCheckBoxFitness.setChecked(false)
            binding.antrenorListCheckBoxKickBox.setChecked(false)
            binding.antrenorListCheckBoxYoga.setChecked(false)
            binding.antrenorListCheckBoxPilates.setChecked(false)
            binding.antrenorListCheckBoxYuzme.setChecked(false)
            binding.antrenorListCheckBoxFutbol.setChecked(false)
            readAntrenorListToFirebase()
        }
        //recycler işlemleri
        val layoutManager=LinearLayoutManager(context)
        binding.antrenorListRecyclerView.layoutManager=layoutManager
        recyclerAntrenorAdapter= AntrenorListRecyclerAdapter(antrenorListesi)
        binding.antrenorListRecyclerView.adapter=recyclerAntrenorAdapter
    }

    private fun filtre(){
        if(binding.antrenorListCheckBoxFitness.isChecked){
            readFitnessFilter()
        }
        if(binding.antrenorListCheckBoxKickBox.isChecked){
            readKickBoxFilter()
        }
        if(binding.antrenorListCheckBoxYoga.isChecked){
            readYogaFilter()
        }
        if(binding.antrenorListCheckBoxPilates.isChecked){
            readPilatesFilter()
        }
        if(binding.antrenorListCheckBoxYuzme.isChecked){
            readYuzmeFilter()
        }
        if(binding.antrenorListCheckBoxFutbol.isChecked){
            readFutbolFilter()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readFutbolFilter(){
        database.collection("UserDetailPost").whereEqualTo("futbol",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun readFitnessFilter(){
        database.collection("UserDetailPost").whereEqualTo("fitness",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun readKickBoxFilter(){
        database.collection("UserDetailPost").whereEqualTo("kickBox",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun readYogaFilter(){
        database.collection("UserDetailPost").whereEqualTo("yoga",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun readPilatesFilter(){
        database.collection("UserDetailPost").whereEqualTo("pilates",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun readYuzmeFilter(){
        database.collection("UserDetailPost").whereEqualTo("yuzme",true).addSnapshotListener{ snapshot,exception ->
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
                            val profilresmiurl =document.get("profilresmiurl") as String
                            val userId =document.get("userId") as String
                            val aylikucret =document.get("aylikUcret") as String
                            val kendinitanit =document.get("antrenorTanit") as String
                            val fitness =document.get("fitness") as Boolean
                            val kickbox =document.get("kickBox") as Boolean
                            val yoga =document.get("yoga") as Boolean
                            val pilates =document.get("pilates") as Boolean
                            val yuzme =document.get("yuzme") as Boolean
                            val futbol =document.get("futbol") as Boolean

                            //sadece antrenörler listelensin
                            if(uyetipi=="antrenör"){
                                val indirilenAntrenorList= AntrenorData(email,isim,soyisim,uyetipi,profilresmiurl,userId,aylikucret,kendinitanit,fitness,kickbox,yoga,pilates,yuzme,futbol)
                                antrenorListesi.add(indirilenAntrenorList)
                            }

                        }
                        recyclerAntrenorAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}