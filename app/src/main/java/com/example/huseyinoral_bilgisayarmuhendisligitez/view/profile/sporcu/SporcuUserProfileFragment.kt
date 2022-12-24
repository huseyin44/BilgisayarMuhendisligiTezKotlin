package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.sporcu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntremanProgramlariListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorlerimListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.OgrencilerListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentSporcuUserProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.SuccessfulPaymentData
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
    private lateinit var antrenorlerimAdapter: AntrenorlerimListRecyclerAdapter
    var antrenorlerimList = ArrayList<SuccessfulPaymentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSporcuUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        antrenorProfileDataShow()
        readProgramlarimData()
        readAntrenorlerimData()
        binding.sporcuUserProfileBilgilerimiGuncelle.setOnClickListener {
            sporcuProfileToUpdateProfile()
        }
    }

    private fun sporcuProfileToUpdateProfile(){
        val action= SporcuUserProfileFragmentDirections.actionSporcuUserProfileFragmentToUpdateSporcuProfileFragment()
        findNavController().navigate(action)
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
                                Log.d("SporcuUserProfileFragment","RealtimeDatabase READP ROGRAMLARİM Veriler READ")
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

    private fun readAntrenorlerimData(){
        val ref = FirebaseDatabase.getInstance().getReference("/SuccessfulPayment")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        antrenorlerimList.clear()
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val antrenorlerimData = p0.getValue(SuccessfulPaymentData::class.java)

                if (antrenorlerimData != null) {
                    if(userID==antrenorlerimData.odeyen_kullanıcı_id){
                        val payment_id=antrenorlerimData.payment_id
                        val antrenor_isim=antrenorlerimData.antrenor_username
                        val antrenor_email=antrenorlerimData.antrenor_email
                        val antrenor_ucret=antrenorlerimData.antrenor_ucret
                        val odeme_tarihi=antrenorlerimData.programin_yazildigi_tarih
                        val odeyen_kullanıcı_id= antrenorlerimData.odeyen_kullanıcı_id
                        val odeyen_kullanıcı_username=antrenorlerimData.odeyen_kullanıcı_username
                        val veriler= SuccessfulPaymentData(payment_id,antrenor_isim,antrenor_email,antrenor_ucret,odeyen_kullanıcı_id,odeyen_kullanıcı_username,odeme_tarihi)

                        antrenorlerimList.add(veriler)
                        Log.d("SporcuUserProfileFragment","RealtimeDatabase READP OGRENCİLERİM Veriler READ")
                    }
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.sporcuUserProfileRecyclerAntrenorlerim.layoutManager=layoutManager
                antrenorlerimAdapter= AntrenorlerimListRecyclerAdapter(antrenorlerimList)
                binding.sporcuUserProfileRecyclerAntrenorlerim.adapter=antrenorlerimAdapter
                //Antrenörde de Antrenör yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                if(antrenorlerimList.size<1){
                    binding.sporcuUserProfileAntrenorlerimTextBossa.text="Antrenör Bulunamadı"
                    binding.sporcuUserProfileAntrenorlerimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.sporcuUserProfileAntrenorlerimTextBossa.visibility=View.GONE
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                antrenorlerimList.clear()
            }
        })
        //Antrenörde de Antrenör yoksa gizli text viewdeki bilgilendirme mesajı yazsın
        if(antrenorlerimList.size<1){
            binding.sporcuUserProfileAntrenorlerimTextBossa.text="Antrenör Bulunamadı"
            binding.sporcuUserProfileAntrenorlerimTextBossa.visibility=View.VISIBLE
        }
        else{
            binding.sporcuUserProfileAntrenorlerimTextBossa.visibility=View.GONE
        }
    }

}