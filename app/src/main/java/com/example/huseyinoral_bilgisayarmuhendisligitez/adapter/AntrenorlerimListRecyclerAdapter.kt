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
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.sporcu.SporcuUserProfileFragmentDirections

class AntrenorlerimListRecyclerAdapter (val antrenorlerimList:ArrayList<SuccessfulPaymentData>): RecyclerView.Adapter<AntrenorlerimListRecyclerAdapter.AntrenorlerimListViewHolder>(){
    class AntrenorlerimListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrenorlerimListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row_antrenorlerim,parent,false)
        return AntrenorlerimListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AntrenorlerimListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenorler_antrenor_isim).text=antrenorlerimList[position].antrenor_username
        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenorler_antrenor_isim_detayli_bilgi).text="Detaylı Bilgi"
        val ogrencinin_yatirdigi_para=antrenorlerimList[position].antrenor_ucret.toString()
        val ogrencinin_para_yatirdigi_tarih=antrenorlerimList[position].programin_yazildigi_tarih.toString()
        val antrenor_username=antrenorlerimList[position].antrenor_username.toString()

        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenorler_antrenor_isim_detayli_bilgi).setOnClickListener {
            val action= SporcuUserProfileFragmentDirections.actionSporcuUserProfileFragmentToAntrenorlerimDetailsListFragment(antrenor_username,ogrencinin_yatirdigi_para,ogrencinin_para_yatirdigi_tarih)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return antrenorlerimList.size
    }
}