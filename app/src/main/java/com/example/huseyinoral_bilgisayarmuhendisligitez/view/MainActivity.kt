
package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.NavigationitemVisibilityClass
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val naviItemVisbilityClass=NavigationitemVisibilityClass()//

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appbar hangi fragmentlerde olacagi
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.registerFragment2, R.id.loginFragment,R.id.homePageFragment,R.id.tipsAndAdviceFragment,R.id.antrenorListFragment2,
                R.id.userProfileFragment,R.id.sportsExerciseFragment,R.id.bodyMassIndexFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Güncel kullanıcıyı alma
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
                    navController.navigate(R.id.tipsAndAdviceFragment)
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
                R.id.menu_cikisyap -> {
                    auth.signOut()
                    naviItemVisbilityClass.navigationitemvisibility(navView)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.loginFragment)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id == R.id.antrenorListFragment2) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.registerFragment2) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.homePageFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.tipsAndAdviceFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.loginFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.userProfileFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.bodyMassIndexFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
            if(destination.id == R.id.sportsExerciseFragment) {
                naviItemVisbilityClass.navigationitemvisibility(navView)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    //GALERİ İZİNLERİYLE İLGİLİ
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    fun registerImageclick(view: View){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //izni almamışız
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //izin zaten varsa
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if(Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    val toolbar: ImageView = findViewById<View>(R.id.imageView) as ImageView
                    toolbar.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    val toolbar: ImageView = findViewById<View>(R.id.imageView) as ImageView
                    toolbar.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
