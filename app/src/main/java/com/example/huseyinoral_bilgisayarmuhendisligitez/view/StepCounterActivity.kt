package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.ActivityStepCounterBinding

class StepCounterActivity : AppCompatActivity() , SensorEventListener {
    private lateinit var binding: ActivityStepCounterBinding

    private var sensorManager: SensorManager? = null
    private var running = false
    private var toplamAdim  = 0f
    private var oncekiToplamAdimlar = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stepCounterAnasayfaMenu.setOnClickListener {
            Log.d("StepCounterActivity","Anasayfa Butonuna Tıklandı")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.stepCounterAdimSayarBaslat.setOnClickListener {
            //izin kontrol
            if (isPermissionGranted()) {
                requestPermission()
                loadData()
            }
        }
        binding.stepCounterAdimSifirla.setOnClickListener {
            resetSteps()
            loadData()
        }

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "Bu cihazda SENSOR algılanamadı", Toast.LENGTH_SHORT).show()
        }
        else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val stepAtilanAdim = binding.stepCounterAtilanAdim
        //Mevcut Adımların kullanıcıya gösterilmesi
        if (running) {
            toplamAdim = event!!.values[0]
            val mevcutAdim = toplamAdim.toInt() - oncekiToplamAdimlar.toInt()
            stepAtilanAdim.text = ("$mevcutAdim")
            binding.stepCounterYakilanKalori.text=(mevcutAdim * 0.05).toString()
        }
    }

    fun resetSteps() {
        val stepAtilanAdim = binding.stepCounterAtilanAdim

        binding.stepCounterAdimSifirla.setOnClickListener {
            oncekiToplamAdimlar = toplamAdim
            stepAtilanAdim.text = 0.toString()
            binding.stepCounterYakilanKalori.text = 0.toString()
            saveData()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", oncekiToplamAdimlar)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        Log.d("StepCounterActivity", "$savedNumber")
        oncekiToplamAdimlar = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //izin verildi adımsayar kullanabilir
                }
            }
        }
    }
}

