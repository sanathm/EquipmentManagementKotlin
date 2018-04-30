package com.group5.sanath.equipmentmanagement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_equipment_details.*

class EquipmentDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_details)

        val intent = getIntent()
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val descrip = intent.getStringExtra("description")
        val loaned = intent.getIntExtra("loaned",0)

        val currentEquip = Equipment(id,name,descrip,loaned)

        equip_ID.text = id
        equip_name.setText(name,TextView.BufferType.EDITABLE)
        equip_descrip.setText(descrip,TextView.BufferType.EDITABLE)

        if (GlobalVars.currentUser.privelage == "Employee") {
            loaned_ID.text = GlobalVars.currentUser.FirstName
            equip_editing.visibility = View.INVISIBLE
        } else {
            if (currentEquip.inStock)
                loaned_ID.text = "In Stock"
            else {
                val userIndex = currentEquip.findLoaner()
                if (userIndex == -1)
                    loaned_ID.text = "Error"
                else
                    loaned_ID.text = GlobalVars.allEmployees[userIndex].FirstName
            }
        }

    }
}
