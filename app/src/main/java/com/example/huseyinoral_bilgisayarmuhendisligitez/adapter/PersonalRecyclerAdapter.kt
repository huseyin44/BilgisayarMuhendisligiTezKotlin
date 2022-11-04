package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.model.PersonalChatData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.model.UserPublicChatData

class PersonalRecyclerAdapter(val personalChatMessageList:ArrayList<PersonalChatData>): RecyclerView.Adapter<PersonalRecyclerAdapter.PersonalListViewHolder>() {
    class PersonalListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.personal_recycler_row,parent,false)
        return PersonalListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.personalRecycler_isimsoyisim).text=personalChatMessageList[position].username
        holder.itemView.findViewById<TextView>(R.id.personalRecycler_text).text=personalChatMessageList[position].text
        val url=personalChatMessageList[position].userphotourl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.personalRecycler_profileimage))
    }

    override fun getItemCount(): Int {
        return personalChatMessageList.size
    }

}



