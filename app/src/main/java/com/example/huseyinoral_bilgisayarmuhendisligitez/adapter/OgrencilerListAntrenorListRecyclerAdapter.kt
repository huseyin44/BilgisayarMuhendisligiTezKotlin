package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.SuccessfulPaymentData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor.UserAntrenorProfileFragmentDirections

class OgrencilerListAntrenorListRecyclerAdapter (val ogrencilerList:ArrayList<SuccessfulPaymentData>): RecyclerView.Adapter<OgrencilerListAntrenorListRecyclerAdapter.OgrencilerListViewHolder>(){
    class OgrencilerListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OgrencilerListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row_ogrenciler,parent,false)
        return OgrencilerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: OgrencilerListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_ogrenciler_ogrenci_isim).text=ogrencilerList[position].odeyen_kullanıcı_username
        holder.itemView.findViewById<TextView>(R.id.recycler_row_ogrenciler_ogrenci_isim_detayli_bilgi).text=ogrencilerList[position].programin_yazildigi_tarih
    }

    override fun getItemCount(): Int {
        return ogrencilerList.size
    }
}