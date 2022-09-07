package com.example.huseyinoral_bilgisayarmuhendisligitez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    private fun loginPageActions(){
        val action=AntrenorListFragmentDirections.actionAntrenorListFragmentToLoginFragment()
        Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        //Güncel kullanıcıyı alma
        auth = FirebaseAuth.getInstance()
        val guncelkullanici=auth.currentUser
        //Menu Çıkış Yap görünürlüğü
        val menusignout=menu?.findItem(R.id.menu_signout)
        if(guncelkullanici==null){
            menusignout?.isVisible=false
            Log.d("OptionsMenuSignOut","GuncellKullanici Yok")
        }
        else{
            menusignout?.isVisible
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_signout){
            //firebase cıkıs
            auth.signOut()
            item.isVisible=false
            loginPageActions()

        }
        return super.onOptionsItemSelected(item)
    }

}