package com.zhair.stitchwell


import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity



class TailorInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tailor_info)
        findViewById<Button>(R.id.loginbtn2).setOnClickListener {
            finish()
        }



    }



}