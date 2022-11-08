package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NearByLocationData

class NearByLocationRecyclerAdapter(val nearByLocationList: ArrayList<NearByLocationData>): RecyclerView.Adapter<NearByLocationRecyclerAdapter.NearByLocationListViewHolder>() {

    class NearByLocationListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByLocationRecyclerAdapter.NearByLocationListViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.nearbylocation_recycler_row,parent,false)
        return NearByLocationListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NearByLocationListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_NearbyLocation_name).text = nearByLocationList[position].placesname
        holder.itemView.findViewById<TextView>(R.id.recycler_NearbyLocation_adress).text = nearByLocationList[position].placesadress
        holder.itemView.findViewById<TextView>(R.id.recycler_NearbyLocation_websitesi).text = nearByLocationList[position].placewebsite
        holder.itemView.findViewById<TextView>(R.id.recycler_NearbyLocation_phone_number).text ="Telefon Numarası: "+nearByLocationList[position].placephone
    }

    override fun getItemCount(): Int {
        return nearByLocationList.size
    }

}