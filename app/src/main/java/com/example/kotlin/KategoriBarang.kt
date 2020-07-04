package com.example.kotlin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class KategoriBarang : AppCompatActivity() {

    var buttonsave: com.google.android.material.card.MaterialCardView?= null
    var server_url: String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null
    var username:kotlin.String? = null
    var password:kotlin.String? = null
    var pd: ProgressDialog? = null
    private var textname: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_barang)

        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        buttonsave = findViewById<View>(R.id.btnSave) as com.google.android.material.card.MaterialCardView
        server_url = "http://aldry.agustianra.my.id/nitip/kategori.php"
        pd = ProgressDialog(this)
        textname = findViewById<View>(R.id.textname) as EditText

        buttonsave!!.setOnClickListener {
            val name = textname!!.text.toString().trim { it <= ' ' }

            if (!name.isEmpty()) {
                simpanData(name,userid!!)
            } else if (name.isEmpty()) {
                textname!!.error = "username tidak boleh kosong"
                textname!!.requestFocus()
            }
        }
    }

    private fun simpanData(name: String,userid: String) {
        val requestQueue = Volley.newRequestQueue(this@KategoriBarang)
        pd!!.setCancelable(false)
        pd!!.setMessage("Harap Menunggu...")
        showDialog()
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, server_url,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    try {
                        val jObject = JSONObject(response)
                        val pesan = jObject.getString("pesan")
                        val hasil = jObject.getString("result")

                        if (hasil.equals("true", ignoreCase = true)) {
                            requestQueue.stop()
                            Toast.makeText(this@KategoriBarang, pesan, Toast.LENGTH_SHORT).show()
                            textname!!.text.clear()
                        } else {
                            Toast.makeText(this@KategoriBarang, pesan, Toast.LENGTH_SHORT).show()
                            requestQueue.stop()
                            textname!!.text.clear()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@KategoriBarang, "Error JSON", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@KategoriBarang, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param: MutableMap<String, String> =
                        HashMap()
                    param["name"] = name
                    param["userid"] = userid
                    return param
                }
            }
        requestQueue.add(stringRequest)
    }


    private fun showDialog() {
        if (!pd!!.isShowing) pd!!.show()
    }


    private fun hideDialog() {
        if (pd!!.isShowing) pd!!.dismiss()
    }


    override fun onBackPressed() {
        startActivity(Intent(this, Barang::class.java))
        finish()
    }
}