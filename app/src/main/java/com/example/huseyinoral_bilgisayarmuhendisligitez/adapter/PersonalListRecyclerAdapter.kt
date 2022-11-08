package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalListChatData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.AntrenorListFragmentDirections
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage.PersonalListChatFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

class PersonalListRecyclerAdapter(val personalChatList:ArrayList<PersonalListChatData>): RecyclerView.Adapter<PersonalListRecyclerAdapter.PersonalChatListViewHolder>() {

    class PersonalChatListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalChatListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.personalistchat_recycler_row,parent,false)
        return PersonalChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalChatListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.personalListChatRecycler_isimsoyisim).text=personalChatList[position].username
        val isim=personalChatList[position].username
        val url=personalChatList[position].userphotourl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.personalListChatRecycler_profileimage))

        val toUserId=personalChatList[position].userId
        holder.itemView.findViewById<ImageView>(R.id.personalListChatRecycler_profileimage).setOnClickListener{
            val action= PersonalListChatFragmentDirections.actionPersonalListChatFragmentToPersonalChatFragment(toUserId.toString(),url.toString(),isim.toString())
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.findViewById<TextView>(R.id.personalListChatRecycler_isimsoyisim).setOnClickListener{
            val action= PersonalListChatFragmentDirections.actionPersonalListChatFragmentToPersonalChatFragment(toUserId.toString(),url.toString(),isim.toString().uppercase(Locale.ROOT))
            holder.itemView.findNavController().currentDestination?.label=isim.toString()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return personalChatList.size
    }

}