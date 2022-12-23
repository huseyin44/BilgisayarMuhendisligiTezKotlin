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
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntremanProgramlariListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentSporcuUserProfileBinding
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


class SporcuUserProfileFragment : Fragment() {

    private var _binding: FragmentSporcuUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    private lateinit var programlarimAdapter: AntremanProgramlariListRecyclerAdapter
    var programList = ArrayList<WriteProgramData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        antrenorProfileDataShow()
        readProgramlarimData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSporcuUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun antrenorProfileDataShow() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val eposta=result.data?.get("email")
                val boy=result.data?.get("boy")
                val kilo=result.data?.get("kilo")
                val cinsiyet=result.data?.get("cinsiyet")

                binding.sporcuUserProfileIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileCinsiyet.text = cinsiyet.toString().uppercase(Locale.getDefault())
                binding.sporcuUserProfileEposta.text = eposta.toString()
                binding.sporcuUserProfileBoy.text = "BOY : "+boy.toString()+" CM"
                binding.sporcuUserProfileKilo.text = "Kilo : "+kilo.toString()+" Kg"
                Glide.with(this).load(profirresimurl.toString()).into(binding.sporcuUserProfileProfilResmi)
            }
    }

    private fun readProgramlarimData(){
        val ref = FirebaseDatabase.getInstance().getReference("/ProgramWrittenByAntrenor")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val kullanci_mail = result.data?.get("email").toString()
                programList.clear()
                ref.addChildEventListener(object: ChildEventListener {
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val programData = p0.getValue(WriteProgramData::class.java)

                        if (programData != null) {
                            if(kullanci_mail==programData.sporcu_mail){
                                val isim=programData.antrenor_username
                                val program_id=programData.program_id
                                val program_text=programData.program_text
                                val programin_yazildigi_tarih=programData.programin_yazildigi_tarih
                                val antrenor_id=programData.antrenor_id
                                val sporcu_mail=programData.sporcu_mail
                                val veriler= WriteProgramData(program_id,isim,antrenor_id,program_text,sporcu_mail,programin_yazildigi_tarih)

                                programList.add(veriler)
                                Log.d("UserAntrenorProfileFragment","RealtimeDatabase READP ROGRAMLARİM Veriler READ")
                            }
                        }
                        //recyclerview
                        val layoutManager= LinearLayoutManager(context)
                        binding.sporcuUserProfileRecyclerProgramlarim.layoutManager=layoutManager
                        programlarimAdapter= AntremanProgramlariListRecyclerAdapter(programList)
                        binding.sporcuUserProfileRecyclerProgramlarim.adapter=programlarimAdapter
                        //programlarim da program yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                        if(programList.size<1){
                            binding.sporcuUserProfileProgramlarimTextBossa.text="Antreman Programı Bulunamadı"
                            binding.sporcuUserProfileProgramlarimTextBossa.visibility=View.VISIBLE
                        }
                        else{
                            binding.sporcuUserProfileProgramlarimTextBossa.visibility=View.GONE
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                    }
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    }
                    override fun onChildRemoved(p0: DataSnapshot) {
                        programList.clear()
                    }
                })
                //programlarim da program yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                if(programList.size<1){
                    binding.sporcuUserProfileProgramlarimTextBossa.text="Antreman Programı Bulunamadı"
                    binding.sporcuUserProfileProgramlarimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.sporcuUserProfileProgramlarimTextBossa.visibility=View.GONE
                }
            }


    }

}