package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor

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
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorlerimListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.OgrencilerListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
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

class UserAntrenorProfileFragment : Fragment() {

    private var _binding: FragmentUserAntrenorProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore

    private lateinit var photoAdapter: AntrenorPhotoShareReadRecyclerAdapter
    var photoList = ArrayList<PhotoSharedByAntrenorData>()
    private lateinit var programlarimAdapter: AntremanProgramlariListRecyclerAdapter
    var programList = ArrayList<WriteProgramData>()
    private lateinit var ogrencilerimAdapter: OgrencilerListRecyclerAdapter
    var ogrencilerimList = ArrayList<SuccessfulPaymentData>()
    private lateinit var antrenorlerimAdapter: AntrenorlerimListRecyclerAdapter
    var antrenorlerimList = ArrayList<SuccessfulPaymentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        antrenorProfileDataShow()
        readSharedPhotoData()
        readProgramlarimData()
        readOgrencilerimData()
        readAntrenorlerimData()
        binding.antrenorUserProfileOgrencilereProgramYaz.setOnClickListener {
            userAntrenorToWriteProgramActivity()
        }
        binding.antrenorUserProfileBilgilerimiGuncelle.setOnClickListener {
            userAntrenorToUpdateProfile()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAntrenorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun userAntrenorToWriteProgramActivity(){
        val action= UserAntrenorProfileFragmentDirections.actionUserAntrenorProfileFragmentToAntrenorWriteProgramActivity()
        findNavController().navigate(action)
    }
    private fun userAntrenorToUpdateProfile(){
        val action= UserAntrenorProfileFragmentDirections.actionUserAntrenorProfileFragmentToUpdateUserAntrenorProfileFragment()
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
                val aylikucret =result.data?.get("aylikUcret") as String
                val kendinitanit =result.data?.get("antrenorTanit") as String
                val fitnessBool =result.data?.get("fitness") as Boolean
                val kickboxBool =result.data?.get("kickBox") as Boolean
                val yogaBool =result.data?.get("yoga") as Boolean
                val pilatesBool =result.data?.get("pilates") as Boolean
                val yuzmeBool =result.data?.get("yuzme") as Boolean
                val futbolBool =result.data?.get("futbol") as Boolean
                val fitness=if(fitnessBool==true) "fitness" else ""
                val kickbox=if(kickboxBool==true) "kickbox" else ""
                val yoga=if(yogaBool==true) "yoga" else ""
                val pilates=if(pilatesBool==true) "pilates" else ""
                val yuzme=if(yuzmeBool==true) "yuzme" else ""
                val futbol=if(futbolBool==true) "futbol" else ""

                binding.antrenorUserProfileBranslar.text="BRANŞLARI : "+ fitness+" "+kickbox+" "+yoga+" "+pilates+" "+yuzme+" "+futbol
                binding.antrenorUserProfileAntrenorDetayliBilgi.text="HAKKINDA : "+kendinitanit
                binding.antrenorUserProfileIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(
                    Locale.getDefault())
                binding.antrenorUserProfileUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.antrenorUserProfileCinsiyet.text = cinsiyet.toString().uppercase(Locale.getDefault())
                binding.antrenorUserProfileEposta.text = eposta.toString()
                binding.antrenorUserProfileBoy.text = "BOY : "+boy.toString()+" CM"
                binding.antrenorUserProfileKilo.text = "Kilo : "+kilo.toString()+" Kg"
                binding.antrenorUserProfileAylikUcret.text ="Aylık Ücret : " +aylikucret.toString()+" TL"
                Glide.with(this).load(profirresimurl.toString()).into(binding.antrenorUserProfileProfilResmi)
            }
    }

