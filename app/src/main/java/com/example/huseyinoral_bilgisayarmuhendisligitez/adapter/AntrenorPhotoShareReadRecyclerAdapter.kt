package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData

class AntrenorPhotoShareReadRecyclerAdapter (val photoList:ArrayList<PhotoSharedByAntrenorData>): RecyclerView.Adapter<AntrenorPhotoShareReadRecyclerAdapter.PhotosShareListViewHolder>(){
    class PhotosShareListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosShareListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row_antrenor_profile_photo_share,parent,false)
        return PhotosShareListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosShareListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recyclerAntrenorProfilePhotoShare_yorumText).text=photoList[position].yorumtext
        val photoUrl=photoList[position].sharedphotourl
        Glide.with(holder.itemView.context).load(photoUrl).into(holder.itemView.findViewById(R.id.recyclerAntrenorProfilePhotoShare_sharePhoto))
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}