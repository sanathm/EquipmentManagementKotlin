package com.group5.sanath.equipmentmanagement

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_emp_details.*
import org.json.JSONArray
import java.text.SimpleDateFormat


class EmployeeDetails : AppCompatActivity() {
    lateinit var currentUser: User
    var userIndex = 0
    var empEquipment = mutableListOf<Equipment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_emp_details)

        val emp_id = intent.extras.getInt("empID",-1)

        if (emp_id != -1) {
            for (index in 0..GlobalVars.allEmployees.count()-1){
                if (GlobalVars.allEmployees[index].UserID == emp_id){
                    currentUser = GlobalVars.allEmployees[index]
                    userIndex = index
                    break
                }
            }

            ID_field.text = currentUser.UserID.toString()
            nameField.setText(currentUser.FirstName,TextView.BufferType.EDITABLE)
            surnameField.setText(currentUser.Surname,TextView.BufferType.EDITABLE)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            dob_field.setText(formatter.format(currentUser.DOB),TextView.BufferType.EDITABLE)
            addr_field.setText(currentUser.Address,TextView.BufferType.EDITABLE)
            email_field.setText(currentUser.EmailAddress,TextView.BufferType.EDITABLE)
            phone_field.setText(currentUser.PhoneNumber,TextView.BufferType.EDITABLE)
            pos_field.setText(currentUser.Position,TextView.BufferType.EDITABLE)

            saveUser_button.setOnClickListener {
                currentUser.FirstName = nameField.text.toString()
                currentUser.Surname = surnameField.text.toString()
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                currentUser.DOB = formatter.parse(dob_field.text.toString())
                currentUser.Address = addr_field.text.toString()
                currentUser.EmailAddress = email_field.text.toString()
                currentUser.PhoneNumber = phone_field.text.toString()
                currentUser.Position = pos_field.text.toString()
                currentUser.updateDB(baseContext)
                GlobalVars.allEmployees[userIndex] = currentUser
                onBackPressed()
            }

            emp_equip.setOnClickListener {
                val id = currentUser.UserID
                val url = GlobalVars.serverURL+"/get/myEquipment.php?id=$id"

                // Request a string response from the provided URL.
                val stringRequest = StringRequest(Request.Method.GET, url,
                        Response.Listener<String> { response ->

                            val jsonArray = JSONArray(response)
                            val equip = mutableListOf<String>()
                            for (index in 0..jsonArray.length()-1) {

                                val json = jsonArray.getJSONObject(index)
                                val equip_id = json.getString("equip_id")
                                equip.add(equip_id)
                                val name = json.getString("name")
                                val desc = json.getString("description")
                                val loaned = json.getInt("loaned_out_to")
                                empEquipment.add(Equipment(equip_id,name,desc,loaned))
                            }
                            currentUser.Equipment = equip

                            GlobalVars.empEquipment = empEquipment

                            val intent = Intent(this,com.group5.sanath.equipmentmanagement.empEquipment::class.java)
                            intent.putExtra("user",currentUser.FirstName)
                            startActivity(intent)

                        },
                        Response.ErrorListener {
                            Toast.makeText(baseContext,"Connection Error", Toast.LENGTH_SHORT).show()
                        })

                // Add the request to the RequestQueue.
                GlobalVars.queue.add(stringRequest)
                Toast.makeText(baseContext,"Retrieving from Server", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Add new user
            emp_equip.visibility = View.INVISIBLE
            val id = GlobalVars.allEmployees.last().UserID+1
            ID_field.text = id.toString()

            saveUser_button.setOnClickListener {

                var pin = 0
                val FirstName = nameField.text.toString()
                val Surname = surnameField.text.toString()
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val DOB = formatter.parse(dob_field.text.toString())
                val Address = addr_field.text.toString()
                val EmailAddress = email_field.text.toString()
                val PhoneNumber = phone_field.text.toString()
                val Position = pos_field.text.toString()

                //Request new PIN
                val pwDialogBuilder = AlertDialog.Builder(this)
                val pwDialogView = layoutInflater.inflate(R.layout.pin_change_dialog,null)
                pwDialogBuilder.setView(pwDialogView)
                pwDialogBuilder.setCancelable(false)
                pwDialogBuilder.setTitle("Enter new PIN")
                pwDialogBuilder.setPositiveButton("OK",{ dialogInterface: DialogInterface, i: Int -> })
                pwDialogBuilder.setNegativeButton("Cancel",{ dialogInterface: DialogInterface, i: Int -> })
                val pwDialog = pwDialogBuilder.create()
                pwDialog.show()
                pwDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                    val pField1 = pwDialogView.findViewById<EditText>(R.id.pf1)
                    val pField2 = pwDialogView.findViewById<EditText>(R.id.pf2)
                    if (pField1.text.toString() == pField2.text.toString()){
                        pin = pField1.text.toString().toInt()
                        currentUser = User(id,pin,"Employee",FirstName,Surname,DOB, Address, EmailAddress, PhoneNumber, Position)
                        currentUser.addtoDB(baseContext)
                        GlobalVars.allEmployees.add(currentUser)
                        pwDialog.dismiss()
                        onBackPressed()
                    } else {
                        Toast.makeText(baseContext,"PIN numbers do not match", Toast.LENGTH_SHORT).show()
                    }
                })

            }

        }

    }



}
