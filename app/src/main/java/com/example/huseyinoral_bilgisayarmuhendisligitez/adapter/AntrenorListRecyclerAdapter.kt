package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        holder.itemView.findViewById<TextView>(R.id.recycler_row_email).text=antrenorList[position].email
        holder.itemView.findViewById<TextView>(R.id.recycler_row_uyetipi).text=antrenorList[position].uyetipi
        holder.itemView.findViewById<TextView>(R.id.recycler_row_isimsoyisim).text=antrenorList[position].isim+" "+antrenorList[position].soyisim
        val url=antrenorList[position].profilresmiurl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.recycler_row_profilresmiurl))
        holder.itemView.findViewById<TextView>(R.id.recycler_row_ucretText).text="Aylık Ucreti : "+antrenorList[position].aylikucret+" TL"

        //PersonalChat
        val toUserid=antrenorList[position].userId
        val toUserProfilUrl=antrenorList[position].profilresmiurl
        val toUserName=antrenorList[position].isim+" "+antrenorList[position].soyisim
        holder.itemView.findViewById<ImageView>(R.id.recycler_row_antrenorList_chatimage).setOnClickListener{
            val action=AntrenorListFragmentDirections.
                actionAntrenorListFragment2ToPersonalChatFragment(toUserid.toString(),toUserProfilUrl.toString(),toUserName.uppercase())
            holder.itemView.findNavController().navigate(action)
        }

        //PaymentPage
        val toUserPayment=antrenorList[position].aylikucret
        holder.itemView.findViewById<ImageView>(R.id.recycler_row_antrenorList_UcretImage).setOnClickListener{
            val action=AntrenorListFragmentDirections.
            actionAntrenorListFragment2ToPaymentPageFragment(toUserid.toString(),toUserPayment.toString(),toUserName.uppercase(),toUserProfilUrl.toString())
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.findViewById<TextView>(R.id.recycler_row_ucretText).setOnClickListener{
            val action=AntrenorListFragmentDirections.
            actionAntrenorListFragment2ToPaymentPageFragment(toUserid.toString(),toUserPayment.toString(),toUserName.uppercase(),toUserProfilUrl.toString())
            holder.itemView.findNavController().navigate(action)
        }
        Log.d("deneme",antrenorList[position].aylikucret.toString())
    }

    override fun getItemCount(): Int {
        return antrenorList.size
    }

}