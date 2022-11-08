package com.example.huseyinoral_bilgisayarmuhendisligitez.view.notePage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.AntrenorListRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.NoteTitleRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.UserPublicRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentNoteDetailsBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentNoteTitlePageBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NoteTitleData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.UserPublicChatData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage.PersonalChatFragmentArgs
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class NoteTitlePageFragment : Fragment() {
    private var _binding: FragmentNoteTitlePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteTitleAdapter: NoteTitleRecyclerAdapter
    var notetitleList = ArrayList<NoteTitleData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readNoteTitleList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteTitlePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteAdd.setOnClickListener {
            noteTitleToNoteDetails()
        }
    }

    private fun noteTitleToNoteDetails(){
        val action=NoteTitlePageFragmentDirections.actionNoteTitlePageFragmentToNoteDetailsFragment("","")
        findNavController().navigate(action)
    }

    private fun readNoteTitleList(){
        val ref = FirebaseDatabase.getInstance().getReference("/NoteList")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val noteTitleList = p0.getValue(NoteTitleData::class.java)

                if (noteTitleList != null) {
                    val noteid=noteTitleList.noteid
                    val title=noteTitleList.notetitle
                    val dateClock=noteTitleList.timeandate
                    val noteText=noteTitleList.notedetails

                    val veriler= NoteTitleData(noteid,title,noteText,dateClock)

                    notetitleList.add(veriler)

                    Log.d("NoteTitleFragment","RealtimeDatabase Veriler READ")
                }
                //recyclerview
                val layoutManager= LinearLayoutManager(context)
                binding.noteTitleRecycler.layoutManager=layoutManager
                noteTitleAdapter= NoteTitleRecyclerAdapter(notetitleList)
                binding.noteTitleRecycler.adapter=noteTitleAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                notetitleList.clear()
            }
        })
    }
}