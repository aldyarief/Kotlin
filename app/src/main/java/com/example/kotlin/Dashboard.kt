package com.example.kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class Dashboard : AppCompatActivity() {
    var name: String? = null
    var pass:kotlin.String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null
    var sharedPreferences: SharedPreferences? = null
    val PREFS_FILENAME = "com.example.kotlin"
    var masteruser:com.google.android.material.card.MaterialCardView?= null
    var logout:com.google.android.material.card.MaterialCardView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        sharedPreferences = this.getSharedPreferences(PREFS_FILENAME, 0)
        masteruser = findViewById<View>(R.id.user) as com.google.android.material.card.MaterialCardView
        logout = findViewById<View>(R.id.logout) as com.google.android.material.card.MaterialCardView


        logout!!.setOnClickListener {
            val editor = sharedPreferences!!.edit()
            editor.putBoolean("session_status",false)
            editor.apply();
            val intent = Intent(this@Dashboard, Login::class.java)
            finish()
            startActivity(intent)
        }

        masteruser!!.setOnClickListener {
            KirimData()
        }
    }

    private fun KirimData() {
        val intent = Intent(this@Dashboard, MasterUser::class.java)
        intent.putExtra("name", name)
        intent.putExtra("pass", pass)
        intent.putExtra("userid", userid)
        intent.putExtra("user", user)
        intent.putExtra("barang", barang)
        intent.putExtra("beli", beli)
        intent.putExtra("jual", jual)
        intent.putExtra("koreksi", koreksi)
        intent.putExtra("laporan", laporan)
        finish()
        startActivity(intent)
    }
}
