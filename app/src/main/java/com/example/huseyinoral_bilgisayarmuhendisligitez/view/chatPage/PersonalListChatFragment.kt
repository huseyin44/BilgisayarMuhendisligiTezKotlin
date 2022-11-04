package com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.PersonalListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentPersonalListChatBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.model.PersonalListChatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PersonalListChatFragment : Fragment() {
    private var _binding: FragmentPersonalListChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var personalListAdapter: PersonalListRecyclerAdapter
    var personalList = ArrayList<PersonalListChatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readPersonalListData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalListChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun readPersonalListData(){
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("/PersonalChatList/$fromUserID")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val personalListChat = snapshot.getValue(PersonalListChatData::class.java)

                if (personalListChat != null) {
                    val isim=personalListChat.username
                    val profilurl=personalListChat.userphotourl
                    val personalListId=personalListChat.personalListId
                    val userId=personalListChat.userId
                    val tarih=personalListChat.timestamp

                    val veriler= PersonalListChatData(personalListId,isim,userId,profilurl,tarih)

                    personalList.add(veriler)
                }

                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.personalListRecycler.layoutManager=layoutManager
                personalListAdapter= PersonalListRecyclerAdapter(personalList)
                binding.personalListRecycler.adapter=personalListAdapter
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
}