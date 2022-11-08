package com.example.huseyinoral_bilgisayarmuhendisligitez.view.notePage

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentNoteDetailsBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NoteTitleData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NoteDetailsFragment : Fragment() {
    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val realtimeDatabase= Firebase.database
    private val args : NoteDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(args.noteTitleToNoteDetails=="NoteTitleToNoteDetails"){
            readNoteTitleToDetails()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteDetailNoteSaveButton.setOnClickListener {
            writeNote()
            noteDetailsToNoteTitle()
        }

        binding.noteDetailNoteBackButton.setOnClickListener {
            noteDetailsToNoteTitle()
        }

        binding.noteDetailNoteDeleteButton.setOnClickListener {
            deleteNoteDetail()
            noteDetailsToNoteTitle()
        }

        binding.noteDetailNoteUpdateButton.setOnClickListener {
            updateNoteDetail()
        }

        //ses izin
        binding.noteDetailNoteTitleSpeakButton.setOnClickListener {
            speakTitleButton()
        }
        binding.noteDetailNoteDetaySpeakButton.setOnClickListener {
            speakNoteEditButton()
        }
    }

    private fun noteDetailsToNoteTitle(){
        val action=NoteDetailsFragmentDirections.actionNoteDetailsFragmentToNoteTitlePageFragment()
        findNavController().navigate(action)
    }

    private fun writeNote(){
        val dateCurrent = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateClock = dateCurrent.format(formatter)

        val noteTitleText=binding.noteDetailsNotetitleEdit.text.toString()
        val noteDetailsText=binding.noteDetailsNotetDetailTextEdit.text.toString()

        val realtimeDatabaseUserPublic=realtimeDatabase.getReference("NoteList").push()

        val noteDetailsData = NoteTitleData(realtimeDatabaseUserPublic.key,noteTitleText,noteDetailsText,dateClock.toString())

        realtimeDatabaseUserPublic.setValue(noteDetailsData).addOnSuccessListener {
            Log.d("NoteDetailsFragment","NoteDetailsFragment Veriler RealtimeDatabase Gönderildi.")
        }.
        addOnFailureListener { exception ->
            Log.d("NoteDetailsFragment","NoteDetailsFragment Text Gönderilemedi")
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun readNoteTitleToDetails(){
        val noteID=args.noteDetailsId
        val ref = FirebaseDatabase.getInstance().getReference("/NoteList/$noteID")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noteTitleList = snapshot.getValue(NoteTitleData::class.java)

                if (noteTitleList != null) {
                    val title=noteTitleList.notetitle
                    val noteText=noteTitleList.notedetails
                    binding.noteDetailsNotetDetailTextEdit.setText(noteText.toString())
                    binding.noteDetailsNotetitleEdit.setText(title.toString())

                    binding.noteDetailsNotetDetailTextEdit.setOnClickListener{
                        binding.noteDetailsNotetDetailTextEdit.text.clear()
                    }
                    binding.noteDetailsNotetitleEdit.setOnClickListener{
                        binding.noteDetailsNotetitleEdit.text.clear()
                    }
                    Log.d("NoteDetailsFragment","RealtimeDatabase Veriler READ")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun deleteNoteDetail(){
        val noteID=args.noteDetailsId
        val ref = FirebaseDatabase.getInstance().getReference("/NoteList/$noteID")
        ref.removeValue()
    }

    private lateinit var database: DatabaseReference
    private fun updateNoteDetail(){
        val noteID=args.noteDetailsId

        val dateCurrent = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateClock = dateCurrent.format(formatter)
        val noteTitleText=binding.noteDetailsNotetitleEdit.text.toString()
        val noteDetailsText=binding.noteDetailsNotetDetailTextEdit.text.toString()

        val noteDetailsData = NoteTitleData(noteID,noteTitleText,noteDetailsText,dateClock.toString())

        database = Firebase.database.reference
        database.child("NoteList").child(noteID).setValue(noteDetailsData)

        noteDetailsToNoteTitle()
    }

    //Ses izin
    fun speakTitleButton(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bir şey söyleyiniz.")
        startActivityForResult(intent,100)
    }
    fun speakNoteEditButton(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bir şey söyleyiniz.")
        startActivityForResult(intent,101)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //NotTitleSpeakButton
        if (requestCode == 100 && data != null) {
            val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            binding.noteDetailsNotetitleEdit.setText(res[0])
        }
        //NotDetailsSpeakButton
        if (requestCode == 101 && data != null) {
            val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            binding.noteDetailsNotetDetailTextEdit.setText(res[0])
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}