package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.HashMap

class Equipment(val ID: String, var Name: String, var Description: String, var Loaned: Int) {

    var inStock: Boolean = (Loaned==0)

    fun findLoaner():Int {
        if (GlobalVars.allEmployees != null)
            for (index in 0..GlobalVars.allEmployees.count()-1){
                if (GlobalVars.allEmployees[index].UserID == Loaned)
                    return index
            }
        return -1
    }

    fun updateDB(context: Context){
        val url = GlobalVars.serverURL+"/post/equipment.php"
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
                params["id"] = ID
                params["name"] = Name
                params["description"] = Description
                params["loaned"] = Loaned.toString()

                return params
            }
        }
        GlobalVars.queue.add(postRequest)
    }
}