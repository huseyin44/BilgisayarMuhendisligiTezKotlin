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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentPhotosShareBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PersonalChatData
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.PhotoSharedByAntrenorData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class PhotosShareFragment : Fragment() {
    private var _binding: FragmentPhotosShareBinding? = null
    private val binding get() = _binding!!

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    private val db= Firebase.firestore
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //galeri izin kontrol
        binding.photosShareGorselSec.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //izni almamışız
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),65)
            } else {
                //izin zaten varsa
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,75)
            }
        }

        //fotograf paylas
        binding.photosSharePaylasButon.setOnClickListener {
            writePhotoSharedByAntrenor()
            Log.d("PhotosShareFragment","Fotoğraf Paylaş Butonuna Tıklandı")
        }
    }

    private fun PhotoSharedPageToHomePage(){
        val action=PhotosShareFragmentDirections.actionPhotosShareFragmentToHomePageFragment()
        findNavController().navigate(action)
    }

    private fun isInputCorrect(): Boolean {
        if (secilenGorsel == null){
            Toast.makeText(requireContext(), "Gönderi için resim seçilmeli.", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun writePhotoSharedByAntrenor() {
        val fromUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val PhotoSharedByAntrenorReference = FirebaseDatabase.getInstance().getReference("/PhotoSharedByAntrenor").push()

        db.collection("UserDetailPost").document(fromUserID).get()
            .addOnSuccessListener { result ->
                val isim = result.data?.get("isim").toString()
                val soyisim = result.data?.get("soyisim").toString()
                val profirresimurl = result.data?.get("profilresmiurl").toString()
                val isimsoyisim= "$isim $soyisim"
                val yorumText=binding.photosShareYorumText.text.toString()
                //
                val uuid = UUID.randomUUID()
                val gorselIsmi = "${uuid}.jpg"
                val reference = storage.reference
                val gorselReference = reference.child("PhotoSharedByAntrenorImagesFolder").child(gorselIsmi)

                if(isInputCorrect()) {
                    if (secilenGorsel != null) {
                        gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
                            val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("PhotoSharedByAntrenorImagesFolder").child(gorselIsmi)
                            yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                                val sharedPhotoUrlByAntrenor = uri.toString()

                                val sharedPhotoFirebase = PhotoSharedByAntrenorData(PhotoSharedByAntrenorReference.key,isimsoyisim,fromUserID,yorumText,profirresimurl,sharedPhotoUrlByAntrenor,System.currentTimeMillis() / 1000)
                                PhotoSharedByAntrenorReference.setValue(sharedPhotoFirebase).addOnSuccessListener {
                                    Log.d("PhotosShareFragment","PhotosShareFragment Paylaşılan Veriler RealtimeDatabase Gönderildi.")
                                    //veriler başarılı gitti anasayfaya dönüş
                                    PhotoSharedPageToHomePage()
                                }.
                                addOnFailureListener { exception ->
                                    Log.d("PhotosShareFragment","PhotosShareFragment Paylaşılan Veriler Gönderilemedi !!")
                                    Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
                                }

                            }.addOnFailureListener { exception ->
                                Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }.
            addOnFailureListener{ exception ->
                Log.d("PhotosShareFragment","Kullanıcı Profil Bilgileri Alınamadı")
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 65){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,75)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 75 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    binding.photosShareGorselSec.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, secilenGorsel)
                    binding.photosShareGorselSec.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}