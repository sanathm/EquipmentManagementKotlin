package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.password_dialog.*
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {

            val id = ID_field.text
            val pin = pin_field.text

            // Instantiate the RequestQueue.

            val queue = Volley.newRequestQueue(this)
            val url = "http://10.0.1.42:9000/get/login.php?id=$id&pin=$pin"
            println(url)

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> { response ->

                        println("Response is: ${response}")
                        val json = JSONObject(response)
                        if (!json.getBoolean("error")){
                            val message = json.getString("message")
                            if (message == "Success") {
                                println(json.getString("user"))
                                authorized(json.getString("user"))
                            } else if (message == "Admin"){

                                val userString = json.getString("user")
                                val jsonString = userString.substring(1,userString.length-1)
                                val userJson = JSONObject(jsonString)
                                val password = userJson.getString("password")

                                val pwDialogBuilder = AlertDialog.Builder(this)
                                val pwDialogView = layoutInflater.inflate(R.layout.password_dialog,null)
                                pwDialogBuilder.setView(pwDialogView)
                                pwDialogBuilder.setCancelable(false)
                                pwDialogBuilder.setTitle("Enter Password")
                                pwDialogBuilder.setPositiveButton("Sign In",{ dialogInterface: DialogInterface, i: Int -> })
                                val pwDialog = pwDialogBuilder.create()
                                pwDialog.show()
                                pwDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                                    val pwField = pwDialogView.findViewById<EditText>(R.id.password_field)
                                    if (pwField.text.toString() == password){
                                        val intent = authorizedAdmin(userJson)
                                        println("intent created")
                                        startActivity(intent)
                                    } else {
                                        println("Incorrect password")
                                    }
                                })


                            } else {
                                // TODO handle incorrect details
                                println("Incorrect Details")
                            }
                        } else {
                            // TODO handle db error
                            println("DB Error")
                        }
                    },
                    Response.ErrorListener { println("That didn't work!" )})

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

        }
    }

    fun authorized(userJson: String) {

        val jsonString = userJson.substring(1,userJson.length-1)
        println(jsonString)
        val json = JSONObject(jsonString)

        println("hellooo?")

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("ID",json.getInt("employee_id"))
        intent.putExtra("PIN",json.getInt("pin"))
        intent.putExtra("Privelage","Employee")
        intent.putExtra("Name",json.getString("first_names"))
        intent.putExtra("Surname",json.getString("surname"))
        intent.putExtra("DOB",Date())
        intent.putExtra("Address",json.getString("physical_address"))
        intent.putExtra("Email",json.getString("email_address"))
        intent.putExtra("Phone",json.getString("phone_number"))
        intent.putExtra("Position",json.getString("position"))
        intent.putExtra("Equipment", arrayOf(5,1,2,3))



        startActivity(intent)
    }

    fun authorizedAdmin(json: JSONObject): Intent {

        println("hellooo?")

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("ID",json.getInt("employee_id"))
        intent.putExtra("PIN",json.getInt("pin"))
        intent.putExtra("Privelage","Admin")
        intent.putExtra("Name",json.getString("first_names"))
        intent.putExtra("Surname",json.getString("surname"))
        intent.putExtra("DOB",Date())
        intent.putExtra("Address",json.getString("physical_address"))
        intent.putExtra("Email",json.getString("email_address"))
        intent.putExtra("Phone",json.getString("phone_number"))
        intent.putExtra("Position",json.getString("position"))
        intent.putExtra("Password", json.getString("password"))

        return intent
    }
}
