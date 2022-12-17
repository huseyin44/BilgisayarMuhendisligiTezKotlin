
package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.classes.NavigationItemClass
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainAppBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.mainDrawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appbar hangi fragmentlerde olacagi
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.registerPageActivity2, R.id.loginPageActivity2,R.id.homePageFragment,R.id.tipsAndAdviceFragment,R.id.antrenorListFragment2,
                R.id.userProfileFragment,R.id.sportsExerciseFragment,R.id.bodyMassIndexFragment,R.id.userProfileFragment,R.id.userProfileUpdateFragment,
                R.id.publicChatFragment,R.id.personalListChatFragment,R.id.personalChatFragment,R.id.noteDetailsFragment,R.id.noteTitlePageFragment,
                R.id.stepCounterActivity,R.id.nearByMapsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val naviItemClass= NavigationItemClass()
        //NavigationDrawer Kullanarak Sayfalar Arası Gezinti için
        naviItemClass.navigationMenuItemSelect(navView,navController,binding)
        //NavigationDrawer itemlerinin görünürlüğü
        naviItemClass.navigationDrawerDestinationChanged(navView,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
