package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.AntrenorData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.AntrenorListFragmentDirections
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.chatPage.PersonalChatFragment

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
        holder.itemView.findViewById<TextView>(R.id.recycler_row_uyetipi).text=antrenorList[position].uyetipi
        holder.itemView.findViewById<TextView>(R.id.recycler_row_isimsoyisim).text=antrenorList[position].isim+" "+antrenorList[position].soyisim
        val url=antrenorList[position].profilresmiurl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.recycler_row_profilresmiurl))
        holder.itemView.findViewById<TextView>(R.id.recycler_row_ucretText).text="Aylık Ücreti : "+antrenorList[position].aylikucret+" TL"
        //antrenor branşlar
        val fitness=if(antrenorList[position].fitness==true) "fitness" else ""
        val kickbox=if(antrenorList[position].kickbox==true) "kickbox" else ""
        val yoga=if(antrenorList[position].yoga==true) "yoga" else ""
        val pilates=if(antrenorList[position].pilates==true) "pilates" else ""
        val yuzme=if(antrenorList[position].yuzme==true) "yuzme" else ""
        val futbol=if(antrenorList[position].futbol==true) "futbol" else ""
        holder.itemView.findViewById<TextView>(R.id.recycler_row_antrenorBrans).text="BRANŞLARI : "+ fitness+" "+kickbox+" "+yoga+" "+pilates+" "+yuzme+" "+futbol

        //PersonalChat
        val toUserid=antrenorList[position].userId
        val toUserProfilUrl=antrenorList[position].profilresmiurl
        val toUserName=antrenorList[position].isim+" "+antrenorList[position].soyisim
        holder.itemView.findViewById<ImageView>(R.id.recycler_row_antrenorList_chatimage).setOnClickListener{
            val action=AntrenorListFragmentDirections.
                actionAntrenorListFragment2ToPersonalChatFragment(toUserid.toString(),toUserProfilUrl.toString(),toUserName.uppercase())
            holder.itemView.findNavController().navigate(action)
        }

        //AntrenorProfileDetails
        holder.itemView.findViewById<AppCompatButton>(R.id.recycler_row_antrenorDetayliBilgi).setOnClickListener{
            val action=AntrenorListFragmentDirections.actionAntrenorListFragment2ToAntrenorProfileFragment(toUserid.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return antrenorList.size
    }

}