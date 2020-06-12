package com.example.kotlin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
    var button: Button? = null
    var button2:android.widget.Button? = null
    var password: CheckBox? = null
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
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button = findViewById<View>(R.id.btnLogin) as Button
        button2 = findViewById<View>(R.id.btnTutup) as Button
        textname = findViewById<View>(R.id.textname) as EditText
        textpass = findViewById<View>(R.id.textpass) as EditText
        password = findViewById<View>(R.id.checkBox1) as CheckBox
        server_url = "https://aldry.000webhostapp.com/Login.php"
        pd = ProgressDialog(this)

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

        password!!.setOnCheckedChangeListener { buttonView, isChecked -> // TODO Auto-generated method stub
            if (!isChecked) {
                textpass!!.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                textpass!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }


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
