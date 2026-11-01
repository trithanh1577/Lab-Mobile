package edu.vlu.week07.productroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.vlu.week07.productroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
