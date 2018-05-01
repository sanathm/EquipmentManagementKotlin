package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import java.util.*
import com.android.volley.VolleyError
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.text.SimpleDateFormat


data class User(val UserID: Int, var PIN: Int, var privelage: String, var FirstName: String, var Surname: String, var DOB: Date, var Address: String, var EmailAddress: String, var PhoneNumber: String, var Position: String) {

    var Equipment: MutableList<String> = mutableListOf()
    var Password = ""

    fun updateDB(context: Context) {

        //POST Request
        if (privelage == "Admin") {
            val url = GlobalVars.serverURL+"/post/admin.php"
            val postRequest = object : StringRequest(Request.Method.POST, url,
                    Response.Listener<String>() {response ->
                        val json = JSONObject(response)
                        if (!json.getBoolean("error")){
                            Toast.makeText(context,json.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener() {
                        Toast.makeText(context,"Error Connecting to Server", Toast.LENGTH_SHORT).show()
                    })
            {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = UserID.toString()
                    params["pin"] = PIN.toString()
                    params["pass"] = Password
                    params["name"] = FirstName
                    params["surname"] = Surname
                    val formatter = SimpleDateFormat("yyyy-MM-dd")

                    params["dob"] = formatter.format(DOB)
                    params["address"] = Address
                    params["email"] = EmailAddress
                    params["phone"] = PhoneNumber
                    params["position"] = Position

                    return params
                }
            }
            GlobalVars.queue.add(postRequest)
        } else {
            val url = GlobalVars.serverURL+"/post/employee.php"
            val postRequest = object : StringRequest(Request.Method.POST, url,
                    Response.Listener<String>() {response ->
                        val json = JSONObject(response)
                        if (!json.getBoolean("error")){
                            Toast.makeText(context,json.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener() {
                        Toast.makeText(context,"Error Connecting to Server", Toast.LENGTH_SHORT).show()
                    })
            {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = UserID.toString()
                    params["pin"] = PIN.toString()
                    params["name"] = FirstName
                    params["surname"] = Surname
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    params["dob"] = formatter.format(DOB)
                    params["address"] = Address
                    params["email"] = EmailAddress
                    params["phone"] = PhoneNumber
                    params["position"] = Position

                    return params
                }
            }
            GlobalVars.queue.add(postRequest)
        }
    }

    fun addtoDB(context: Context){
        val url = GlobalVars.serverURL+"/post/addEmployee.php"
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener<String>() { response ->
                    val json = JSONObject(response)
                    if (!json.getBoolean("error")){
                        Toast.makeText(context,json.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener() {
                    Toast.makeText(context,"Error Connecting to Server", Toast.LENGTH_SHORT).show()
                })
        {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = UserID.toString()
                params["pin"] = PIN.toString()
                params["name"] = FirstName
                params["surname"] = Surname
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                params["dob"] = formatter.format(DOB)
                params["address"] = Address
                params["email"] = EmailAddress
                params["phone"] = PhoneNumber
                params["position"] = Position

                return params
            }
        }
        GlobalVars.queue.add(postRequest)
    }

}