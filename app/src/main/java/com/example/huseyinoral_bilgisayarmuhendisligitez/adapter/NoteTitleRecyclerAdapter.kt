package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NoteTitleData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.notePage.NoteTitlePageFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NoteTitleRecyclerAdapter(val noteTitleList:ArrayList<NoteTitleData>): RecyclerView.Adapter<NoteTitleRecyclerAdapter.NoteListViewHolder>() {

    class NoteListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.notetitle_recycler_row,parent,false)
        return NoteListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid

        holder.itemView.findViewById<TextView>(R.id.notetitlleRecycler_title).text=noteTitleList[position].notetitle
        holder.itemView.findViewById<TextView>(R.id.notetitlleRecycler_dateClock).text=noteTitleList[position].timeandate

        val noteId=noteTitleList[position].noteid
        holder.itemView.findViewById<TextView>(R.id.notetitlleRecycler_title).setOnClickListener {
            val action= NoteTitlePageFragmentDirections.actionNoteTitlePageFragmentToNoteDetailsFragment("NoteTitleToNoteDetails",noteId.toString())
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.findViewById<AppCompatImageButton>(R.id.noteTitleRecycler_DeleteButton).setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/NoteList/$fromUserID/$noteId")
            ref.removeValue()
            val action= NoteTitlePageFragmentDirections.actionNoteTitlePageFragmentSelf()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return noteTitleList.size
    }
}