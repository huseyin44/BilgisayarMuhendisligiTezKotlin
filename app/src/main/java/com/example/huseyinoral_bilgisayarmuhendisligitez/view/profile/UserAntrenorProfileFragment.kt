package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorPhotoShareReadRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.PhotosShareRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
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
        _binding = FragmentUserAntrenorProfileBinding.inflate(inflater, container, false)
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
                        Log.d("HomePageFragment","RealtimeDatabase Veriler READ")
                    }
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.antrenorUserProfileRecyclerPaylasimlarim.layoutManager=layoutManager
                photoAdapter= AntrenorPhotoShareReadRecyclerAdapter(photoList)
                binding.antrenorUserProfileRecyclerPaylasimlarim.adapter=photoAdapter
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