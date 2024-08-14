package com.example.riddleme

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val overviewFragment = OverviewFragment()
        val getStartedFragment = GetStartedFragment()
        val overviewFragmentTextView: TextView = findViewById(R.id.overviewFragment)
        val getStartedFragmentTextView: TextView = findViewById(R.id.getStartedFragment)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FLfragment, overviewFragment)
            commit()
        }

        overviewFragmentTextView.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLfragment, overviewFragment)
                commit()
            }
        }

        getStartedFragmentTextView.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLfragment, getStartedFragment)
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.appVersion -> Toast.makeText(this, "App Version: 1.0.0", Toast.LENGTH_SHORT).show()
        }
        return true;
    }
}