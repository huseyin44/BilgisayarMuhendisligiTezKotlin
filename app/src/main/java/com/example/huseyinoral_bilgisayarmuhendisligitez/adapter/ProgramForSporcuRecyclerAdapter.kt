package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData

class ProgramForSporcuRecyclerAdapter (val programList:ArrayList<WriteProgramData>): RecyclerView.Adapter<ProgramForSporcuRecyclerAdapter.ProgramListViewHolder>(){
    class ProgramListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row_programlarim,parent,false)
        return ProgramListViewHolder(view)
        Log.d("ProgramForSporcuRecyclerAdapter","ddddd tıklandı")
    }

    override fun onBindViewHolder(holder: ProgramListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_programlarim_antrenor_username).text="ANTRENÖR : "+programList[position].antrenor_username
        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenor_programinYazildigiTarih).text=programList[position].programin_yazildigi_tarih
        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenor_programinYazildigiTarih).setOnClickListener {
            Log.d("ProgramForSporcuRecyclerAdapter","tarihe tıklandı")
        }

    }

    override fun getItemCount(): Int {
        return programList.size
        Log.d("ProgramForSporcuRecyclerAdapter","aqqqqq tıklandı")
    }

}