    private fun readSharedPhotoData(){
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/PhotoSharedByAntrenor")
        photoList.clear()
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val photoData = p0.getValue(PhotoSharedByAntrenorData::class.java)

                if (photoData != null) {
                    if(userID==photoData.fromUserId){
                        val isim=photoData.username
                        val userid=photoData.id
                        val profilurl=photoData.userphotourl
                        val tarih=photoData.timestamp
                        val id=photoData.id
                        val yorumtext=photoData.yorumtext
                        val paylasilanFotoUrl=photoData.sharedphotourl
                        val veriler= PhotoSharedByAntrenorData(id,isim,userid,yorumtext, profilurl,paylasilanFotoUrl,tarih)

                        photoList.add(veriler)
                        Log.d("UserAntrenorProfileFragment","RealtimeDatabase SHAREDPHOTO Veriler READ")
                    }
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.antrenorUserProfileRecyclerPaylasimlarim.layoutManager=layoutManager
                photoAdapter= AntrenorPhotoShareReadRecyclerAdapter(photoList)
                binding.antrenorUserProfileRecyclerPaylasimlarim.adapter=photoAdapter
                //paylaşımlarımda da paylaşım yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                if(photoList.size<1){
                    binding.antrenorUserProfilePaylasimlarimTextBossa.text="Paylaşım Bulunamadı"
                    binding.antrenorUserProfilePaylasimlarimTextBossa.visibility=View.VISIBLE
                    Log.d("UserAntrenorProfileFragment","paylaşım bulunamadı")
                }
                else{
                    binding.antrenorUserProfilePaylasimlarimTextBossa.visibility=View.INVISIBLE
                    Log.d("UserAntrenorProfileFragment","paylaşım bulundu photoList.size>0")
                }
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
        //paylaşımlarımda da paylaşım yoksa gizli text viewdeki bilgilendirme mesajı yazsın
        if(photoList.size<1){
            binding.antrenorUserProfilePaylasimlarimTextBossa.text="Paylaşım Bulunamadı"
            binding.antrenorUserProfilePaylasimlarimTextBossa.visibility=View.VISIBLE
            Log.d("UserAntrenorProfileFragment","paylaşım bulunamadı")
        }
        else{
            binding.antrenorUserProfilePaylasimlarimTextBossa.visibility=View.INVISIBLE
            Log.d("UserAntrenorProfileFragment","paylaşım bulundu photoList.size>0")
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
                        binding.antrenorUserProfileRecyclerProgramlarim.layoutManager=layoutManager
                        programlarimAdapter= AntremanProgramlariListRecyclerAdapter(programList)
                        binding.antrenorUserProfileRecyclerProgramlarim.adapter=programlarimAdapter
                        //programlarim da program yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                        if(programList.size<1){
                            binding.antrenorUserProfileProgramlarimTextBossa.text="Antreman Programı Bulunamadı"
                            binding.antrenorUserProfileProgramlarimTextBossa.visibility=View.VISIBLE
                        }
                        else{
                            binding.antrenorUserProfileProgramlarimTextBossa.visibility=View.GONE
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
                    binding.antrenorUserProfileProgramlarimTextBossa.text="Antreman Programı Bulunamadı"
                    binding.antrenorUserProfileProgramlarimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.antrenorUserProfileProgramlarimTextBossa.visibility=View.GONE
                }
            }
    }

    private fun readOgrencilerimData(){
        val ref = FirebaseDatabase.getInstance().getReference("/SuccessfulPayment")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val antrenor_email = result.data?.get("email").toString()
                ogrencilerimList.clear()
                ref.addChildEventListener(object: ChildEventListener {
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val ogrencilerimData = p0.getValue(SuccessfulPaymentData::class.java)

                        if (ogrencilerimData != null) {
                            if(antrenor_email==ogrencilerimData.antrenor_email){
                                val payment_id=ogrencilerimData.payment_id
                                val antrenor_isim=ogrencilerimData.antrenor_username
                                val antrenor_ucret=ogrencilerimData.antrenor_ucret
                                val odeme_tarihi=ogrencilerimData.programin_yazildigi_tarih
                                val odeyen_kullanıcı_id= ogrencilerimData.odeyen_kullanıcı_id
                                val odeyen_kullanıcı_username=ogrencilerimData.odeyen_kullanıcı_username
                                val veriler= SuccessfulPaymentData(payment_id,antrenor_isim,antrenor_email,antrenor_ucret,odeyen_kullanıcı_id,odeyen_kullanıcı_username,odeme_tarihi)

                                ogrencilerimList.add(veriler)
                                Log.d("UserAntrenorProfileFragment","RealtimeDatabase READP OGRENCİLERİM Veriler READ")
                            }
                        }
                        //recyclerview
                        val layoutManager= LinearLayoutManager(context)
                        binding.antrenorUserProfileRecyclerOgrencilerim.layoutManager=layoutManager
                        ogrencilerimAdapter= OgrencilerListRecyclerAdapter(ogrencilerimList)
                        binding.antrenorUserProfileRecyclerOgrencilerim.adapter=ogrencilerimAdapter
                        //öğrencilerimde de öğrenci yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                        if(ogrencilerimList.size<1){
                            binding.antrenorUserProfileOgrencilerimTextBossa.text="Öğrenci Bulunamadı"
                            binding.antrenorUserProfileOgrencilerimTextBossa.visibility=View.VISIBLE
                        }
                        else{
                            binding.antrenorUserProfileOgrencilerimTextBossa.visibility=View.GONE
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                    }
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    }
                    override fun onChildRemoved(p0: DataSnapshot) {
                        ogrencilerimList.clear()
                    }
                })
                //öğrencilerimde de öğrenci yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                if(ogrencilerimList.size<1){
                    binding.antrenorUserProfileOgrencilerimTextBossa.text="Öğrenci Bulunamadı"
                    binding.antrenorUserProfileOgrencilerimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.antrenorUserProfileOgrencilerimTextBossa.visibility=View.GONE
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
                        Log.d("UserAntrenorProfileFragment","RealtimeDatabase READP OGRENCİLERİM Veriler READ")
                    }
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.antrenorUserProfileRecyclerAntrenorlerim.layoutManager=layoutManager
                antrenorlerimAdapter= AntrenorlerimListRecyclerAdapter(antrenorlerimList)
                binding.antrenorUserProfileRecyclerAntrenorlerim.adapter=antrenorlerimAdapter
                //Antrenörde de Antrenör yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                if(antrenorlerimList.size<1){
                    binding.antrenorUserProfileAntrenorlerimTextBossa.text="Antrenör Bulunamadı"
                    binding.antrenorUserProfileAntrenorlerimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.antrenorUserProfileAntrenorlerimTextBossa.visibility=View.GONE
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
            binding.antrenorUserProfileAntrenorlerimTextBossa.text="Antrenör Bulunamadı"
            binding.antrenorUserProfileAntrenorlerimTextBossa.visibility=View.VISIBLE
        }
        else{
            binding.antrenorUserProfileAntrenorlerimTextBossa.visibility=View.GONE
        }
    }
}