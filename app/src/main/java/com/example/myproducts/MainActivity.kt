package com.example.myproducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myproducts.ui.products.ProductsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}