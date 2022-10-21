
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
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.classes.NavigationItemClass
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appbar hangi fragmentlerde olacagi
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.registerFragment2, R.id.loginFragment,R.id.homePageFragment,R.id.tipsAndAdviceFragment,R.id.antrenorListFragment2,
                R.id.userProfileFragment,R.id.sportsExerciseFragment,R.id.bodyMassIndexFragment,R.id.userProfileFragment,R.id.userProfileUpdateFragment,
                R.id.publicChatFragment
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

    //GALERİ İZİNLERİYLE İLGİLİ
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    var secilenUserProfilGorsel : Uri? = null
    var secilenUserProfileBitmap2 : Bitmap? = null
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
    fun userProfileImageclick(view: View){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //izni almamışız
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //izin zaten varsa
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,3)
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
                startActivityForResult(galeriIntent,3)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null ) {
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
        //userprofile profil resmi güncellemesi için
        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            secilenUserProfilGorsel = data.data
            if (secilenUserProfilGorsel != null ) {
                if(Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,secilenUserProfilGorsel!!)
                    secilenUserProfileBitmap2 = ImageDecoder.decodeBitmap(source)
                    val toolbar: ImageView = findViewById<View>(R.id.userprofile_edit_image) as ImageView
                    toolbar.setImageBitmap(secilenUserProfileBitmap2)
                } else {
                    secilenUserProfileBitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenUserProfilGorsel)
                    val toolbar: ImageView = findViewById<View>(R.id.userprofile_edit_image) as ImageView
                    toolbar.setImageBitmap(secilenUserProfileBitmap2)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
