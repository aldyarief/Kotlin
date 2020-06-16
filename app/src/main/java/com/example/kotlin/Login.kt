package com.example.kotlin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Login : AppCompatActivity() {
    var button:com.google.android.material.card.MaterialCardView?= null
    var button2:com.google.android.material.card.MaterialCardView? = null
    private var textname: EditText? = null
    private  var textpass:EditText? = null
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



    var session = false
    var key_username:kotlin.String? = null
    var key_password:kotlin.String? = null
    var key_userid:kotlin.String? = null
    var key_user:kotlin.String? = null
    var key_barang:kotlin.String? = null
    var key_beli:kotlin.String? = null
    var key_jual:kotlin.String? = null
    var key_koreksi:kotlin.String? = null
    var key_laporan:kotlin.String? = null
    val session_status = "session_status"
    val PREFERENCE_NAME = "PREFERENCE_DATA"

    var sharedpreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    var editor = sharedpreferences!!.edit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button = findViewById<View>(R.id.btnLogin) as com.google.android.material.card.MaterialCardView
        button2 = findViewById<View>(R.id.btnTutup) as com.google.android.material.card.MaterialCardView
        textname = findViewById<View>(R.id.textname) as EditText
        textpass = findViewById<View>(R.id.textpass) as EditText
        server_url = "http://aldry.agustianra.my.id/nitip/Login.php"
        pd = ProgressDialog(this)


        // Cek session login jika TRUE maka langsung buka MainActivity
        session = sharedpreferences.getBoolean(session_status, false)
        password = sharedpreferences.getString(key_password, null)
        username = sharedpreferences.getString(key_username, null)
        userid = sharedpreferences.getString(key_userid, null)
        user = sharedpreferences.getString(key_user, null)
        barang = sharedpreferences.getString(key_barang, null)
        beli = sharedpreferences.getString(key_beli, null)
        jual = sharedpreferences.getString(key_jual, null)
        koreksi = sharedpreferences.getString(key_koreksi, null)
        laporan = sharedpreferences.getString(key_laporan, null)


        if (session==true) {
            val intent = Intent(this@Login, Dashboard::class.java)
            intent.putExtra(userid, userid)
            intent.putExtra(user, user)
            intent.putExtra(barang, barang)
            intent.putExtra(beli, beli)
            intent.putExtra(jual, jual)
            intent.putExtra(koreksi, koreksi)
            intent.putExtra(laporan, laporan)
            finish()
            startActivity(intent)
        }

        button!!.setOnClickListener {
            val name = textname!!.text.toString().trim { it <= ' ' }
            val pass = textpass!!.text.toString().trim { it <= ' ' }
            if (!name.isEmpty() && !pass.isEmpty()) {
                simpanData(name, pass)
            } else if (name.isEmpty()) {
                textname!!.error = "username tidak boleh kosong"
                textname!!.requestFocus()
            } else if (pass.isEmpty()) {
                textpass!!.error = "password tidak boleh kosong"
                textpass!!.requestFocus()
            }
        }

        button2!!.setOnClickListener { finish() }


    }

    private fun simpanData(name: String, pass: String) {
        val requestQueue = Volley.newRequestQueue(this@Login)
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
                        userid = jObject.getString("userid")
                        user = jObject.getString("user")
                        barang = jObject.getString("barang")
                        beli = jObject.getString("beli")
                        jual = jObject.getString("jual")
                        koreksi = jObject.getString("koreksi")
                        laporan = jObject.getString("laporan")
                        if (hasil.equals("true", ignoreCase = true)) {
                            KirimData()
                            requestQueue.stop()
                            textname!!.text.clear()
                            textpass!!.text.clear()

                            // menyimpan login ke session
                            editor.putBoolean(session_status, true)
                            editor.putString(key_username, name)
                            editor.putString(key_password, pass)
                            editor.putString(key_userid, userid)
                            editor.putString(key_user, user)
                            editor.putString(key_barang, barang)
                            editor.putString(key_beli, beli)
                            editor.putString(key_jual, jual)
                            editor.putString(key_koreksi, koreksi)
                            editor.putString(key_laporan, laporan)
                            editor.commit();
                        } else {
                            Toast.makeText(this@Login, pesan, Toast.LENGTH_SHORT).show()
                            requestQueue.stop()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@Login, "Error JSON", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@Login, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param: MutableMap<String, String> =
                        HashMap()
                    param["name"] = name
                    param["pass"] = pass
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

    private fun KirimData() {
        val intent = Intent(this@Login, Dashboard::class.java)
        intent.putExtra("name", textname!!.text.toString().trim { it <= ' ' })
        intent.putExtra("pass", textpass!!.text.toString().trim { it <= ' ' })
        intent.putExtra("userid", userid)
        intent.putExtra("user", user)
        intent.putExtra("barang", barang)
        intent.putExtra("beli", beli)
        intent.putExtra("jual", jual)
        intent.putExtra("koreksi", koreksi)
        intent.putExtra("laporan", laporan)
        startActivity(intent)
    }

}
