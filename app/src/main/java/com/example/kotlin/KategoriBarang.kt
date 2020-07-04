package com.example.kotlin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class KategoriBarang : AppCompatActivity() {

    var buttonsave: com.google.android.material.card.MaterialCardView?= null
    var name: String? = null
    var pass:kotlin.String? = null
    var server_url: String? = null
    var server_kategori: String? = null
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
    private var listView: ListView? = null
    private var textname: EditText? = null
    private var kategori: ArrayList<String>? = null
    private var result: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_barang)
        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        AmbilKategori()
        buttonsave = findViewById<View>(R.id.btnSave) as com.google.android.material.card.MaterialCardView
        server_url = "http://aldry.agustianra.my.id/nitip/kategori.php"
        pd = ProgressDialog(this)
        textname = findViewById<View>(R.id.textname) as EditText
        listView= findViewById(R.id.listViewProgramming) as ListView
        kategori = ArrayList()

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
                            kategori!!.clear()
                            AmbilKategori()
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
        startActivity(Intent(this, Dashboard::class.java))
        intent.putExtra("name", name!!.trim())
        intent.putExtra("pass", pass!!.trim())
        intent.putExtra("userid", userid!!.trim())
        intent.putExtra("user", user!!.trim())
        intent.putExtra("barang", barang!!.trim())
        intent.putExtra("beli", beli!!.trim())
        intent.putExtra("jual", jual!!.trim())
        intent.putExtra("koreksi", koreksi!!.trim())
        intent.putExtra("laporan", laporan!!.trim())
        finish()
    }

    private fun AmbilKategori() {
        server_kategori = "http://aldry.agustianra.my.id/nitip/ambilkategori.php"
        val requestQueue = Volley.newRequestQueue(this@KategoriBarang)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST, server_kategori,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    var j: JSONObject? = null
                    try {
                        j = JSONObject(response)
                        result = j.getJSONArray("data")
                        getKategori(result!!)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@KategoriBarang, "Error JSON", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@KategoriBarang, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return HashMap()
                }
            }
        requestQueue.add(stringRequest)
    }


    private fun getKategori(j: JSONArray) {
        //Traversing through all the items in the json array
        for (i in 0 until j.length()) {
            try {
                val json = j.getJSONObject(i)
                kategori!!.add(json.getString("nama"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        //Setting adapter to show the items in the listview
        val arrayAdapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,kategori!!);
        listView!!.setAdapter(arrayAdapter)
    }
}