package com.example.myproducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.myproducts.ui.products.ProductsFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var loadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingBar = findViewById(R.id.loadingBar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductsFragment.newInstance())
                .commit()
        }

//        Timer().schedule(object:TimerTask() {
//            override fun run() {
//                Log.d("IVONA",currentFocus?.toString()?:"null focused")
//            }
//        },1000,1000)
    }

    fun showLoading() {
        loadingBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loadingBar.visibility = View.GONE
    }
}