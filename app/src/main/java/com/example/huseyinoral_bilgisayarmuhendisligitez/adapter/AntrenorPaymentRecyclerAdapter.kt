package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorPaymentData

class AntrenorPaymentRecyclerAdapter (val antrenorPaymentTitleList:ArrayList<AntrenorPaymentData>): RecyclerView.Adapter<AntrenorPaymentRecyclerAdapter.AntrenorPaymentListViewHolder>() {

    class AntrenorPaymentListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrenorPaymentRecyclerAdapter.AntrenorPaymentListViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.antrenor_payment_recycler_row,parent,false)
        return AntrenorPaymentListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AntrenorPaymentListViewHolder, position: Int) {
        val isim="Hesabınıza"+" "+antrenorPaymentTitleList[position].fromUsername+" Tarafından "+ antrenorPaymentTitleList[position].baslangicTarihi +" Tarihinde "+
                antrenorPaymentTitleList[position].odenenUcret+" TL Yatırılmıştır. Sözleşme Bitiş Tarihi : "+antrenorPaymentTitleList[position].bitisTarihi
        holder.itemView.findViewById<TextView>(R.id.antrenorPayment_recylcer_text).text=isim
    }

    override fun getItemCount(): Int {
        return antrenorPaymentTitleList.size
    }

}