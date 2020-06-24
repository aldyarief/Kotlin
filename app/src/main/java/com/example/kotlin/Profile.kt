package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class Profile : AppCompatActivity() {
    private var textname: EditText? = null
    private  var textpass: EditText? = null
    var button:com.google.android.material.card.MaterialCardView?= null
    var userid:kotlin.String? = null
    var name:kotlin.String? = null
    var pass:kotlin.String? = null
    var user:kotlin.String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userid = intent.getStringExtra("userid")
        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        user = intent.getStringExtra("user")
        textname = findViewById<View>(R.id.txtname) as EditText
        textpass = findViewById<View>(R.id.txtpass) as EditText
        button = findViewById<View>(R.id.btnGanti) as com.google.android.material.card.MaterialCardView

        textname!!.setText(name)
        textpass!!.setText(pass)

    }

    override fun onBackPressed() {
        startActivity(Intent(this, Dashboard::class.java))
        intent.putExtra("user", user!!.trim())
        finish()
    }
}