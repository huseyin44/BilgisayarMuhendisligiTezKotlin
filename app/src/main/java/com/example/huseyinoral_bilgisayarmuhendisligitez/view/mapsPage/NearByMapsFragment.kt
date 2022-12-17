package com.example.huseyinoral_bilgisayarmuhendisligitez.view.mapsPage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentNearByMapsBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NearByLocationData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class NearByMapsFragment : Fragment(),OnMapReadyCallback {

    private var _binding: FragmentNearByMapsBinding? = null
    private val binding get() = _binding!!

    var nearByLocationListesi = ArrayList<NearByLocationData>()

    private lateinit var locationManager:LocationManager
    private lateinit var locationListener:LocationListener

    private val callback = OnMapReadyCallback { googleMap ->
        onMapReady(mMap = googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearByMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(com.example.huseyinoral_bilgisayarmuhendisligitez.R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.nearBylocationYakindakiYerlerButton.setOnClickListener {
            nearMapsToLocationListMap()
        }
    }

    private fun nearMapsToLocationListMap(){
        println(nearByLocationListesi)
        val action=NearByMapsFragmentDirections.actionNearByMapsFragmentToLocationListFragment(nearByLocationListesi.toTypedArray())
        findNavController().navigate(action)
    }

    private val RAPIDAPI_KEY = "e7b364f6bfmsh03e4b7aa636711bp11ee1djsnd9bf6a72364f"
    private val RAPIDAPI_TRUEWAY_PLACES_HOST = "trueway-places.p.rapidapi.com"

    private val client = OkHttpClient()
    private fun getRapidApiAsync(url: String, rapidApiKey: String, rapidApiHost: String, callback: Callback?) {
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .addHeader("x-rapidapi-key", rapidApiKey)
            .addHeader("x-rapidapi-host", rapidApiHost)
            .build()
        val call: Call = client.newCall(request)
        if (callback != null) {
            call.enqueue(callback)
        }
    }

    private fun findPlacesNearby(lat: Double, lng: Double, type: String?, radius: Int, language: String?, callback: Callback?) {
        getRapidApiAsync(
            java.lang.String.format(
                Locale.US,
                "https://%s/FindPlacesNearby?location=%.6f,%.6f&type=%s&radius=%d&language=%s",
                RAPIDAPI_TRUEWAY_PLACES_HOST,
                lat,
                lng,
                type,
                radius,
                language
            ), RAPIDAPI_KEY, RAPIDAPI_TRUEWAY_PLACES_HOST, callback
        )
    }

    private fun showResults(responseStr: String) {
        try {
            val jsonObj = JSONObject(responseStr)
            // Getting results JSON Array node
            val results = jsonObj.getJSONArray("results")
            Log.d("NearByMapsFragment2", "Toplam Yerler: " + results.length())
            // looping through All Results
            //verileri yüklemeden temizleme
            nearByLocationListesi.clear()
            for (i in 0 until results.length()) {
                val result = results.getJSONObject(i)
                val id = result.getString("id")
                val name = result.getString("name")
                val address=if(result.has("address")) result.getString("address") else "Adres Bilgisi Bulunamadı"
                val phone_number=if(result.has("phone_number")) result.getString("phone_number") else "Telefon Bilgisi Bulunamadı"
                val website=if(result.has("website")) result.getString("website") else "Website Bilgisi Bulunamadı"
                val location = result.getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                val distance = if (result.has("distance")) result.getInt("distance") else 0
                Log.d("NearByMapsFragment3", java.lang.String.format(
                    Locale.US,
                    "result[%s]: id=%s; name=%s; address=%s; lat=%.6f; lng=%.6f; distance=%d",
                    i,
                    id,
                    name,
                    address,
                    lat,
                    lng,
                    distance)
                )

                val veriler= NearByLocationData(id,name,address,lat,lng,distance,phone_number,website)
                nearByLocationListesi.add(veriler)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        locationManager= activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener= LocationListener { location ->
            mMap.clear()
            val guncelKonum=LatLng(location.latitude,location.longitude)
            mMap.addMarker(MarkerOptions().position(guncelKonum).title("Güncel Konumunuz"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(guncelKonum))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

            binding.nearBylocationYakindakiYerlerButton.isVisible=true //mark yüklendikten sonra diger sayfaya gecebilsin diye

            findPlacesNearby(location.latitude, location.longitude, "gym", 3500, "en",
                object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("nearByLocationListesi","fail")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val responseStr: String = response.body!!.string()
                            // Do what you want to do with the response.
                            Log.d("NearByMapsFragment4", "findPlacesNearby sonuç: $responseStr")
                            showResults(responseStr)
                        } else {
                            // Request not successful
                        }
                    }
                })
        }

        //Izin kontrol
        if(ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),700)
        }else{
            //izin zaten verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==700){
            if(grantResults.isNotEmpty()){
                if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,200,1f,locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}