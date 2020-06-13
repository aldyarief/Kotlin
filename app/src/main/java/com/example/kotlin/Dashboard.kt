package com.example.kotlin

import android.os.Bundle
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
    }
}
