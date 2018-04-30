package com.group5.sanath.equipmentmanagement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_emp_details.*
import java.text.SimpleDateFormat


class EmployeeDetails : AppCompatActivity() {
    lateinit var currentUser: User
    var userIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_emp_details)

        val emp_id = intent.extras.getInt("empID")

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

    }



}
