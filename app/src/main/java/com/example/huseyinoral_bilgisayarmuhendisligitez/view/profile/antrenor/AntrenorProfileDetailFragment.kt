package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.OgrencilerListAntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.OgrencilerListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.SuccessfulPaymentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class AntrenorProfileDetailFragment : Fragment() {

    private var _binding: FragmentAntrenorProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore
    private lateinit var photoAdapter: AntrenorPhotoShareReadRecyclerAdapter
    var photoList = ArrayList<PhotoSharedByAntrenorData>()
    private lateinit var ogrencilerimAdapter: OgrencilerListAntrenorListRecyclerAdapter
    var ogrencilerimList = ArrayList<SuccessfulPaymentData>()

    private val args : AntrenorProfileDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        antrenorProfileDataShow()
        readSharedPhotoData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrenorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.antrenorListOdemeSayfasinaGit.setOnClickListener {
            antrenorProfileToPaymentPage()
        }

        readOgrencilerimData()
    }

    private fun antrenorProfileToPaymentPage(){
        val antrenorId = args.antrenorId
        db.collection("UserDetailPost").document(antrenorId).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val username=isim.toString()+" "+soyisim.toString()
                val aylikucret=result.data?.get("aylikUcret")
                val eposta=result.data?.get("email")

                val action= AntrenorProfileDetailFragmentDirections.actionAntrenorProfileFragmentToCreditCardPaymentPageActivity(eposta.toString(),username,aylikucret.toString())
                findNavController().navigate(action)
            }
    }

    private fun antrenorProfileDataShow() {
        val antrenorId = args.antrenorId

        db.collection("UserDetailPost").document(antrenorId).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim")
                val soyisim = result.data?.get("soyisim")
                val uyetipi = result.data?.get("uyetipi")
                val profirresimurl = result.data?.get("profilresmiurl")
                val aylikucret=result.data?.get("aylikUcret")
                val eposta=result.data?.get("email")
                val kendinitanit =result.data?.get("antrenorTanit")

                val fitness =result.data?.get("fitness")
                val kickbox =result.data?.get("kickBox")
                val yoga =result.data?.get("yoga")
                val pilates =result.data?.get("pilates")
                val yuzme =result.data?.get("yuzme")
                val futbol =result.data?.get("futbol")
                val fitnessString=if(fitness==true) "fitness" else ""
                val kickboxString=if(kickbox==true) "kickbox" else ""
                val yogaString=if(yoga==true) "yoga" else ""
                val pilatesString=if(pilates==true) "pilates" else ""
                val yuzmeString=if(yuzme==true) "yuzme" else ""
                val futbolString=if(futbol==true) "futbol" else ""

                binding.antrenorListBranslar.text = "BRANŞLARI : "+ fitnessString+" "+kickboxString+" "+yogaString+" "+pilatesString+" "+yuzmeString+" "+futbolString
                binding.antrenorProfileIsim.text = isim.toString().uppercase(Locale.getDefault())+" "+soyisim.toString().uppercase(Locale.getDefault())
                binding.antrenorProfileUyetipi.text = uyetipi.toString().uppercase(Locale.getDefault())
                binding.antrenorProfileEposta.text = eposta.toString()
                binding.antrenorProfileAylikucret.text = "Aylık Ücreti : "+aylikucret.toString()+" TL"
                binding.antrenorListAntrenorDetayliBilgi.text = "Antrenör Hakkında Detaylı Bilgi : "+kendinitanit.toString()
                Glide.with(this).load(profirresimurl.toString()).into(binding.antrenorProfileProfilResmi)

            }
    }

    private fun readSharedPhotoData(){
        val antrenorId=args.antrenorId
        val ref = FirebaseDatabase.getInstance().getReference("/PhotoSharedByAntrenor")
        photoList.clear()
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val photoData = p0.getValue(PhotoSharedByAntrenorData::class.java)

                if (photoData != null) {
                    if(antrenorId==photoData.fromUserId){
                        val isim=photoData.username
                        val userid=photoData.id
                        val profilurl=photoData.userphotourl
                        val tarih=photoData.timestamp
                        val id=photoData.id
                        val yorumtext=photoData.yorumtext
                        val paylasilanFotoUrl=photoData.sharedphotourl
                        val veriler= PhotoSharedByAntrenorData(id,isim,userid,yorumtext, profilurl,paylasilanFotoUrl,tarih)

                        photoList.add(veriler)
                        Log.d("AntrenorProfileDetailFragment","RealtimeDatabase Veriler READ")
                    }
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.antrenorProfileRecyclerPaylasilanGonderiler.layoutManager=layoutManager
                photoAdapter= AntrenorPhotoShareReadRecyclerAdapter(photoList)
                binding.antrenorProfileRecyclerPaylasilanGonderiler.adapter=photoAdapter
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
                        binding.antrenorProfileRecyclerAntrenorOgrencileri.layoutManager=layoutManager
                        ogrencilerimAdapter= OgrencilerListAntrenorListRecyclerAdapter(ogrencilerimList)
                        binding.antrenorProfileRecyclerAntrenorOgrencileri.adapter=ogrencilerimAdapter
                        //öğrencilerimde de öğrenci yoksa gizli text viewdeki bilgilendirme mesajı yazsın
                        if(ogrencilerimList.size<1){
                            binding.antrenorProfileOgrencilerimTextBossa.text="Öğrenci Bulunamadı"
                            binding.antrenorProfileOgrencilerimTextBossa.visibility=View.VISIBLE
                        }
                        else{
                            binding.antrenorProfileOgrencilerimTextBossa.visibility=View.GONE
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
                    binding.antrenorProfileOgrencilerimTextBossa.text="Öğrenci Bulunamadı"
                    binding.antrenorProfileOgrencilerimTextBossa.visibility=View.VISIBLE
                }
                else{
                    binding.antrenorProfileOgrencilerimTextBossa.visibility=View.GONE
                }
            }
    }

}