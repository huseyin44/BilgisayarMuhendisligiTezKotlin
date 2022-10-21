package com.example.huseyinoral_bilgisayarmuhendisligitez.classes

import android.app.Activity
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class NavigationItemClass {

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
            navView.menu.findItem(R.id.menu_publicchat).isVisible=false
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
            navView.menu.findItem(R.id.menu_publicchat).isVisible=true
            Log.d("Navigationitemvisibility","GuncellKullanici VAR")
        }
    }

    fun navigationMenuItemSelect(navView: NavigationView,navController:NavController,binding:ActivityMainBinding){
        val auth = FirebaseAuth.getInstance()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_girisyap -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)//navigationview tıklandıktan sonra kapanmasını sağlar
                    navController.navigate(R.id.loginFragment)
                    true
                }
                R.id.menu_anasayfa -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.homePageFragment)
                    true
                }
                R.id.menu_kayitol -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.registerFragment2)
                    true
                }
                R.id.menu_ipucları -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.tipsAndAdviceFragment)
                    true
                }
                R.id.menu_antrenorler -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.antrenorListFragment2)
                    true
                }
                R.id.menu_egzersiz -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.sportsExerciseFragment)
                    true
                }
                R.id.menu_profilduzenle -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.userProfileFragment)
                    true
                }
                R.id.menu_vucutkitleindeksi -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.bodyMassIndexFragment)
                    true
                }
                R.id.menu_publicchat -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.publicChatFragment)
                    true
                }
                R.id.menu_cikisyap -> {
                    auth.signOut()
                    navigationitemvisibility(navView)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.loginFragment)
                    true
                }
                else -> false
            }
        }
    }

    fun navigationDrawerDestinationChanged(navView:NavigationView,navController:NavController){
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id == R.id.antrenorListFragment2) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.registerFragment2) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.homePageFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.tipsAndAdviceFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.loginFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.userProfileFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.bodyMassIndexFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.sportsExerciseFragment) {
                navigationitemvisibility(navView)
            }
            if(destination.id == R.id.publicChatFragment) {
                navigationitemvisibility(navView)
            }
        }
    }

}