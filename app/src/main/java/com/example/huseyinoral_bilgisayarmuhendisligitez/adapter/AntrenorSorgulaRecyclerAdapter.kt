package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

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
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.ogrenciAntrenorListPage.AntrenorListFragmentDirections
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages.AntrenorPaymentFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView

class AntrenorSorgulaRecyclerAdapter(val antrenorList:ArrayList<AntrenorData>): RecyclerView.Adapter<AntrenorSorgulaRecyclerAdapter.AntrenorListViewHolder>() {
    class AntrenorListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrenorListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_antrenor_sorgula,parent,false)
        return AntrenorListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AntrenorListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.paymentPage_uyetipi).text=antrenorList[position].uyetipi
        holder.itemView.findViewById<TextView>(R.id.paymentPage_antrenor_eposta).text=antrenorList[position].email
        holder.itemView.findViewById<TextView>(R.id.paymentPage_antrenor_isim).text=antrenorList[position].isim+" "+antrenorList[position].soyisim
        val url=antrenorList[position].profilresmiurl
        Glide.with(holder.itemView.context).load(url).into(holder.itemView.findViewById(R.id.paymentPage_profilResmi))
        holder.itemView.findViewById<TextView>(R.id.paymentPage_antrenor_aylikucret).text="Aylık Ücreti : "+antrenorList[position].aylikucret+" TL"
        //antrenor branşlar
        val fitness=if(antrenorList[position].fitness==true) "fitness" else ""
        val kickbox=if(antrenorList[position].kickbox==true) "kickbox" else ""
        val yoga=if(antrenorList[position].yoga==true) "yoga" else ""
        val pilates=if(antrenorList[position].pilates==true) "pilates" else ""
        val yuzme=if(antrenorList[position].yuzme==true) "yuzme" else ""
        val futbol=if(antrenorList[position].futbol==true) "futbol" else ""
        holder.itemView.findViewById<TextView>(R.id.paymentPage_antrenor_branslar).text="BRANŞLARI : "+ fitness+" "+kickbox+" "+yoga+" "+pilates+" "+yuzme+" "+futbol
        holder.itemView.findViewById<TextView>(R.id.paymentPage_antrenorDetayliBilgi).text="Antrenör Hakkında Detaylı Bilgi : "+antrenorList[position].kendinitanit

        //PaymentPAge
        val email=antrenorList[position].email.toString()
        val antrenor_isim=(antrenorList[position].isim+" "+antrenorList[position].soyisim).uppercase()
        val antrenor_ucret=antrenorList[position].aylikucret.toString()
        holder.itemView.findViewById<AppCompatButton>(R.id.paymentPage_antrenor_ucret_ode_button).setOnClickListener{
            val action= AntrenorPaymentFragmentDirections.
            actionAntrenorPaymentFragmentToCreditCardPaymentPageActivity(email,antrenor_isim,antrenor_ucret)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return antrenorList.size
    }
}