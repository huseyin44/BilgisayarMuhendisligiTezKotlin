package com.example.huseyinoral_bilgisayarmuhendisligitez.view.profile.antrenor.userWriteAntrenor

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.huseyinoral_bilgisayarmuhendisligitez.R
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUpdateUserAntrenorProfileBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentUpdateUserAntrenorUpdateDetailsBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.view.paymentPages.CreditCardPaymentPageActivityArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class UpdateUserAntrenorUpdateDetailsFragment : Fragment() {
    private var _binding: FragmentUpdateUserAntrenorUpdateDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    val db= Firebase.firestore
    private lateinit var storage : FirebaseStorage
    private val args : UpdateUserAntrenorUpdateDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserAntrenorUpdateDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //galeri izin kontrol
        binding.registerPageImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //izni almamışız
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),321)
            } else {
                //izin zaten varsa
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,322)
            }
        }

        val gelenProfilItem=args.changeProfilItem
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        profileItemVisibility()

        binding.registerRadiogroup.setOnCheckedChangeListener(){ _, _ ->
            registerAntrenorRadioFun()
        }
        binding.registerRadiogroupCinsiyet.setOnCheckedChangeListener { group, checkedId ->
            registerCinsiyetRadioFun()
        }

        binding.buttonGuncelle.setOnClickListener {
            val isim=binding.registerNameText.text.toString()
            val soyisim=binding.registerSurnameText.text.toString()
            val boy=binding.registerBoy.text.toString()
            val kilo=binding.registerKilo.text.toString()
            val aylikUcret=binding.registerAylikucret.text.toString()
            val hakkinda=binding.registerAntrenorTanit.text.toString()
            val uyetipi=binding.radiobuttonText.text.toString()
            val cinsiyet=binding.radiobuttonCinsiyetText.text.toString()
            //egitmen ceşitleri
            val fitness= binding.checkBoxFitness.isChecked
            val kickBox=binding.checkBoxKickBox.isChecked
            val yoga=binding.checkBoxYoga.isChecked
            val pilates=binding.checkBoxPilates.isChecked
            val yuzme=binding.checkBoxYuzme.isChecked
            val futbol=binding.checkBoxFutbol.isChecked

            if(gelenProfilItem=="ProfilResmi"){
                val reference = storage.reference
                val gorselIsmi = "${userID}.jpg"
                val gorselReference = reference.child("ProfileImages").child(gorselIsmi)

                if (secilenRegisterGorsel == null){
                    Toast.makeText(requireContext(), "Profil resmi seçilmeli.", Toast.LENGTH_LONG).show()
                }
                else{
                    //Resmi Firebase Cloud Stora yükleme
                    gorselReference.putFile(secilenRegisterGorsel!!).addOnSuccessListener {
                        //Resim Cloud Stora gönderildikten sonra onun urlni alma
                        val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("ProfileImages").child(gorselIsmi)
                        yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            db.collection("UserDetailPost").document(userID).update("profilresmiurl",downloadUrl).addOnSuccessListener {
                                updateProfileDetailsToAntrenorProfile()
                            }.addOnFailureListener{exception ->
                                Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                            }
                        }
                    }
                }
            }

            if(gelenProfilItem=="ProfilIsim"){
                if(isim.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "İsim kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("isim",isim).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilSoyisim"){
                if(soyisim.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Soyisim kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("soyisim",soyisim).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilKilo"){
                if(kilo.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Kilo kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("kilo",kilo).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilBoy"){
                if(boy.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Boy kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("boy",boy).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilUyeTipi"){
                if(!binding.radiobuttonSporcu.isChecked && !binding.radiobuttonAntrenor.isChecked) {
                    Toast.makeText(requireContext(), "Antrenör yada Sporcu işaretli olmalıdır", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("uyetipi",uyetipi).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilAylikUcret"){
                if(aylikUcret.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Aylık Ücret kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("aylikUcret",aylikUcret).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilCinsiyet"){
                if(!binding.radiobuttonKadin.isChecked && !binding.radiobuttonErkek.isChecked) {
                    Toast.makeText(requireContext(), "Cinsiyet işaretli olmalıdır", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("cinsiyet",cinsiyet).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilHakkinda"){
                if(hakkinda.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Hakkında kısmı boş olmamalı.", Toast.LENGTH_LONG).show()
                }
                else{
                    db.collection("UserDetailPost").document(userID).update("antrenorTanit",hakkinda).addOnSuccessListener {
                        updateProfileDetailsToAntrenorProfile()
                    }.addOnFailureListener{exception ->
                        Log.d("UpdateUserAntrenorUpdateDetailsFragment","Veriler FireStore Kaydedilemedi")
                    }
                }
            }

            if(gelenProfilItem=="ProfilBranslar"){
                db.collection("UserDetailPost").document(userID).update("fitness",fitness)
                db.collection("UserDetailPost").document(userID).update("kickBox",kickBox)
                db.collection("UserDetailPost").document(userID).update("yoga",yoga)
                db.collection("UserDetailPost").document(userID).update("pilates",pilates)
                db.collection("UserDetailPost").document(userID).update("yuzme",yuzme)
                db.collection("UserDetailPost").document(userID).update("futbol",futbol)
                updateProfileDetailsToAntrenorProfile()
            }
        }
    }

    private fun updateProfileDetailsToAntrenorProfile(){
        val action= UpdateUserAntrenorUpdateDetailsFragmentDirections.actionUpdateUserAntrenorUpdateDetailsFragmentToUpdateUserAntrenorProfileFragment()
        findNavController().navigate(action)
    }

    //radiobutton kadın erkek
    private fun registerCinsiyetRadioFun():String{
        var cinsiyet=""
        if(binding.radiobuttonKadin.isChecked){
            binding.radiobuttonErkek.isChecked=false
            cinsiyet="kadın"
            Log.d("RegisterLog","kadin")
        }
        else if(binding.radiobuttonErkek.isChecked){
            binding.radiobuttonKadin.isChecked=false
            cinsiyet="erkek"
            Log.d("RegisterLog","erkek")
        }
        binding.radiobuttonCinsiyetText.text=cinsiyet
        return cinsiyet
    }
    //radiobutton sporcu antrenor check
    private fun registerAntrenorRadioFun():String{
        var uyetipi=""
        if(binding.radiobuttonSporcu.isChecked){
            binding.radiobuttonAntrenor.isChecked=false
            uyetipi="sporcu"
        }

        else if(binding.radiobuttonAntrenor.isChecked){
            binding.radiobuttonSporcu.isChecked=false
            uyetipi="antrenör"
        }
        binding.radiobuttonText.text=uyetipi
        return uyetipi
    }

    private fun profileItemVisibility(){
        val gelenProfilItem=args.changeProfilItem
        if(gelenProfilItem=="ProfilResmi"){
            binding.textView2.text="Profil Resmini Değiştir"
            binding.registerPageImageView.visibility=View.VISIBLE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilIsim"){
            binding.textView2.text="İsim Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.VISIBLE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilSoyisim"){
            binding.textView2.text="Soyisim Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.VISIBLE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilKilo"){
            binding.textView2.text="Kilo Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.VISIBLE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilBoy"){
            binding.textView2.text="Boy Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.VISIBLE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilUyeTipi"){
            binding.textView2.text="Üye Tipi Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.VISIBLE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilAylikUcret"){
            binding.textView2.text="Antrenör Aylık Ücret Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.VISIBLE
        }
        if(gelenProfilItem=="ProfilCinsiyet"){
            binding.textView2.text="Cinsiyet"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.VISIBLE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilHakkinda"){
            binding.textView2.text="Hakkında Kısmını Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.GONE
            binding.registerAntrenorTanit.visibility=View.VISIBLE
            binding.registerAylikucret.visibility=View.GONE
        }
        if(gelenProfilItem=="ProfilBranslar"){
            binding.textView2.text="Antrenör Branşlar Değiştir"
            binding.registerPageImageView.visibility=View.GONE
            binding.registerNameText.visibility=View.GONE
            binding.registerSurnameText.visibility=View.GONE
            binding.registerRadiogroupCinsiyet.visibility=View.GONE
            binding.registerBoy.visibility=View.GONE
            binding.registerKilo.visibility=View.GONE
            binding.registerRadiogroup.visibility=View.GONE
            binding.registerRadiogroupEgitmenTuru.visibility=View.VISIBLE
            binding.registerRadiogroupEgitmenTuru2.visibility=View.VISIBLE
            binding.registerAntrenorTanit.visibility=View.GONE
            binding.registerAylikucret.visibility=View.GONE
        }
    }

    //GALERİ İZİNLERİYLE İLGİLİ
    var secilenRegisterGorsel: Uri?=null
    var secilenRegisterBitMap: Bitmap?=null
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 321){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,322)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 322 && resultCode == Activity.RESULT_OK && data != null) {
            secilenRegisterGorsel = data.data
            if (secilenRegisterGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, secilenRegisterGorsel!!)
                    secilenRegisterBitMap = ImageDecoder.decodeBitmap(source)
                    binding.registerPageImageView.setImageBitmap(secilenRegisterBitMap)
                } else {
                    secilenRegisterBitMap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, secilenRegisterGorsel)
                    binding.registerPageImageView.setImageBitmap(secilenRegisterBitMap)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}