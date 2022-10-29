package com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.PersonalRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentPersonalChatBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalChatData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalListChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PersonalChatFragment : Fragment() {
    private var _binding: FragmentPersonalChatBinding? = null
    private val binding get() = _binding!!

    private val db= Firebase.firestore

    private lateinit var personalMessageAdapter: PersonalRecyclerAdapter
    var messageList = ArrayList<PersonalChatData>()

    private val args : PersonalChatFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readPersonalChatData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personalChatSendButton.setOnClickListener {
            writePersonalChat()
        }

    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }



    private fun writePersonalChat() {
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val toUserId=args.personalToUuid
        val toUserProfilUrl=args.toUserProfilUrl
        val toUserName=args.toUserName

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromUserID/$toUserId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toUserId/$fromUserID").push()

        db.collection("UserDetailPost").document(fromUserID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val profirresimurl = result.data?.get("profilresmiurl").toString()

                //Realtime PersonalChat Verileri yollama
                val isimsoyisim= "$isim $soyisim"
                val personalChatText=binding.personalChatEditText.text.toString()

                val userMessage = PersonalChatData(reference.key,isimsoyisim,fromUserID,toUserId,personalChatText,profirresimurl,System.currentTimeMillis() / 1000)

                reference.setValue(userMessage).addOnSuccessListener {
                    Log.d("PersonalChatFragment","PersonalChat FromID Veriler RealtimeDatabase Gönderildi.")
                }.
                addOnFailureListener { exception ->
                    Log.d("PersonalChatFragment","PersonalChat FROMID Text Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

                toReference.setValue(userMessage).addOnSuccessListener {
                    Log.d("PersonalChatFragment","PersonalChat TOID Veriler RealtimeDatabase Gönderildi.")
                }.
                addOnFailureListener { exception ->
                    Log.d("PersonalChatFragment","PersonalChat TOID Text Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

                //PersonalListWrite
                val fromPersonalListReference = FirebaseDatabase.getInstance().getReference("/PersonalChatList/$fromUserID/$toUserId")
                val toPersonalListReference = FirebaseDatabase.getInstance().getReference("/PersonalChatList/$toUserId/$fromUserID")

                val personalFromList=PersonalListChatData(fromPersonalListReference.key,isimsoyisim,fromUserID,profirresimurl,System.currentTimeMillis() / 1000)
                val personalToList=PersonalListChatData(toPersonalListReference.key,toUserName,toUserId,toUserProfilUrl,System.currentTimeMillis() / 1000)

                fromPersonalListReference.setValue(personalToList).addOnSuccessListener {
                    Log.d("PersonalChatFragment","PersonalChat personalList FromUuid Veriler RealtimeDatabase Gönderildi.")
                }.
                addOnFailureListener { exception ->
                    Log.d("PersonalChatFragment","PersonalChat personalList FromUUid Text Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

                toPersonalListReference.setValue(personalFromList).addOnSuccessListener {
                    Log.d("PersonalChatFragment","PersonalChat personalList TOID Veriler RealtimeDatabase Gönderildi.")
                }.
                addOnFailureListener { exception ->
                    Log.d("PersonalChatFragment","PersonalChat personalList TOID Text Gönderilemedi")
                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }.

            addOnFailureListener{ exception ->
                Log.d("PersonalChatFragment","PersonalChat Profil Bilgileri Alınamadı")
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun readPersonalChatData(){
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val toUserId=args.personalToUuid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromUserID/$toUserId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val personalChatMessage = p0.getValue(PersonalChatData::class.java)

                if (personalChatMessage != null) {
                    val isim=personalChatMessage.username
                    val fromuserid=personalChatMessage.fromUserId
                    val touserid=personalChatMessage.toUserId
                    val profilurl=personalChatMessage.userphotourl
                    val tarih=personalChatMessage.timestamp
                    val mesajid=personalChatMessage.messajid
                    val text=personalChatMessage.text
                    val veriler= PersonalChatData(mesajid,isim,fromuserid,touserid,text, profilurl,tarih)

                    messageList.add(veriler)

                    //klavyenin kapanması ve yazının temizlenmesi
                    binding.personalChatEditText.text.clear()
                    activity?.let { it2 -> hideSoftKeyboard(it2) }
                    //sayfanın sondan başlaması
                    binding.personalChatNestedscrollview.post{
                       binding.personalChatNestedscrollview.fullScroll(View.FOCUS_DOWN)
                    }

                    Log.d("PersonalChatFragment","RealtimeDatabase Veriler READ")
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.personalRecyclerview.layoutManager=layoutManager
                personalMessageAdapter= PersonalRecyclerAdapter(messageList)
                binding.personalRecyclerview.adapter=personalMessageAdapter
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