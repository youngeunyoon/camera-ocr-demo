package com.example.cameraxapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraxapp.databinding.ActivityMainBinding

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, CameraFragment())
                .commit()
        }
    }
}
