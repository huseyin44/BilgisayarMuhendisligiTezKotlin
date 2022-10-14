package com.example.huseyinoral_bilgisayarmuhendisligitez

import android.util.Log
import androidx.viewbinding.ViewBinding

import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class NavigationitemVisibilityClass {
    fun navigationitemvisibility(navView: NavigationView){
        //Güncel kullanıcıyı alma
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        val guncelkullanici=auth.currentUser
        //kullanıcı yokken
        if(guncelkullanici==null){
            navView.menu.findItem(R.id.menu_anasayfa).isVisible=false
            navView.menu.findItem(R.id.menu_cikisyap).isVisible=false
            navView.menu.findItem(R.id.menu_kayitol).isVisible=true
            navView.menu.findItem(R.id.menu_girisyap).isVisible=true
            navView.menu.findItem(R.id.menu_antrenorler).isVisible=false
            navView.menu.findItem(R.id.menu_egzersiz).isVisible=false
            navView.menu.findItem(R.id.menu_ipucları).isVisible=false
            navView.menu.findItem(R.id.menu_profilduzenle).isVisible=false
            navView.menu.findItem(R.id.menu_vucutkitleindeksi).isVisible=false
            Log.d("Navigationitemvisibility","GuncellKullanici Yok")
        }
        if(guncelkullanici!=null){
            navView.menu.findItem(R.id.menu_anasayfa).isVisible=true
            navView.menu.findItem(R.id.menu_cikisyap).isVisible=true
            navView.menu.findItem(R.id.menu_kayitol).isVisible=false
            navView.menu.findItem(R.id.menu_girisyap).isVisible=false
            navView.menu.findItem(R.id.menu_antrenorler).isVisible=true
            navView.menu.findItem(R.id.menu_egzersiz).isVisible=true
            navView.menu.findItem(R.id.menu_ipucları).isVisible=true
            navView.menu.findItem(R.id.menu_profilduzenle).isVisible=true
            navView.menu.findItem(R.id.menu_vucutkitleindeksi).isVisible=true
            Log.d("Navigationitemvisibility","GuncellKullanici VAR")
        }
    }
}