package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.UserPublicChatData
import com.google.firebase.auth.FirebaseAuth

class UserPublicRecyclerAdapter(val publicChatMessageList:ArrayList<UserPublicChatData>): RecyclerView.Adapter<UserPublicRecyclerAdapter.UserPublicListViewHolder>(){
    class UserPublicListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPublicListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.userpublic_recycler_row,parent,false)
        return UserPublicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserPublicListViewHolder, position: Int) {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        holder.itemView.findViewById<TextView>(R.id.userpublicRecycler_isimsoyisim).text=publicChatMessageList[position].username
        holder.itemView.findViewById<TextView>(R.id.userpublicRecycler_text).text=publicChatMessageList[position].text
        val url=publicChatMessageList[position].userphotourl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.userpublicRecycler_profileimage))

        if(userID==publicChatMessageList[position].userid){
            holder.itemView.findViewById<TextView>(R.id.userpublicRecycler_isimsoyisim).setTextColor(holder.itemView.resources.getColor(R.color.holo_green_light))
        }
    }

    override fun getItemCount(): Int {
        return publicChatMessageList.size
    }

}
