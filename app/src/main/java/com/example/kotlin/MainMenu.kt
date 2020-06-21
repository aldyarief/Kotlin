package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainMenu : AppCompatActivity() {
    var waktu_loading:kotlin.Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        waktu_loading = 4000
        Handler().postDelayed(Runnable { //setelah loading maka akan langsung berpindah ke home activity
            val home = Intent(this@MainMenu, Login::class.java)
            startActivity(home)
            finish()
        }, waktu_loading!!)
    }
}