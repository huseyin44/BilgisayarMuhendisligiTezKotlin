package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData

class PhotosShareRecyclerAdapter (val photoList:ArrayList<PhotoSharedByAntrenorData>): RecyclerView.Adapter<PhotosShareRecyclerAdapter.PhotosShareListViewHolder>(){
    class PhotosShareListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosShareListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recyclerrow__photo_share,parent,false)
        return PhotosShareListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosShareListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recyclerPhotoShare_isimSoyisim).text=photoList[position].username
        holder.itemView.findViewById<TextView>(R.id.recyclerPhotoShare_yorumText).text=photoList[position].yorumtext
        val userUrl=photoList[position].userphotourl
        Glide.with(holder.itemView.context).load(userUrl).into(holder.itemView.findViewById(R.id.recyclerPhotoShare_profilResmi))
        val photoUrl=photoList[position].userphotourl
        Glide.with(holder.itemView.context).load(photoUrl).into(holder.itemView.findViewById(R.id.recyclerPhotoShare_sharePhoto))
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}