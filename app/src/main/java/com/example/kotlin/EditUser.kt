package com.example.kotlin

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_edit_user.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class EditUser : AppCompatActivity() {
    var button:com.google.android.material.card.MaterialCardView?= null
    var name: String? = null
    var pass:kotlin.String? = null
    var userid:kotlin.String? = null
    var user:kotlin.String? = null
    var barang:kotlin.String? = null
    var beli:kotlin.String? = null
    var jual:kotlin.String? = null
    var koreksi:kotlin.String? = null
    var laporan:kotlin.String? = null
    private var textpaslama: EditText? = null
    private  var textpasbaru: EditText? = null
    var server_url: String? = null
    var pd: ProgressDialog? = null
    var sharedPreferences: SharedPreferences? = null
    val PREFS_FILENAME = "com.example.kotlin"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        name = intent.getStringExtra("name")
        pass = intent.getStringExtra("pass")
        userid = intent.getStringExtra("userid")
        user = intent.getStringExtra("user")
        barang = intent.getStringExtra("barang")
        beli = intent.getStringExtra("beli")
        jual = intent.getStringExtra("jual")
        koreksi = intent.getStringExtra("koreksi")
        laporan = intent.getStringExtra("laporan")
        textpaslama = findViewById<View>(R.id.txtpaslama) as EditText
        textpasbaru = findViewById<View>(R.id.txtpasbaru) as EditText
        server_url = "http://aldry.agustianra.my.id/nitip/edituser.php"
        button = findViewById<View>(R.id.btnGanti) as com.google.android.material.card.MaterialCardView
        pd = ProgressDialog(this)
        sharedPreferences = this.getSharedPreferences(PREFS_FILENAME, 0)

        textpaslama!!.setText(pass)

        button!!.setOnClickListener {
            val passlama = textpaslama!!.text.toString().trim { it <= ' ' }
            val passbaru = textpasbaru!!.text.toString().trim { it <= ' ' }
            if (!passlama.isEmpty() && !passbaru.isEmpty()) {
                simpanData(passlama, passbaru, userid!!)
            } else if (passlama.isEmpty()) {
                txtpaslama!!.error = "username tidak boleh kosong"
                txtpaslama!!.requestFocus()
            } else if (passbaru.isEmpty()) {
                txtpasbaru!!.error = "password tidak boleh kosong"
                txtpasbaru!!.requestFocus()
            }
        }

    }
    private fun simpanData(passlama: String, passbaru: String, userid: String) {
        val requestQueue = Volley.newRequestQueue(this@EditUser)
        pd!!.setCancelable(false)
        pd!!.setMessage("Harap Menunggu...")
        showDialog()
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, server_url,
                Response.Listener { response ->
                    Log.d("response", response)
                    hideDialog()
                    try {
                        val jObject = JSONObject(response)
                        val pesan = jObject.getString("pesan")
                        val hasil = jObject.getString("result")

                        if (hasil.equals("true", ignoreCase = true)) {
                            Toast.makeText(this@EditUser, pesan, Toast.LENGTH_SHORT).show()
                            requestQueue.stop()
                            textpaslama!!.text.clear()
                            textpasbaru!!.text.clear()

                            // menyimpan login ke session
                            val editor = sharedPreferences!!.edit()
                            editor.putString("key_password", passbaru)
                            editor.apply();
                        } else {
                            Toast.makeText(this@EditUser, pesan, Toast.LENGTH_SHORT).show()
                            requestQueue.stop()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@EditUser, "Error JSON", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->
                    VolleyLog.d("ERROR", error.message)
                    Toast.makeText(this@EditUser, error.message, Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param: MutableMap<String, String> =
                        HashMap()
                    param["passlama"] = passlama
                    param["passbaru"] = passbaru
                    param["userid"] = userid
                    return param
                }
            }
        requestQueue.add(stringRequest)
    }

    override fun onBackPressed() {
        val intent = Intent(this@EditUser, Profile::class.java)
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
        startActivity(intent)
    }

    private fun showDialog() {
        if (!pd!!.isShowing) pd!!.show()
    }


    private fun hideDialog() {
        if (pd!!.isShowing) pd!!.dismiss()
    }
}