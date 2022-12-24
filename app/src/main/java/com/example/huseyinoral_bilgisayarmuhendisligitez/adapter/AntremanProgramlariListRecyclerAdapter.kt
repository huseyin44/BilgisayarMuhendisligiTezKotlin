package com.example.huseyinoral_bilgisayarmuhendisligitez.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.WriteProgramData
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor.UserAntrenorProfileFragmentDirections
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.sporcu.SporcuUserProfileFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AntremanProgramlariListRecyclerAdapter (val AntrenamProgramlariList:ArrayList<WriteProgramData>):
    RecyclerView.Adapter<AntremanProgramlariListRecyclerAdapter.AntrenamProgramlariListViewHolder>() {
    class AntrenamProgramlariListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrenamProgramlariListViewHolder {
        //görünümü burda bağlıyıoruz
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row_programlarim,parent,false)
        return AntrenamProgramlariListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AntrenamProgramlariListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_programlarim_antrenor_username).text="Antrenör : "+AntrenamProgramlariList[position].antrenor_username
        holder.itemView.findViewById<TextView>(R.id.recycler_row_programlarim_programinYazildigiTarih).text=AntrenamProgramlariList[position].programin_yazildigi_tarih

        //sporcu yada antrenor ise ona göre profilden antreman detay sayfasına gitmesi
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        val program_id=AntrenamProgramlariList[position].program_id
        holder.itemView.findViewById<TextView>(R.id.recycler_row_programlarim_programinYazildigiTarih).setOnClickListener{
            db.collection("UserDetailPost").document(userID).get()
                .addOnSuccessListener { result ->
                    val uyeTipi = result.data?.get("uyetipi").toString()
                    if(uyeTipi=="antrenör"){
                        val action= UserAntrenorProfileFragmentDirections.actionUserAntrenorProfileFragmentToAntremanProgramiDetailsFragment(program_id.toString())
                        holder.itemView.findNavController().navigate(action)
                        Log.d("AntrenamProgramlariListRecyclerAdapter",uyeTipi)
                    }
                    if(uyeTipi=="sporcu"){
                        val action= SporcuUserProfileFragmentDirections.actionSporcuUserProfileFragmentToAntremanProgramiDetailsFragment(program_id.toString())
                        holder.itemView.findNavController().navigate(action)
                        Log.d("AntrenamProgramlariListRecyclerAdapter",uyeTipi)
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return AntrenamProgramlariList.size
    }
    }