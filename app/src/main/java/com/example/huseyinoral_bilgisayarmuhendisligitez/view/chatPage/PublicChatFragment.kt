package com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.UserPublicRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentPublicChatBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.model.UserPublicChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PublicChatFragment : Fragment() {
    private var _binding: FragmentPublicChatBinding? = null
    private val binding get() = _binding!!

    private val realtimeDatabase=Firebase.database
    private val db= Firebase.firestore

    private lateinit var userPublicMessageAdapter:UserPublicRecyclerAdapter
    var messageList = ArrayList<UserPublicChatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readPublicChatData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.publicChatSendButton.setOnClickListener {
            writePublicChat()
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.getCurrentFocus() == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    private fun writePublicChat() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("UserDetailPost").document(userID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val profirresimurl = result.data?.get("profilresmiurl").toString()
                Log.d("PublicChatFragment","PublicChat Profil Bilgileri Alındı")
                //Realtime PublicChat Verileri yollama
                val isimsoyisim= "$isim $soyisim"
                val publicChatText=binding.publicChatEditText.text.toString()

                val realtimeDatabaseUserPublic=realtimeDatabase.getReference("PublicChat").push()

                val user = UserPublicChatData(realtimeDatabaseUserPublic.key,isimsoyisim,userID,publicChatText,profirresimurl,System.currentTimeMillis() / 1000)

                realtimeDatabaseUserPublic.setValue(user).addOnSuccessListener {
                    Log.d("PublicChatFragment","PublicChat Veriler RealtimeDatabase Gönderildi.")
                }.
                addOnFailureListener { exception ->
                    Log.d("PublicChatFragment","PublicChat Text Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }.
            addOnFailureListener{ exception ->
                Log.d("PublicChatFragment","PublicChat Profil Bilgileri Alınamadı")
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun readPublicChatData(){
        val ref = FirebaseDatabase.getInstance().getReference("/PublicChat")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val publicChatMessage = p0.getValue(UserPublicChatData::class.java)

                if (publicChatMessage != null) {
                    val isim=publicChatMessage.username
                    val userid=publicChatMessage.userid
                    val profilurl=publicChatMessage.userphotourl
                    val tarih=publicChatMessage.timestamp
                    val mesajid=publicChatMessage.messajid
                    val text=publicChatMessage.text
                    val veriler= UserPublicChatData(mesajid,isim,userid,text, profilurl,tarih)

                    messageList.add(veriler)

                    //klavyenin kapanması ve yazının temizlenmesi
                    binding.publicChatEditText.text.clear()
                    activity?.let { it1 -> hideSoftKeyboard(it1) }
                    //sayfanın sondan başlaması
                    binding.publicChatNestedscrollview.post{
                        binding.publicChatNestedscrollview.fullScroll(View.FOCUS_DOWN)
                    }

                    Log.d("PublicChatFragment","RealtimeDatabase Veriler READ")
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.userpublicRecyclerview.layoutManager=layoutManager
                userPublicMessageAdapter= UserPublicRecyclerAdapter(messageList)
                binding.userpublicRecyclerview.adapter=userPublicMessageAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                messageList.clear()
            }
        })
    }
}


