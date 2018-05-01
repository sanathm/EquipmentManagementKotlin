package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.password_dialog.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        val sharedPref = applicationContext.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
        GlobalVars.serverURL = sharedPref.getString("server","http://10.0.1.31:9000/api")
        GlobalVars.cameraIP = sharedPref.getString("camera","http://10.0.1.31:9000/api/get/viewFeed.php")

        login_button.setOnClickListener {

            val id = ID_field.text
            val pin = pin_field.text

            // Instantiate the RequestQueue.

            GlobalVars.queue = Volley.newRequestQueue(this)
            val url = GlobalVars.serverURL+"/get/login.php?id=$id&pin=$pin"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        val json = JSONObject(response)
                        if (!json.getBoolean("error")){
                            val message = json.getString("message")
                            if (message == "Success") {
                                authorized(json.getString("user"))
                            } else if (message == "Admin"){

                                val userString = json.getString("user")
                                val jsonString = userString.substring(1,userString.length-1)
                                val userJson = JSONObject(jsonString)
                                val password = userJson.getString("password")

                                //Request password
                                val pwDialogBuilder = AlertDialog.Builder(this)
                                val pwDialogView = layoutInflater.inflate(R.layout.password_dialog,null)
                                pwDialogBuilder.setView(pwDialogView)
                                pwDialogBuilder.setCancelable(false)
                                pwDialogBuilder.setTitle("Enter Password")
                                pwDialogBuilder.setPositiveButton("Sign In",{ dialogInterface: DialogInterface, i: Int -> })
                                pwDialogBuilder.setNegativeButton("Cancel",{ dialogInterface: DialogInterface, i: Int -> })
                                val pwDialog = pwDialogBuilder.create()
                                pwDialog.show()
                                pwDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                                    val pwField = pwDialogView.findViewById<EditText>(R.id.password_field)
                                    if (pwField.text.toString() == password){
                                        val intent = authorizedAdmin(userJson)
                                        startActivity(intent)
                                        pwDialog.dismiss()
                                    } else {
                                        Toast.makeText(baseContext,"Incorrect Password", Toast.LENGTH_SHORT).show()
                                    }
                                })


                            } else {
                                Toast.makeText(baseContext,"Server Error. Please try again later", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(baseContext,"Database Error. Please try again later", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        val dialog = AlertDialog.Builder(this).create()
                        dialog.setTitle("Connection Error")
                        dialog.setMessage("Would you like to change the network settings?")
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{ dialogInterface: DialogInterface, i: Int -> })
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",{ dialogInterface: DialogInterface, i: Int -> })
                        dialog.show()
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                            val intent = Intent(this,Container::class.java)
                            startActivity(intent)
                            dialog.dismiss()
                        })
                    })

            // Add the request to the RequestQueue.
           GlobalVars.queue.add(stringRequest)

        }
    }



    fun authorized(userJson: String) {

        val jsonString = userJson.substring(1,userJson.length-1)
        val json = JSONObject(jsonString)

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("ID",json.getInt("employee_id"))
        intent.putExtra("PIN",json.getInt("pin"))
        intent.putExtra("Privelage","Employee")
        intent.putExtra("Name",json.getString("first_names"))
        intent.putExtra("Surname",json.getString("surname"))
        val dateString = json.getString("date_of_birth")
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        intent.putExtra("DOB",formatter.parse(dateString))
        intent.putExtra("Address",json.getString("physical_address"))
        intent.putExtra("Email",json.getString("email_address"))
        intent.putExtra("Phone",json.getString("phone_number"))
        intent.putExtra("Position",json.getString("position"))
        intent.putExtra("Equipment", arrayOf(5,1,2,3))



        startActivity(intent)
    }

    fun authorizedAdmin(json: JSONObject): Intent {

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("ID",json.getInt("employee_id"))
        intent.putExtra("PIN",json.getInt("pin"))
        intent.putExtra("Privelage","Admin")
        intent.putExtra("Name",json.getString("first_names"))
        intent.putExtra("Surname",json.getString("surname"))
        val dateString = json.getString("date_of_birth")
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        intent.putExtra("DOB",formatter.parse(dateString))
        intent.putExtra("Address",json.getString("physical_address"))
        intent.putExtra("Email",json.getString("email_address"))
        intent.putExtra("Phone",json.getString("phone_number"))
        intent.putExtra("Position",json.getString("position"))
        intent.putExtra("Password", json.getString("password"))

        return intent
    }
}
