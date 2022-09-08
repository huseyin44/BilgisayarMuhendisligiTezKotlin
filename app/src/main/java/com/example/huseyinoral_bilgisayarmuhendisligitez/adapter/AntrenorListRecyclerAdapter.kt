package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData

class AntrenorListRecyclerAdapter(val antrenorList:ArrayList<AntrenorData>):RecyclerView.Adapter<AntrenorListRecyclerAdapter.AntrenorListViewHolder>() {
    class AntrenorListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrenorListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.antrenorlist_recycler_row,parent,false)
        return AntrenorListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AntrenorListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_email).text=antrenorList[position].email
        holder.itemView.findViewById<TextView>(R.id.recycler_row_isim).text=antrenorList[position].isim
        holder.itemView.findViewById<TextView>(R.id.recycler_row_soyisim).text=antrenorList[position].soyisim
        holder.itemView.findViewById<TextView>(R.id.recycler_row_uyetipi).text=antrenorList[position].uyetipi
    }

    override fun getItemCount(): Int {
        return antrenorList.size
    }

}