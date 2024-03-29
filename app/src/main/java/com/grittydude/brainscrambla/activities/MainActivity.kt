package com.grittydude.brainscrambla.activities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.grittydude.brainscrambla.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}